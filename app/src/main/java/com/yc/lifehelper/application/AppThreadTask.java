package com.yc.lifehelper.application;

import android.os.Looper;

import com.yc.appstart.AppStartTask;
import com.yc.appstart.TaskExecutorManager;
import com.yc.netlib.utils.NetworkTool;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toolutils.logger.AppLogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class AppThreadTask extends AppStartTask {
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        boolean isMainThread = (Looper.myLooper() == Looper.getMainLooper());
        AppLogUtils.i("app init 5 task thread total time : " + (end-start)
                + " ; 线程是否是主线程" + isMainThread);
    }

    @Override
    public boolean isRunOnMainThread() {
        //子线程
        return true;
    }

    @Override
    public List<Class<? extends AppStartTask>> getDependsTaskList() {
        List<Class<? extends AppStartTask>> dependsTaskList = new ArrayList<>();
        dependsTaskList.add(AppDelayTask.class);
        return dependsTaskList;
    }

}
