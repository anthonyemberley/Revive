package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e13StartCPR extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 5000; // in milliseconds
    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e13_start_cpr);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e12startcpr);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            //mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen){
                    final Intent mainIntent = new Intent(e13StartCPR.this, c01FirstHand.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, pagetime);
    }

    public void gotoc01(View view) {
        Intent intent = new Intent(this, c01FirstHand.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }
}