package com.pink2016.revive;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Set;

public class e07BeginCPR extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 10000; // in milliseconds
    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    String CAPACITOR_ARDUINO_CODE = "2";
    String CAPACITOR_CHARGED_VALUE = "2";
    private int mInterval = 500; // 5 seconds by default, can be changed later
    private Handler mHandler;

    private enum ArduinoState {
        STATIC, ECG, CAPACITOR, DELIVER_SHOCK, CHECK_PADS
    }

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
        setContentView(R.layout.activity_e07_begin_cpr);

        mHandler = new Handler();
        startRepeatingTask();

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e13startcpr);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        mHandlerUsb = new MyHandler(this);

        //Set up the timer for staying on this page
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(!toNextScreen){
//                    final Intent mainIntent = new Intent(e07BeginCPR.this, e08Analyzing.class);
//                    startActivity(mainIntent);
//                    finish();
//                }
//
//            }
//        }, pagetime);


        //write to arduino every half second to check for capacitor charge


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                writeToArduino(CAPACITOR_ARDUINO_CODE); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
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
        toNextScreen = false;
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

    private void capacitorFullyCharged(){
        Intent intent = new Intent(e07BeginCPR.this, e08Analyzing.class);
        toNextScreen = true;
        mHandler.removeCallbacks(mStatusChecker);
        mediaPlayer.stop();
        startActivity(intent);
    }

    private void writeToArduino(String writeData){
        if(usbService != null){
            usbService.write(writeData.getBytes());
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<e07BeginCPR> mActivity;

        public MyHandler(e07BeginCPR activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    e07BeginCPR activity = mActivity.get();

                    if(data.equals(activity.CAPACITOR_CHARGED_VALUE)){
                        //capacitor charged!!
                        activity.capacitorFullyCharged();
                    }

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


    public void gotoe08(View view) {
        Intent intent = new Intent(this, e11ReadyToShock.class);
        toNextScreen = true;
        mediaPlayer.stop();
        mHandler.removeCallbacks(mStatusChecker);
        startActivity(intent);
    }

    public void gotob01(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Revive")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(e07BeginCPR.this, FirstTutorialScreen.class);
                        mHandler.removeCallbacks(mStatusChecker);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }})
                .setNegativeButton("No", null).show();

    }
}
