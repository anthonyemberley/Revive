package com.helge.arrhythmiapt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class c04_2Checkmark extends AppCompatActivity {

    //Set variable for time to spend on this page
    int pagetime = 50; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c04_2_checkmark);

        //Set up the timer for staying on this page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(c04_2Checkmark.this, c04_1PerformingCPR.class);
                startActivity(mainIntent);
                finish();
            }
        }, pagetime);
    }
}
