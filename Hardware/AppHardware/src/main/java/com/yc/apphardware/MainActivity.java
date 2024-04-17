package com.yc.apphardware;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PresentationActivity.startActivity(this);
//        startActivity(new Intent(this, KeyCodeActivity.class));
//        startActivity(new Intent(this, KeyCodeActivity2.class));
    }
}