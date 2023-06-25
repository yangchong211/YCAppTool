package com.yc.apptool.thread;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.yc.apptool.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ExecutorsTestActivity extends AppCompatActivity implements View.OnClickListener {

    int number = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executors_test);
        findViewById(R.id.tv_0).setOnClickListener(this);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_0) {//最普通方式
            createThread();
        } else if (id == R.id.tv_1) {
            newFixedThreadPool();
            //newFixedThreadPool1();
        } else if (id == R.id.tv_2) {
            newSingleThreadExecutor();
        } else if (id == R.id.tv_3) {
            newCachedThreadPool();
        } else if (id == R.id.tv_4) {
            newScheduledThreadPool();
        }
    }

    /**
     * 最普通创建线程的方式
     */
    private void createThread() {
        for (int i = 1; i <= number; i++) {
            final int index = i;
            //线程抢占资源，会占用cpu
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    Log.e("潇湘剑雨Thread", "线程："+threadName+",正在执行第" + index + "个任务");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        //todo 模拟线程导致OOM操作
    }

    /**
     * 由于newFixedThreadPool只有核心线程，并且这些线程都不会被回收，也就是它能够更快速的响应外界请求
     */
    private void newFixedThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= number; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    Log.e("潇湘剑雨newFixedThreadPool", "线程："+threadName+",正在执行第" + index + "个任务");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void newSingleThreadExecutor() {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= number; i++) {
            final int index = i;
            singleThreadPool.execute(new Runnable() {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    Log.e("潇湘剑雨 newSingleThreadExecutor", "线程："+threadName+",正在执行第" + index + "个任务");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 缓存线程池，
     */
    private void newCachedThreadPool() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 1; i <= number; i++) {
            final int index = i;
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    Log.e("潇湘剑雨newCachedThreadPool", "线程：" + threadName + ",正在执行第" + index + "个任务");
                    try {
                        long time =  500;
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void newScheduledThreadPool() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        //延迟2秒后执行该任务
        scheduledThreadPool.schedule(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                Log.e("潇湘剑雨newScheduledThreadPool", "线程：" + threadName + ",正在执行");
            }
        }, 2, TimeUnit.SECONDS);
        //延迟1秒后，每隔2秒执行一次该任务
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                Log.e("潇湘剑雨", "线程：" + threadName + ",正在执行");
            }
        }, 1, 2, TimeUnit.SECONDS);
    }


}
