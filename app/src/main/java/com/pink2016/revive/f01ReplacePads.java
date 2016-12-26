package com.pink2016.revive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class f01ReplacePads extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f01_replace_pads);
    }

    public void gotof00(View view) {
        Intent intent = new Intent(this, f00Questions.class);
        startActivity(intent);
    }

    public void gotob02(View view) {
        Intent intent = new Intent(this, b02UseCase.class);
        startActivity(intent);
    }
}
