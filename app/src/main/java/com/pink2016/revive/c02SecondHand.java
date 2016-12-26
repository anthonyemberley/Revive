package com.pink2016.revive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class c02SecondHand extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 5000; // in milliseconds
    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c02_second_hand);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.c02secondhand);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen){
                    final Intent mainIntent = new Intent(c02SecondHand.this, c03PressMotion.class);
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

    public void gotoc03(View view) {
        Intent intent = new Intent(this, c03PressMotion.class);
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

    public void gotob01(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Revive")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(c02SecondHand.this, FirstTutorialScreen.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }})
                .setNegativeButton("No", null).show();

    }
}
