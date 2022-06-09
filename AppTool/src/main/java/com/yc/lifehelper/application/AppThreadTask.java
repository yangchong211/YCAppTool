package com.yc.lifehelper.application;

import android.os.Looper;

import com.yc.appstart.AppStartTask;
import com.yc.autocloserlib.AppAutoCloser;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;
import java.util.List;

public class AppThreadTask extends AppStartTask {
    @Override
    public void run() {
        //app 进入后台一定时间后执行退出或者重启操作，有助于释放内存，减少用户电量消耗
        initAutoCloser();
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

    private void initAutoCloser() {
        AppAutoCloser appAutoCloser = AppAutoCloser.getInstance();
        appAutoCloser.init(MainApplication.getInstance());
        appAutoCloser.setTime(60);
        appAutoCloser.start();
    }

}
