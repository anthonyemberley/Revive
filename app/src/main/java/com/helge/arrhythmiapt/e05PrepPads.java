package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e05PrepPads extends AppCompatActivity {

    String kidOrAdult = "";

    //Set variable for time to spend on this page
    int pagetime = 8000; // in milliseconds
    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e05_prep_pads);

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
                final Intent mainIntent = new Intent(e05PrepPads.this, e06PlacePads.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);



        //Residual kid/adult code from version 1
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen){
                    final Intent mainIntent = new Intent(e05PrepPads.this, e06PlacePads.class);

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

            }
        }, pagetime);*/
    }

    public void gotoe05(View view) {
        Intent intent = new Intent(this, e04ExposeChest.class);
        toNextScreen = true;
        mediaPlayer.stop();
        startActivity(intent);
    }

    public void gotoe06(View view) {
        Intent intent = new Intent(this, e06PlacePads.class);
        toNextScreen = true;
        mediaPlayer.stop();

        //Residual code from kid/adult decision in version 1
 /*       Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String value = extras.getString("KidOrAdult");
            kidOrAdult = value;
            System.out.println(value);

            //The key argument here must match that used in the other activity
        }
        intent.putExtra("KidOrAdult",kidOrAdult);*/

        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        mediaPlayer.stop();
        toNextScreen = true;
        startActivity(intent);
    }
}
