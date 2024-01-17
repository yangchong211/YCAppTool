package com.yc.apptool.thread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.yc.apptool.App;
import com.yc.apptool.R;

public class ThreadMainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, ThreadMainActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_main);
        findViewById(R.id.tv_easy).setOnClickListener(this);
        findViewById(R.id.tv_poll).setOnClickListener(this);
        findViewById(R.id.tv_executors).setOnClickListener(this);
        findViewById(R.id.tv_thread).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_easy) {
            EasyExecutorActivity.startActivity(this);
        } else if (id == R.id.tv_poll) {
            ThreadPollActivity.startActivity(this);
        } else if (id == R.id.tv_executors) {
            startActivity(new Intent(this, ExecutorsTestActivity.class));
        } else if (id == R.id.tv_thread) {
            startActivity(new Intent(this, ThreadActivity.class));
        }
    }

}
