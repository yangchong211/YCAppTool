package com.yc.taskscheduler;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class SchedulerTask implements Runnable {

    protected long startDelayMillisecond;
    protected long periodMillisecond;
    protected boolean mainThread = true;
    protected AtomicBoolean canceled = new AtomicBoolean(false);

    protected SchedulerTask(long periodMillisecond) {
        this.periodMillisecond = periodMillisecond;
    }

    protected SchedulerTask(long periodMillisecond,boolean mainThread) {
        this.periodMillisecond = periodMillisecond;
        this.mainThread = mainThread;
    }

    protected SchedulerTask(long periodMillisecond,boolean mainThread,long startDelayMillisecond) {
        this.periodMillisecond = periodMillisecond;
        this.mainThread = mainThread;
        this.startDelayMillisecond = startDelayMillisecond;
    }

    public abstract void onSchedule();

    @Override
    public void run() {
        if(!canceled.get()) {
            onSchedule();
        }
    }

}
