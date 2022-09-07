package com.yc.apptool;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.yc.statusbar.bar.StateAppBar;

public class ToolMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_main);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
    }
}