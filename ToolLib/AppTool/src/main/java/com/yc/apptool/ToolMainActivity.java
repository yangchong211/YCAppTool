package com.yc.apptool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.yc.apptool.transition.TransitionActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.statusbar.bar.StateAppBar;

public class ToolMainActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundTextView tvView1;
    private RoundTextView tvView2;
    private RoundTextView tvView3;
    private RoundTextView tvView4;
    private RoundTextView tvView5;
    private RoundTextView tvView6;
    private RoundTextView tvView7;
    private RoundTextView tvView8;
    private RoundTextView tvView9;
    private RoundTextView tvView10;
    private RoundTextView tvView11;
    private RoundTextView tvView12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_main);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
        initListener();
        initData();
    }

    private void initView() {

    }

    private void initListener() {
        tvView1.setOnClickListener(this);
        tvView2.setOnClickListener(this);
        tvView3.setOnClickListener(this);
        tvView4.setOnClickListener(this);
        tvView5.setOnClickListener(this);
        tvView6.setOnClickListener(this);
        tvView7.setOnClickListener(this);
    }

    private void initData() {
        tvView1.setText("1.版本更新案例库");
        tvView2.setText("2.Activity之间转场动画");
        tvView3.setText("3.各种动画合集案例");
        tvView4.setText("4.测试线程循环执行案例");
        tvView5.setText("5.测试方法耗时操作");
        tvView6.setText("6.测试心跳服务");
    }

    @Override
    public void onClick(View v) {
        if (v == tvView1) {
            startActivity(new Intent(this, UploadActivity.class));
        } else if (v == tvView2) {
            startActivity(new Intent(this, TransitionActivity.class));
        } else if (v == tvView3) {

        } else if (v == tvView4) {

        } else if (v == tvView5) {
            //测试一下

        } else if (v == tvView6) {
        }
    }

}