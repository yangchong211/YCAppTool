package com.yc.lifehelper.application;

import android.os.Looper;

import com.yc.appstart.AppStartTask;
import com.yc.netlib.utils.NetworkTool;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toolutils.logger.AppLogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class AppMonitorTask extends AppStartTask {
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
    public List<Class<? extends AppStartTask>> getDependsTaskList() {
        List<Class<? extends AppStartTask>> dependsTaskList = new ArrayList<>();
        dependsTaskList.add(AppCoreTask.class);
        return dependsTaskList;
    }

    @Override
    public Executor runOnExecutor() {
        return TaskExecutorManager.getInstance().getCPUThreadPoolExecutor();
    }



    private void initCrash() {
        //ThreadHandler.getInstance().init(this);
        CrashHandler.getInstance().init(MainApplication.getInstance(), new CrashListener() {
            /**
             * 重启app
             */
            @Override
            public void againStartApp() {
                System.out.println("崩溃重启----------againStartApp------");
                //CrashToolUtils.reStartApp1(MainApplication.this,2000);
                //CrashToolUtils.reStartApp2(App.this,2000, MainActivity.class);
                //CrashToolUtils.reStartApp3(App.this);
            }

            /**
             * 自定义上传crash，支持开发者上传自己捕获的crash数据
             * @param ex                        ex
             */
            @Override
            public void recordException(Throwable ex) {
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
