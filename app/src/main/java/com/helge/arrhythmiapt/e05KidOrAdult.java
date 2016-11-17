package com.helge.arrhythmiapt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class e05KidOrAdult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e05_kid_or_adult);
    }

    public void iskid(View view) {
        Intent intent = new Intent(this, e06ExposeChest.class);
        intent.putExtra("KidOrAdult","kid");
        startActivity(intent);
    }

    public void isadult(View view) {
        Intent intent = new Intent(this, e06ExposeChest.class);
        intent.putExtra("KidOrAdult","adult");
        startActivity(intent);
    }

    public void gotoe03(View view) {
        Intent intent = new Intent(this, e03RemovePads.class);
        startActivity(intent);
    }

    public void gotob01(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        startActivity(intent);
    }
}
