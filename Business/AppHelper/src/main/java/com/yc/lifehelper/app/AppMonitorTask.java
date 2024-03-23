package com.yc.lifehelper.app;

import android.os.Looper;

import com.yc.parallel.AbsParallelTask;
import com.yc.ycthreadpoollib.easy.DelegateTaskExecutor;
import com.yc.netlib.utils.NetworkTool;
import com.yc.crash.lib.CrashHandler;
import com.yc.crash.lib.CrashListener;
import com.yc.toolutils.system.AppLogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class AppMonitorTask extends AbsParallelTask {
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        //崩溃库
        initCrash();
        //网络拦截库
        initNetWork();
        try {
            Thread.sleep(200);
        }catch (Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        boolean isMainThread = (Looper.myLooper() == Looper.getMainLooper());
        AppLogUtils.i("app init 2 task monitor total time : " + (end-start)
                + " ; 线程是否是主线程" + isMainThread);
    }

    @Override
    public boolean isRunOnMainThread() {
        return true;
    }

    @Override
    public List<Class<? extends AbsParallelTask>> getDependsTaskList() {
        List<Class<? extends AbsParallelTask>> dependsTaskList = new ArrayList<>();
        dependsTaskList.add(AppCoreTask.class);
        return dependsTaskList;
    }

    @Override
    public Executor runOnExecutor() {
        return DelegateTaskExecutor.getInstance().getCpuThreadExecutor();
    }


    private void initCrash() {
        //ThreadHandler.getInstance().init(this);
        CrashHandler.getInstance()
                .setWriteLog(false)
                .init(MainApplication.getInstance(), new CrashListener() {
                    @Override
                    public void recordException(Throwable ex, int crashCount) {
                        AppLogUtils.e("record exception : " + ex.getMessage());
                        //自定义上传crash，支持开发者上传自己捕获的crash数据
                        //StatService.recordException(getApplication(), ex);
                    }
        });
    }


    private void initNetWork() {
        NetworkTool.getInstance().init(MainApplication.getInstance());
    }

}
