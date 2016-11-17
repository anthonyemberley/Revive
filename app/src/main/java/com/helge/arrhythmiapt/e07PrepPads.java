package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class e07PrepPads extends AppCompatActivity {

    String kidOrAdult = "";

    //Set variable for time to spend on this page
    int pagetime = 8000; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e07_prep_pads);

        //Set up the corresponding audio file, play when page opens
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.e07preppads);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("KidOrAdult");
            kidOrAdult = value;
            //The key argument here must match that used in the other activity
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (kidOrAdult.equals("kid")) {
                    final Intent mainIntent = new Intent(e07PrepPads.this, e08_2KidPads.class);
                    startActivity(mainIntent);
                    //Saves input from e05, the kid/adult option
                    mainIntent.putExtra("KidOrAdult",kidOrAdult);
                    finish();
                } else if (kidOrAdult.equals("adult")){
                    final Intent mainIntent = new Intent(e07PrepPads.this, e08_1AdultPads.class);
                    startActivity(mainIntent);
                    //Saves input from e05, the kid/adult option
                    mainIntent.putExtra("KidOrAdult",kidOrAdult);
                    finish();
                }
            }
        }, pagetime);
    }
}
