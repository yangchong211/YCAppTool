package com.yc.appcontextlib.parallel;

import android.os.Process;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2019/03/15
 * @desc : task自定义runnable
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public class TaskRunnable implements Runnable {

    private final AbsParallelTask appStartTask;
    private final ParallelTaskDispatcher appStartTaskDispatcher;

    public TaskRunnable(AbsParallelTask appStartTask, ParallelTaskDispatcher appStartTaskDispatcher) {
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
