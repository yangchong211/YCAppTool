package com.yc.other.thread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.library.base.config.AppConfig;
import com.yc.other.R;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.yc.ycthreadpoollib.PoolThread;
import com.yc.ycthreadpoollib.callback.AsyncCallback;
import com.yc.ycthreadpoollib.callback.ThreadCallback;
import com.yc.ycthreadpoollib.deliver.AndroidDeliver;

public class MainThreadActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, MainThreadActivity.class);
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_main);
        findViewById(R.id.tv_0).setOnClickListener(this);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_1_2).setOnClickListener(this);
        findViewById(R.id.tv_2_1).setOnClickListener(this);
        findViewById(R.id.tv_2_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_4_2).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6_1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_0) {
            startThread0();
        } else if (id == R.id.tv_1) {
            startThread1();
        } else if (id == R.id.tv_1_2) {
            startThread1_2();
        } else if (id == R.id.tv_2_1) {
            startThread2();
        } else if (id == R.id.tv_2_2) {
        } else if (id == R.id.tv_3) {
            startThread3();
        } else if (id == R.id.tv_4) {
            startThread4();
        } else if (id == R.id.tv_4_2) {
            startThread5();
        } else if (id == R.id.tv_5) {
            startActivity(new Intent(this, ExecutorsTestActivity.class));
        } else if (id == R.id.tv_6_1) {
            startActivity(new Intent(this, ThreadActivity.class));
        }
    }

    private void startThread0() {
        //可以自己创建
        PoolThread executor = PoolThread.ThreadBuilder.createSingle().build();
        executor.setName("最简单的线程调用方式");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式");
            }
        });
    }

    private void startThread1() {
        //也可以全局使用
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        executor.setName("最简单的线程调用方式");
        executor.setDeliver(new AndroidDeliver());
        executor.setCallback(new ThreadCallback() {
            @Override
            public void onError(String threadName, Throwable t) {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式 onError : " + t.getMessage());
            }

            @Override
            public void onCompleted(String threadName) {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式 onCompleted : " + threadName);
            }

            @Override
            public void onStart(String threadName) {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式 onStart : " + threadName);
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式");
            }
        });
    }

    private void startThread1_2() {
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        executor.setName("最简单的线程调用方式");
        executor.setDeliver(new AndroidDeliver());
        executor.setCallback(new ThreadCallback() {
            @Override
            public void onError(String threadName, Throwable t) {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式 onError : " + t.getMessage());
            }

            @Override
            public void onCompleted(String threadName) {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式 onCompleted : " + threadName);
            }

            @Override
            public void onStart(String threadName) {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式 onStart : " + threadName);
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式");
                String i = "13.4";
                Integer.parseInt(i);
            }
        });
    }

    private void startThread2() {
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        executor.setName("异步回调");
        executor.setDelay(2,TimeUnit.SECONDS);
        // 启动异步任务
        executor.async(new Callable<String>(){
            @Override
            public String call() throws Exception {
                Log.e("PoolThreadAsyncCallback","耗时操作");
                Thread.sleep(5000);
                // 做一些操作
                return null;
            }
        }, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String user) {
                Log.e("PoolThreadAsyncCallback","成功");
            }

            @Override
            public void onFailed(Throwable t) {
                Log.e("PoolThreadAsyncCallback","失败");
            }

            @Override
            public void onStart(String threadName) {
                Log.e("PoolThreadAsyncCallback","开始");
            }
        });
    }


    private void startThread3() {
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        executor.setName("延迟时间执行任务");
        executor.setDelay(2, TimeUnit.SECONDS);
        executor.setDeliver(new AndroidDeliver());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("PoolThreadMainActivity","延迟时间执行任务");
            }
        });
    }


    private void startThread4() {
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        //设置为当前的任务设置线程名
        executor.setName("延迟时间执行任务");
        //设置当前任务的延迟时间
        executor.setDelay(2, TimeUnit.SECONDS);
        //设置当前任务的线程传递
        executor.setDeliver(new AndroidDeliver());
        executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d("PoolThreadstartThread4","startThread4---call");
                Thread.sleep(2000);
                String str = "小杨逗比";
                return str;
            }
        });
        executor.setCallback(new ThreadCallback() {
            @Override
            public void onError(String threadName, Throwable t) {
                Log.d("PoolThreadstartThread4","startThread4---onError");
            }

            @Override
            public void onCompleted(String threadName) {
                Log.d("PoolThreadstartThread4","startThread4---onCompleted");
            }

            @Override
            public void onStart(String threadName) {
                Log.d("PoolThreadstartThread4","startThread4---onStart");
            }
        });
    }



    private void startThread5() {
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        //设置为当前的任务设置线程名
        executor.setName("延迟时间执行任务");
        //设置当前任务的延迟时间
        executor.setDelay(2, TimeUnit.SECONDS);
        //设置当前任务的线程传递
        executor.setDeliver(new AndroidDeliver());
        Future<String> submit = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d("PoolThreadstartThread5","startThread5---call");
                String str = "小杨逗比";
                return str;
            }
        });
        try {
            String result = submit.get();
            Log.d("PoolThreadstartThread5","startThread5-----"+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        executor.close();
    }
}
