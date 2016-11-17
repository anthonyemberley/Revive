package com.helge.arrhythmiapt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class FirstTutorialScreen extends AppCompatActivity {

    int pagetime = 1000; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_tutorial_screen);

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(FirstTutorialScreen.this, b02UseCase.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }

//    public void b01tob02(View view) {
//        Intent intent = new Intent(this, b02usecase.class);
//        startActivity(intent);
//    }

}
