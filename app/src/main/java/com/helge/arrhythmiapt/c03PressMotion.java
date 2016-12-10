package com.helge.arrhythmiapt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class c03PressMotion extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 5000; // in milliseconds
    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c03_press_motion);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.c03pressrelease);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen){
                    final Intent mainIntent = new Intent(c03PressMotion.this, c04CprBeat.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, pagetime);
    }

    public void gotoc02(View view) {
        Intent intent = new Intent(this, c02SecondHand.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }

    public void gotoc04(View view) {
        Intent intent = new Intent(this, c04CprBeat.class);
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
                        Intent intent = new Intent(c03PressMotion.this, FirstTutorialScreen.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }})
                .setNegativeButton("No", null).show();

    }
}
