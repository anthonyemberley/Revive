package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e02ConnectPhone extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 8000; // in milliseconds
    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e02_connect_phone);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e02redcap);
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen){
                    final Intent mainIntent = new Intent(e02ConnectPhone.this, e03RemovePads.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, pagetime);
    }

    public void gotoe01(View view) {
        Intent intent = new Intent(this, e01StayCalmCall.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onStart();
        toNextScreen = true;


        mediaPlayer.stop();
    };

    public void gotoe03(View view) {
        Intent intent = new Intent(this, e03RemovePads.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }
}
