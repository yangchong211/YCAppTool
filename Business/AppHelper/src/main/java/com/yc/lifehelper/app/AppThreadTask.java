package com.yc.lifehelper.app;

import android.os.Looper;

import com.yc.ycthreadpoollib.easy.DelegateTaskExecutor;
import com.yc.parallel.AbsParallelTask;
import com.yc.appprocesslib.AppAutoCloser;
import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.sntp.NtpGetTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppThreadTask extends AbsParallelTask {
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        //app 进入后台一定时间后执行退出或者重启操作，有助于释放内存，减少用户电量消耗
        initAutoCloser();
        initNtpTime();
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
    public List<Class<? extends AbsParallelTask>> getDependsTaskList() {
        List<Class<? extends AbsParallelTask>> dependsTaskList = new ArrayList<>();
        dependsTaskList.add(AppDelayTask.class);
        return dependsTaskList;
    }

    private void initAutoCloser() {
        AppAutoCloser appAutoCloser = AppAutoCloser.getInstance();
        appAutoCloser.init(MainApplication.getInstance());
        appAutoCloser.setTime(60);
        appAutoCloser.start();
    }

    private void initNtpTime() {
        DelegateTaskExecutor.getInstance().executeOnCore(() -> {
            try {
                NtpGetTime.build()
                        .withNtpHost("time.ustc.edu.cn")
                        .withSharedPreferencesCache(MainApplication.getInstance())
                        .withConnectionTimeout(30000)
                        .initialize();
            } catch (IOException e) {
                e.printStackTrace();
                AppLogUtils.e("something went wrong when trying to initialize NtpGetTime "+ e);
            }
        });
    }

}
