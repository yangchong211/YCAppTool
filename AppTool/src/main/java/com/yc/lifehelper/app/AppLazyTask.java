package com.yc.lifehelper.app;

import android.os.Looper;

import com.yc.monitortimelib.TimeMonitorHelper;
import com.yc.parallel.AbsParallelTask;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;
import java.util.List;

public class AppLazyTask extends AbsParallelTask {

    @Override
    public void run() {
        TimeMonitorHelper.start("AppLazyTask");
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        boolean isMainThread = (Looper.myLooper() == Looper.getMainLooper());
        AppLogUtils.i("app init 4 task lazy total time : " + (end-start)
                + " ; 线程是否是主线程" + isMainThread);
        TimeMonitorHelper.end("AppLazyTask");
    }

    @Override
    public boolean isRunOnMainThread() {
        return false;
    }

    @Override
    public List<Class<? extends AbsParallelTask>> getDependsTaskList() {
        List<Class<? extends AbsParallelTask>> dependsTaskList = new ArrayList<>();
        dependsTaskList.add(AppDelayTask.class);
        return dependsTaskList;
    }
}
