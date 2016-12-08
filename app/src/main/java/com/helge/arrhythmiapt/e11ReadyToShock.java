package com.helge.arrhythmiapt;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e11_ready_to_shock);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarHeart2);
        animation = ObjectAnimator.ofInt (findViewById(R.id.progressBarHeart2), "progress", 0, 100); // see this max value coming back here, we animale towards that value



        //timing handler
        mHandler = new Handler();

        ;
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
        mediaPlayer = MediaPlayer.create(this, R.raw.e10_1breadytoshock);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }
    }



    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        mediaPlayer.stop();
        startActivity(intent);
    }

    public void presstoshock(View view) {

    }
}
