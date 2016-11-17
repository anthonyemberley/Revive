package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class c02SecondHand extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 5000; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c02_second_hand);

        //Set up the corresponding audio file, play when page opens
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.c02secondhand);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(c02SecondHand.this, c03PressMotion.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

    public void gotoc01(View view) {
        Intent intent = new Intent(this, c01FirstHand.class);
        startActivity(intent);
    }

    public void gotoc03(View view) {
        Intent intent = new Intent(this, c03PressMotion.class);
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        startActivity(intent);
    }
}
