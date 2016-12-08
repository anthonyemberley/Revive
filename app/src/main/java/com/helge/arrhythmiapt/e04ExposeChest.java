package com.helge.arrhythmiapt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e04ExposeChest extends AppCompatActivity {

    boolean toNextScreen = false;
    MediaPlayer mediaPlayer;
    int pagetime = 7000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e04_expose_chest);

        //Set up the corresponding audio file, play when page opens
        mediaPlayer = MediaPlayer.create(this, R.raw.e06exposechest);
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            // mediaPlayer.setLooping(true);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!toNextScreen) {
                    final Intent mainIntent = new Intent(e04ExposeChest.this, e05PrepPads.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, pagetime);

    }

    public void gotoe03(View view) {
        Intent intent = new Intent(this, e03RemovePads.class);
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

    // Residual code from the kid/adult command in the first version
/*    public void iskid(View view) {
        Intent intent = new Intent(this, e05PrepPads.class);
        intent.putExtra("KidOrAdult","kid");
        startActivity(intent);
    }

    public void isadult(View view) {
        Intent intent = new Intent(this, e05PrepPads.class);
        intent.putExtra("KidOrAdult","adult");
        startActivity(intent);
    }*/
}
