package com.yc.apptool.thread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.apploglib.AppLogHelper;
import com.yc.apptool.R;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toastutils.ToastUtils;


public class EasyExecutorActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, EasyExecutorActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_executor);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1){
            test1();
        } else if (id == R.id.tv_2){
            test2();
        } else if (id == R.id.tv_3){
            test3();
        } else if (id == R.id.tv_4){
            test4();
        } else if (id == R.id.tv_5){
            test5();
        }
    }

    private void test1() {
        DelegateTaskExecutor.getInstance().executeOnCore(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("EasyExecutor: " , "核心任务的线程池，执行任务");
            }
        });
    }

    private void test2() {
        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("EasyExecutor: " , "IO 密集型任务的线程池，执行任务");
            }
        });
    }

    private void test3() {
        DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("EasyExecutor: " , "CPU 密集型任务的线程池，执行任务");
            }
        });
    }

    private void test4() {
        DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("EasyExecutor: " , "UI主线程共有handler对象，执行任务1");
                ToastUtils.showRoundRectToast("UI主线程共有handler对象");
            }
        });
        DelegateTaskExecutor.getInstance().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("EasyExecutor: " , "UI主线程共有handler对象，执行任务2");
                ToastUtils.showRoundRectToast("UI主线程共有handler对象");
            }
        });
    }

    private void test5() {
        DelegateTaskExecutor.getInstance().postIoHandler(new Runnable() {
            @Override
            public void run() {
                AppLogHelper.d("EasyExecutor: " , "配合HandlerThread使用的handler，执行任务");
            }
        });
    }
}
