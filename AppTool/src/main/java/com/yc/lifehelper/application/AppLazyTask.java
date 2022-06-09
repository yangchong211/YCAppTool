package com.yc.lifehelper.application;

import android.os.Looper;

import com.yc.appstart.AppStartTask;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;
import java.util.List;

public class AppLazyTask extends AppStartTask {

    @Override
    public void run() {
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
    }

    @Override
    public boolean isRunOnMainThread() {
        return false;
    }

    @Override
    public List<Class<? extends AppStartTask>> getDependsTaskList() {
        List<Class<? extends AppStartTask>> dependsTaskList = new ArrayList<>();
        dependsTaskList.add(AppDelayTask.class);
        return dependsTaskList;
    }
}
