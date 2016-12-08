package com.helge.arrhythmiapt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class f02SafetyCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f02_safety_check);
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
