package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e07PrepPads extends AppCompatActivity {

    String kidOrAdult = "";
    boolean toNextScreen = false;
    //Set variable for time to spend on this page
    int pagetime = 6000; // in milliseconds
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e07_prep_pads);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e07preppads);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
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
                        final Intent mainIntent = new Intent(e07PrepPads.this, e08_2KidPads.class);
                        startActivity(mainIntent);
                        finish();
                    } else if (kidOrAdult.equals("adult")){
                        final Intent mainIntent = new Intent(e07PrepPads.this, e08_1AdultPads.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }

            }
        }, pagetime);
    }

    public void gotoe08(View view) {
        Bundle extras = getIntent().getExtras();
        mediaPlayer.stop();
        if(extras != null) {
            String value = extras.getString("KidOrAdult");
            toNextScreen = true;

            kidOrAdult = value;
        //The key argument here must match that used in the other activity
        }
        if (kidOrAdult.equals("kid")) {
            final Intent mainIntent = new Intent(e07PrepPads.this, e08_2KidPads.class);
            toNextScreen = true;

            startActivity(mainIntent);
            finish();
        } else if (kidOrAdult.equals("adult")){
            final Intent mainIntent = new Intent(e07PrepPads.this, e08_1AdultPads.class);
            toNextScreen = true;

            startActivity(mainIntent);
            finish();
        }

    }

    public void gotoe06(View view) {
        Intent intent = new Intent(this, e06ExposeChest.class);
        toNextScreen = true;
        intent.putExtra("KidOrAdult",kidOrAdult);
        mediaPlayer.stop();
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }
}
