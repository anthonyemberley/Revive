package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e08_2KidPads extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 8000; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e08_2_kid_pads);

        //Set up the corresponding audio file, play when page opens
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.e08placepads);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(e08_2KidPads.this, e09Analyzing.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

    public void gotoe09(View view) {
        Intent intent = new Intent(this, e09Analyzing.class);
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        startActivity(intent);
    }
}
