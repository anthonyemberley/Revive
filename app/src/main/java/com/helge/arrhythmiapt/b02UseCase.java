package com.helge.arrhythmiapt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class b02UseCase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b02_use_case);
    }

    public void b02toe01(View view) {
        Intent intent = new Intent(this, e01StayCalmCall.class);
        startActivity(intent);
    }

    public void gotof00(View view) {
        Intent intent = new Intent(this, f00Questions.class);
        startActivity(intent);
    }
}
