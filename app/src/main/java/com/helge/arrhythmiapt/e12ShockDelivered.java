package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class e12ShockDelivered extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 300000; // in milliseconds
    // Handler myHandler = new Handler();
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e12_shock_delivered);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e10_2shockdelivered);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this  //myHandler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(e12ShockDelivered.this, e13StartCPR.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

}
