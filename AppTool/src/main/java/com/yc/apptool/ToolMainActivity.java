package com.yc.apptool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.yc.roundcorner.view.RoundTextView;
import com.yc.statusbar.bar.StateAppBar;

public class ToolMainActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundTextView tvAppUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_main);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        tvAppUpload = findViewById(R.id.tv_app_upload);

        tvAppUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvAppUpload){
            startActivity(new Intent(this,UploadActivity.class));
        }
    }
}