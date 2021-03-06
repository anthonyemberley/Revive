package com.pink2016.revive;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class e01StayCalmCall extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 8000; //in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e01_stay_calm_call);

        //Set up the corresponding audio file, play when page opens
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.e01staycalm);
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }


        //Set up timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(e01StayCalmCall.this, e03RemovePads.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

}
