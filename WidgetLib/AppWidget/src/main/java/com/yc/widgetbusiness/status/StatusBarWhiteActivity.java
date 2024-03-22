package com.yc.widgetbusiness.status;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.statusbar.bar.StateAppBar;

import cn.ycbjie.ycstatusbar.R;

public class StatusBarWhiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_white);

        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
    }
}
