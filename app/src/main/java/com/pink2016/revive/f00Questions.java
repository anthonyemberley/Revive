package com.pink2016.revive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class f00Questions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f00_questions);
    }

    public void gotof01(View view) {
        Intent intent = new Intent(this, f01ReplacePads.class);
        startActivity(intent);
    }

    public void gotof02(View view) {
        Intent intent = new Intent(this, f02SafetyCheck.class);
        startActivity(intent);
    }

    public void gotof03(View view) {
        Intent intent = new Intent(this, f03BatteryPower.class);
        startActivity(intent);
    }

    public void gotof04(View view) {
        Intent intent = new Intent(this, f04CellSignal.class);
        startActivity(intent);
    }

    public void gotof05(View view) {
        Intent intent = new Intent(this, f05Instructions.class);
        startActivity(intent);
    }

    public void gotob02(View view) {
        Intent intent = new Intent(this, b02UseCase.class);
        startActivity(intent);
    }
}
