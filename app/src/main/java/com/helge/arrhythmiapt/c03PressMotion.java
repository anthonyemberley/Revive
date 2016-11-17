package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class c03PressMotion extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 5000; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c03_press_motion);

        //Set up the corresponding audio file, play when page opens
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.c03pressmotion);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(c03PressMotion.this, c04_1PerformingCPR.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

    public void gotoc02(View view) {
        Intent intent = new Intent(this, c02SecondHand.class);
        startActivity(intent);
    }

    public void gotoc04(View view) {
        Intent intent = new Intent(this, c04_1PerformingCPR.class);
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        startActivity(intent);
    }
}
