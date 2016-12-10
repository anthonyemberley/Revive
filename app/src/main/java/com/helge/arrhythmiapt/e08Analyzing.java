package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class e08Analyzing extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 7000;// in milliseconds
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e08_analyzing);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e08analyzing);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(e08Analyzing.this, e10Advised.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

    @Override
    protected void onPause() {
        super.onStart();
        mediaPlayer.stop();

    };

}
