package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e06ExposeChest extends AppCompatActivity {

    String kidOrAdult = "";

    //Set variable for time to spend on this page
    int pagetime = 8000; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e06_expose_chest);

        //Set up the corresponding audio file, play when page opens
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.e06exposechest);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //System.out.println("here");


        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(e06ExposeChest.this, e07PrepPads.class);

                System.out.println("here2");
                System.out.println("kidoradult in runnable:" + kidOrAdult);
                Bundle extras = getIntent().getExtras();

                if (extras != null) {
                    String value = extras.getString("KidOrAdult");
                    kidOrAdult = value;
                    System.out.println(value);

                    //The key argument here must match that used in the other activity
                }

                //Saves input from e05, the kid/adult option
                mainIntent.putExtra("KidOrAdult",kidOrAdult);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

    public void gotoe05(View view) {
        Intent intent = new Intent(this, e05KidOrAdult.class);
        startActivity(intent);
    }

    public void gotoe07(View view) {
        Intent intent = new Intent(this, e07PrepPads.class);
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        startActivity(intent);
    }
}
