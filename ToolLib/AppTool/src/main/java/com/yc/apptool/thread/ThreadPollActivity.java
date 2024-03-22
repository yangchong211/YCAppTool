package com.yc.apptool.thread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.apploglib.AppLogHelper;
import com.yc.apptool.R;
import com.yc.apptool.App;
import com.yc.toastutils.ToastUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import cn.ycbjie.ycthreadpoollib.callback.AsyncCallback;
import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback;
import cn.ycbjie.ycthreadpoollib.deliver.AndroidDeliver;


public class ThreadPollActivity extends AppCompatActivity implements View.OnClickListener {

    private PoolThread executor;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, ThreadPollActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_thread);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2_1).setOnClickListener(this);
        findViewById(R.id.tv_2_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_4_2).setOnClickListener(this);


        // 计算可使用的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 取四分之一的可用内存作为缓存
        final int cacheSize = maxMemory / 4;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {
            startThread1();
        } else if (id == R.id.tv_2_1) {
            startThread2();
        } else if (id == R.id.tv_2_2) {
            if (executor != null) {
                executor.stop();
            } else {
                ToastUtils.showRoundRectToast("请先开启异步耗时操作");
            }
        } else if (id == R.id.tv_3) {
            startThread3();
        } else if (id == R.id.tv_4) {
            startThread4();
        } else if (id == R.id.tv_4_2) {
            startThread5();
        }
    }

    private void startThread1() {
        PoolThread executor = App.getInstance().getExecutor();
        executor.setName("最简单的线程调用方式");
        executor.setDeliver(new AndroidDeliver());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("PoolThread: ","最简单的线程调用方式");
            }
        });
    }


    private void startThread2() {
        executor = PoolThread.ThreadBuilder.createSingle().build();
        executor.setName("异步回调");
        executor.setDelay(2,TimeUnit.SECONDS);
        // 启动异步任务
        executor.async(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                AppLogHelper.d("PoolThread: ","耗时操作");
                Thread.sleep(5000);
                // 做一些操作
                return true;
            }
        }, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isLogin) {
                AppLogHelper.d("PoolThread: ","成功" + isLogin);
                ToastUtils.showRoundRectToast("异步成功");
            }

            @Override
            public void onFailed(Throwable t) {
                AppLogHelper.d("PoolThread: ","失败"+t.getMessage());
                ToastUtils.showRoundRectToast("异步失败");
            }

            @Override
            public void onStart(String threadName) {
                AppLogHelper.d("PoolThread: ","开始");
                ToastUtils.showRoundRectToast("异步开始");
            }
        });
        executor.setCallback(new ThreadCallback() {
            @Override
            public void onError(String threadName, Throwable t) {
                AppLogHelper.d("PoolThread: ","异常：" + t.getMessage());
            }

            @Override
            public void onCompleted(String threadName) {
                AppLogHelper.d("PoolThread: ","成功onCompleted");
            }

            @Override
            public void onStart(String threadName) {
                AppLogHelper.d("PoolThread: ","开始onStart");
            }
        });
    }


    private void startThread3() {
        PoolThread executor = App.getInstance().getExecutor();
        executor.setName("延迟时间执行任务");
        executor.setDelay(2, TimeUnit.SECONDS);
        executor.setDeliver(new AndroidDeliver());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("PoolThread: ","延迟时间执行任务");
            }
        });
    }


    private void startThread4() {
        PoolThread executor = App.getInstance().getExecutor();
        //设置为当前的任务设置线程名
        executor.setName("延迟时间执行任务");
        //设置当前任务的延迟时间
        executor.setDelay(2, TimeUnit.SECONDS);
        //设置当前任务的线程传递
        executor.setDeliver(new AndroidDeliver());
        executor.setCallback(new ThreadCallback() {
            @Override
            public void onError(String threadName, Throwable t) {
                AppLogHelper.d("PoolThread: ","startThread4---onError");
            }

            @Override
            public void onCompleted(String threadName) {
                AppLogHelper.d("PoolThread: ","startThread4---onCompleted");
            }

            @Override
            public void onStart(String threadName) {
                AppLogHelper.d("PoolThread: ","startThread4---onStart");
            }
        });
        executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppLogHelper.d("PoolThread: ","startThread4---call");
                Thread.sleep(2000);
                String str = "小杨逗比";
                return str;
            }
        });
    }



    private void startThread5() {
        PoolThread executor = App.getInstance().getExecutor();
        //设置为当前的任务设置线程名
        executor.setName("延迟时间执行任务");
        //设置当前任务的延迟时间
        executor.setDelay(2, TimeUnit.SECONDS);
        //设置当前任务的线程传递
        executor.setDeliver(new AndroidDeliver());
        Future<String> submit = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                AppLogHelper.d("PoolThread: ","startThread5---call");
                Thread.sleep(2000);
                String str = "小杨逗比";
                return str;
            }
        });
        try {
            String result = submit.get();
            AppLogHelper.d("PoolThread: ","startThread5-----"+result);
            ToastUtils.showRoundRectToast(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
