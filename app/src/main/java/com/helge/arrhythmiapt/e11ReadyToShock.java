package com.helge.arrhythmiapt;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Set;

public class e11ReadyToShock extends AppCompatActivity {
    Button shockButton;
    //Set variable for time to spend on this page
    int pagetime = 8000; // in milliseconds
    private Handler mHandler;
    boolean buttonCancelled = false;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    int i=0;
    ObjectAnimator animation;
    MediaPlayer mediaPlayer;

    private UsbService usbService;
    private MyHandler mHandlerUsb;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandlerUsb);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e11_ready_to_shock);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarHeart2);
        animation = ObjectAnimator.ofInt (findViewById(R.id.progressBarHeart2), "progress", 0, 100); // see this max value coming back here, we animale towards that value



        //timing handler
        mHandler = new Handler();
        mHandlerUsb = new MyHandler(this);

        mCountDownTimer=new CountDownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressBar.setProgress(i);

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                mProgressBar.setProgress(i);
            }
        };



        //Set up Button
        shockButton = (Button) findViewById(R.id.presstoshock);
        shockButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Start 3 second timer
                        buttonCancelled = false;
                        mCountDownTimer.start();

                        animation.setDuration (3000); //in milliseconds
                        animation.setInterpolator (new DecelerateInterpolator());
                        animation.start();
                        mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!buttonCancelled){
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                // Vibrate for 500 milliseconds
                                v.vibrate(500);
                                if(usbService != null){
                                    usbService.write("3".getBytes());
                                }
                                Intent intent = new Intent(e11ReadyToShock.this, e12ShockDelivered.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }, 3000);

                        return true;
                    case MotionEvent.ACTION_UP:
                        //Cancel 3 second timer
                        mCountDownTimer.cancel();
                        i = 0;
                        buttonCancelled = true;
                        mProgressBar.clearAnimation();
                        mProgressBar.setProgress(0);
                        animation.cancel();
                        mHandler.removeCallbacksAndMessages(null);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        mCountDownTimer.cancel();
                        i = 0;
                        buttonCancelled = true;
                        animation.cancel();
                        mHandler.removeCallbacksAndMessages(null);
                        mProgressBar.clearAnimation();
                        mProgressBar.setProgress(0);


                        //cancel timer if not already done
                }
                return false;
            }
        });
        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e11readytoshock);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }
    }



    public void gotob01(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Revive")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(e11ReadyToShock.this, FirstTutorialScreen.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }})
                .setNegativeButton("No", null).show();

    }


    public void presstoshock(View view) {

    }





    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }


    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    //Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    //Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    //Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    //Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    //Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    //USB stuff
    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<e11ReadyToShock> mActivity;

        public MyHandler(e11ReadyToShock activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    //mActivity.get().display.append(data);
                    //This is where we do stuff with the message we receive
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
