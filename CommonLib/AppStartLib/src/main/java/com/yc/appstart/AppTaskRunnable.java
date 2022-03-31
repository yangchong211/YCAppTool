package com.yc.appstart;

import android.os.Process;


public class AppTaskRunnable implements Runnable {
    private final AppStartTask appStartTask;
    private final AppTaskDispatcher appStartTaskDispatcher;

    public AppTaskRunnable(AppStartTask appStartTask, AppTaskDispatcher appStartTaskDispatcher) {
        this.appStartTask = appStartTask;
        this.appStartTaskDispatcher = appStartTaskDispatcher;
    }

    @Override
    public void run() {
        int priority = appStartTask.priority();
        Process.setThreadPriority(priority);
        appStartTask.waitToNotify();
        appStartTask.run();
        appStartTaskDispatcher.setNotifyChildren(appStartTask);
        appStartTaskDispatcher.markAppStartTaskFinish(appStartTask);
    }
}
