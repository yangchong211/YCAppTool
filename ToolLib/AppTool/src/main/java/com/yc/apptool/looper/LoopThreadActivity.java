package com.yc.apptool.looper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.apptool.R;
import com.yc.looperthread.DefaultLoopThread;
import com.yc.looperthread.HandlerLoopThread;
import com.yc.looperthread.IDoAction;
import com.yc.looperthread.ScheduledLoopThread;
import com.yc.looperthread.TimerLoopThread;
import com.yc.looperthread.WhileLoopThread;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoopThreadActivity extends AppCompatActivity implements View.OnClickListener {

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

    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, LoopThreadActivity.class);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_text_view);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        tvView5 = findViewById(R.id.tv_view_5);
        tvView6 = findViewById(R.id.tv_view_6);
        tvView7 = findViewById(R.id.tv_view_7);
        tvView8 = findViewById(R.id.tv_view_8);
        tvView9 = findViewById(R.id.tv_view_9);
        tvView10 = findViewById(R.id.tv_view_10);
        tvView11 = findViewById(R.id.tv_view_11);
        tvView12 = findViewById(R.id.tv_view_12);
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
        tvView1.setText("1.开始轮训");
        tvView2.setText("2.停止轮训");
        tvView3.setText("3.结束轮训");
    }

    private final IDoAction defaultLoopThread = new WhileLoopThread(){
        @Override
        public void doAction() {
            super.doAction();
        }

        @Override
        public boolean isLoop() {
            //是否轮训的判断条件
            return true;
        }

        @Override
        public long getSleepTime() {
            //轮训的时间间隔
            return 1000;
        }
    };

    @Override
    public void onClick(View v) {
        if (v == tvView1){
            defaultLoopThread.beginLoop();
//            defaultScheduledThread.beginLoop();
            ToastUtils.showRoundRectToast("开始轮训");
        } else if (v == tvView2){
            defaultLoopThread.endLoop();
//            defaultScheduledThread.endLoop();
            ToastUtils.showRoundRectToast("暂停轮训");
        } else if (v == tvView3){
            defaultLoopThread.release();
//            defaultScheduledThread.release();
            ToastUtils.showRoundRectToast("销毁轮训");
        }
    }

    public void simplePolling() {
        while (true) {
            try {
                // 线程睡眠1秒钟
                Thread.sleep(1000);
                // 轮询的代码
                System.out.println("polling..."+ count++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int count ;
    public void scheduledPolling() {
        count = 0;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // 轮询的代码
            System.out.println("polling..." + count++);
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
}