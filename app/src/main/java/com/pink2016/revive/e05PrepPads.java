package com.pink2016.revive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e05PrepPads extends AppCompatActivity {

    String kidOrAdult = "";

    //Set variable for time to spend on this page
    int pagetime = 6000; // in milliseconds
    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e05_prep_pads);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e05preppads);
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen) {
                    final Intent mainIntent = new Intent(e05PrepPads.this, e06PlacePads.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, pagetime);



        //Residual kid/adult code from version 1
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen){
                    final Intent mainIntent = new Intent(e05PrepPads.this, e06PlacePads.class);

                    mainIntent.putExtra("KidOrAdult",kidOrAdult);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, pagetime);
    }

    @Override
    protected void onPause() {
        super.onStart();
        toNextScreen = true;

        mediaPlayer.stop();
    };

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
        new AlertDialog.Builder(this)
                .setTitle("Revive")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(e05PrepPads.this, FirstTutorialScreen.class);
                        mediaPlayer.stop();
                        startActivity(intent);
                    }})
                .setNegativeButton("No", null).show();

    }
}
