package com.yc.appmvx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yc.keyeventlib.demo.KeyCodeActivity;
import com.yc.keyeventlib.demo.KeyCodeActivity2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PresentationActivity.startActivity(this);
        startActivity(new Intent(this, KeyCodeActivity.class));
//        startActivity(new Intent(this, KeyCodeActivity2.class));
    }
}