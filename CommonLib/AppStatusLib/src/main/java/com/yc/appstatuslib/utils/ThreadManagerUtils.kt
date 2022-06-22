package com.yc.appstatuslib.utils;



import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : 线程工具类
 *     revise :
 * </pre>
 */
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
        List<Thread> runnableThread = new ArrayList<>();
        int var5 = threads.length;
        for(int i = 0; i < var5; ++i) {
            Thread thread = threads[i];
            if (thread.getState() == State.RUNNABLE) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }

    public List<Thread> getBlockThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threads = (Thread[])threadSet.toArray(new Thread[threadSet.size()]);
        List<Thread> runnableThread = new ArrayList<>();
        int length = threads.length;
        for(int i = 0; i < length; ++i) {
            Thread thread = threads[i];
            if (thread.getState() == State.BLOCKED) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }

    public List<Thread> getTimeWaitingThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threads = (Thread[])threadSet.toArray(new Thread[threadSet.size()]);
        List<Thread> runnableThread = new ArrayList<>();
        for(int i = 0; i < threads.length; ++i) {
            Thread thread = threads[i];
            if (thread.getState() == State.TIMED_WAITING) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }

    public List<Thread> getWaitingThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threads = (Thread[])threadSet.toArray(new Thread[threadSet.size()]);
        List<Thread> runnableThread = new ArrayList<>();
        for(int i = 0; i < threads.length; ++i) {
            Thread thread = threads[i];
            if (thread.getState() == State.WAITING) {
                runnableThread.add(thread);
            }
        }
        return runnableThread;
    }
}

