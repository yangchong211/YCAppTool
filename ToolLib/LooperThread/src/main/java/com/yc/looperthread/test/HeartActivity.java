package com.yc.looperthread.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.looperthread.R;
import com.yc.looperthread.abs.IDoAction;
import com.yc.looperthread.heart.HeartManager;
import com.yc.looperthread.looper.HandlerLoopThread;
import com.yc.looperthread.looper.ScheduledLoopThread;
import com.yc.looperthread.looper.ThreadLoopThread;
import com.yc.looperthread.looper.TimerLoopThread;
import com.yc.looperthread.looper.WhileLoopThread;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartActivity extends AppCompatActivity {

    private static final String TAG = "HeartActivity";
    private TextView tvView1;
    private TextView tvView2;
    private TextView tvView3;
    private TextView tvView4;
    private TextView tvView5;
    private TextView tvView6;
    private TextView tvView7;

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
        setContentView(R.layout.activity_looper_test);

        initView();
        initListener();
    }

    private void initView() {
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        tvView5 = findViewById(R.id.tv_view_5);
        tvView6 = findViewById(R.id.tv_view_6);
        tvView7 = findViewById(R.id.tv_view_7);

        tvView1.setText("1.开启服务");
        tvView2.setText("2.模拟一个崩溃，看看是否重启");
        tvView3.setText("3.模拟一个崩溃，看看是否重启");
        tvView4.setText("4.模拟一个崩溃，看看是否重启");
    }

    private void initListener() {
        HeartManager.getInstance().start();
        tvView1.setOnClickListener(v -> HeartManager.getInstance().start());
        tvView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer.parseInt("12.3");
            }
        });
        tvView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Object> objects = new ArrayList<>();
                objects.get(5);
            }
        });
        tvView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = null;
                activity.isDestroyed();
            }
        });
        tvView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultLoopThread.beginLoop();
                Log.d(TAG,"开始轮训");
            }
        });
        tvView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultLoopThread.endLoop();
                Log.d(TAG,"暂停轮训");
            }
        });
        tvView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultLoopThread.release();
                Log.d(TAG,"销毁轮训");
            }
        });
    }

    /**
     * {@link WhileLoopThread}
     * {@link TimerLoopThread}
     * {@link ScheduledLoopThread}
     * {@link ThreadLoopThread}
     * {@link HandlerLoopThread}
     */
    private final IDoAction defaultLoopThread = new ThreadLoopThread(){
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
