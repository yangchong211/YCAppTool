package com.yc.appstatuslib.utils;



import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ThreadManagerUtils {

    private static ThreadManagerUtils INSTANCE;

    public static ThreadManagerUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (ThreadManagerUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadManagerUtils();
                }
            }
        }
        return INSTANCE;
    }


    private ThreadManagerUtils() {

    }

    public int getThreadCount() {
        return Thread.getAllStackTraces().keySet().size();
    }

    public List<Thread> getRunningThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threads = (Thread[])threadSet.toArray(new Thread[threadSet.size()]);
        List<Thread> runnableThread = new ArrayList();
        Thread[] var4 = threads;
        int var5 = threads.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            Thread thread = var4[var6];
            if (thread.getState() == State.RUNNABLE) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }

    public List<Thread> getBlockThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threads = (Thread[])threadSet.toArray(new Thread[threadSet.size()]);
        List<Thread> runnableThread = new ArrayList();
        Thread[] var4 = threads;
        int var5 = threads.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            Thread thread = var4[var6];
            if (thread.getState() == State.BLOCKED) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }

    public List<Thread> getTimeWaitingThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threads = (Thread[])threadSet.toArray(new Thread[threadSet.size()]);
        List<Thread> runnableThread = new ArrayList();
        Thread[] var4 = threads;
        int var5 = threads.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            Thread thread = var4[var6];
            if (thread.getState() == State.TIMED_WAITING) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }

    public List<Thread> getWaitingThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threads = (Thread[])threadSet.toArray(new Thread[threadSet.size()]);
        List<Thread> runnableThread = new ArrayList();
        Thread[] var4 = threads;
        int var5 = threads.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            Thread thread = var4[var6];
            if (thread.getState() == State.WAITING) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }
}

