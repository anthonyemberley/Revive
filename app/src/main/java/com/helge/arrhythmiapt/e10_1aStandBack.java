package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e10_1aStandBack extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 4000; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e10_1a_stand_back);

        //Set up the corresponding audio file, play when page opens
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.e10_1astandclear);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(e10_1aStandBack.this, e10_1bReadyToShock.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        startActivity(intent);
    }
}
