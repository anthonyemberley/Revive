package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e06PlacePads extends AppCompatActivity {

    String kidOrAdult = "";
    boolean toNextScreen = false;

    //Set variable for time to spend on this page
    int pagetime = 9000; // in milliseconds
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e06_place_pads);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e08placepads);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen) {
                    final Intent mainIntent = new Intent(e06PlacePads.this, e07BeginCPR.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, pagetime);

        //Residual code from the kid/adult decision in version 1
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen){
                    Bundle extras = getIntent().getExtras();
                    System.out.println("here");

                    if (extras != null) {
                        String value = extras.getString("KidOrAdult");
                        kidOrAdult = value;
                        System.out.println(value);

                        //The key argument here must match that used in the other activity
                    }
                    if (kidOrAdult.equals("kid")) {
                        final Intent mainIntent = new Intent(e06PlacePads.this, e08_2KidPads.class);
                        startActivity(mainIntent);
                        finish();
                    } else if (kidOrAdult.equals("adult")){
                        final Intent mainIntent = new Intent(e06PlacePads.this, e07BeginCPR.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }

            }
        }, pagetime);*/
    }

    public void gotoe07(View view) {
        Intent intent = new Intent(this, e07BeginCPR.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }

    public void gotoe05(View view) {
        Intent intent = new Intent(this, e05PrepPads.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }

    //Residual code from kid/adult decision in version 1
/*    public void gotoe08(View view) {
        Bundle extras = getIntent().getExtras();
        mediaPlayer.stop();
        if(extras != null) {
            String value = extras.getString("KidOrAdult");
            toNextScreen = true;

            kidOrAdult = value;
        //The key argument here must match that used in the other activity
        }
        if (kidOrAdult.equals("kid")) {
            final Intent mainIntent = new Intent(e06PlacePads.this, e08_2KidPads.class);
            toNextScreen = true;

            startActivity(mainIntent);
            finish();
        } else if (kidOrAdult.equals("adult")){
            final Intent mainIntent = new Intent(e06PlacePads.this, e07BeginCPR.class);
            toNextScreen = true;

            startActivity(mainIntent);
            finish();
        }

    }*/
}
