package com.yc.appstatuslib.info;

import java.util.List;

/**
 * <pre>
 *     @author: yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : 线程信息
 *     revise :
 * </pre>
 */
public final class AppThreadInfo {

    private int threadCount;
    private List<Thread> runningThreadCount;
    private List<Thread> blockThreadCount;
    private List<Thread> timeWaitingThreadCount;
    private List<Thread> waitingThreadCount;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public List<Thread> getRunningThreadCount() {
        return runningThreadCount;
    }

    public void setRunningThreadCount(List<Thread> runningThreadCount) {
        this.runningThreadCount = runningThreadCount;
    }

    public List<Thread> getBlockThreadCount() {
        return blockThreadCount;
    }

    public void setBlockThreadCount(List<Thread> blockThreadCount) {
        this.blockThreadCount = blockThreadCount;
    }

    public List<Thread> getTimeWaitingThreadCount() {
        return timeWaitingThreadCount;
    }

    public void setTimeWaitingThreadCount(List<Thread> timeWaitingThreadCount) {
        this.timeWaitingThreadCount = timeWaitingThreadCount;
    }

    public List<Thread> getWaitingThreadCount() {
        return waitingThreadCount;
    }

    public void setWaitingThreadCount(List<Thread> waitingThreadCount) {
        this.waitingThreadCount = waitingThreadCount;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "threadCount=" + threadCount +
                ", runningThreadCount=" + runningThreadCount +
                ", blockThreadCount=" + blockThreadCount +
                ", timeWaitingThreadCount=" + timeWaitingThreadCount +
                ", waitingThreadCount=" + waitingThreadCount +
                '}';
    }
}
