package com.yc.apptest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.looperthread.heart.HeartManager;
import com.yc.roundcorner.view.RoundTextView;

public class HeartActivity extends AppCompatActivity {

    private RoundTextView tvView1;
    private RoundTextView tvView2;
    private RoundTextView tvView3;
    private RoundTextView tvView4;
    private RoundTextView tvView5;

    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, HeartActivity.class);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_main);

        initView();
        initListener();
    }

    private void initView() {
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        tvView5 = findViewById(R.id.tv_view_5);

        tvView1.setText("1.开启服务");
        tvView2.setText("2.模拟一个崩溃，看看是否重启");
    }

    private void initListener() {
        tvView1.setOnClickListener(v -> HeartManager.getInstance().start());
        tvView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer.parseInt("12.3");
            }
        });
    }
}
