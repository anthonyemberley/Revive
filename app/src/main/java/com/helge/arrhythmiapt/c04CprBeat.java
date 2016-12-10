package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class c04CprBeat extends AppCompatActivity {
    private int mInterval = 6000; // 6 seconds by default, can be changed later
    private Handler mHandler;
    public boolean imageIsShown;
    public ImageView checkImageView;
    public boolean readyToCheck;
    public MediaPlayer mediaPlayer;
    public MediaPlayer mediaPlayerSteph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c04_cpr_beat);
        mHandler = new Handler();
        startRepeatingTask();
        checkImageView = (ImageView)findViewById(R.id.checkMarkImageView);
        readyToCheck = true;

//        mediaPlayer = MediaPlayer.create(this, R.raw.e10_1breadytoshock);
//        if(!mediaPlayer.isPlaying() && mediaPlayer != null){
//            mediaPlayer.start();
//            mediaPlayer.setLooping(true);
//        }

        //add back metronome
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(c04CprBeat.this, R.raw.cprbeep);
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }

            }
        }, 2000);


        mediaPlayerSteph = MediaPlayer.create(this, R.raw.c04cprbeat);
        if(!mediaPlayerSteph.isPlaying()){
            mediaPlayerSteph.start();
        }

    }

    public void homeButtonPressed(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        mediaPlayer.stop();
        mediaPlayerSteph.stop();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onStart();
        mediaPlayer.stop();
        mediaPlayerSteph.stop();
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateImage();
                updateTime();//this function can change value of mInterval.
                imageIsShown = !imageIsShown;
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void updateImage(){
        if(readyToCheck){
            if (imageIsShown){
             checkImageView.setVisibility(View.INVISIBLE);
            }else{
             checkImageView.setVisibility(View.VISIBLE);
         }
        }

    }

    void updateTime(){
        if (imageIsShown){
            mInterval = 400;
        }else{
            mInterval = 200;
        }
    }

    public void gotoc03(View view) {
        Intent intent = new Intent(this, c03PressMotion.class);
        mediaPlayer.stop();
        mediaPlayerSteph.stop();
        startActivity(intent);
    }


    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
