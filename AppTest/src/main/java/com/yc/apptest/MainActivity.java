package com.yc.apptest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.looperthread.heart.HeartManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeartManager.getInstance().setCrash(false);
        findViewById(R.id.tv_view1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeartActivity.startActivity(MainActivity.this);
            }
        });
    }
}
