package com.yc.anrtoollib.watch;

import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class ANRError extends Error {

    private static final long serialVersionUID = 1L;
    public final long duration;

    private ANRError(ANRStackTrace.MyThread st, long duration) {
        super("Application Not Responding for at least " + duration + " ms.", st);
        this.duration = duration;
    }

    @Override
    @NonNull
    public Throwable fillInStackTrace() {
        setStackTrace(new StackTraceElement[] {});
        return this;
    }

    @NonNull
    public static ANRError New(long duration, @Nullable String prefix, boolean logThreadsWithoutStackTrace) {
        Looper mainLooper = Looper.getMainLooper();
        final Thread mainThread = mainLooper.getThread();

        final Map<Thread, StackTraceElement[]> stackTraces = new TreeMap<Thread, StackTraceElement[]>(new Comparator<Thread>() {
            @Override
            public int compare(Thread lhs, Thread rhs) {
                if (lhs == rhs) {
                    return 0;
                }
                if (lhs == mainThread) {
                    return 1;
                }
                if (rhs == mainThread) {
                    return -1;
                }
                return rhs.getName().compareTo(lhs.getName());
            }
        });
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        Set<Map.Entry<Thread, StackTraceElement[]>> entries = allStackTraces.entrySet();
        for (Map.Entry<Thread, StackTraceElement[]> entry : entries){
            Thread key = entry.getKey();
            StackTraceElement[] value = entry.getValue();
            if (key == mainThread ||  (key.getName().startsWith(prefix)
                    &&  (logThreadsWithoutStackTrace || value.length > 0))){
                stackTraces.put(key, value);
            }
        }

        // Sometimes main is not returned in getAllStackTraces() - ensure that we list it
        if (!stackTraces.containsKey(mainThread)) {
            stackTraces.put(mainThread, mainThread.getStackTrace());
        }

        ANRStackTrace.MyThread tst = null;
        for (Map.Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet()){
            Thread key = entry.getKey();
            StackTraceElement[] value = entry.getValue();
            //获取线程名称
            String threadTitle = getThreadTitle(key);
            //创建包装类
            ANRStackTrace anrStackTrace = new ANRStackTrace(threadTitle, value);
            tst = anrStackTrace.new MyThread(tst);
        }
        return new ANRError(tst, duration);
    }

    @NonNull
    public static ANRError NewMainOnly(long duration) {
        //通过mainLooper拿到主线程
        final Thread mainThread = Looper.getMainLooper().getThread();
        //通过主线程拿到现场的堆栈信息
        final StackTraceElement[] mainStackTrace = mainThread.getStackTrace();
        //获取线程名称
        String threadTitle = getThreadTitle(mainThread);
        //创建包装类
        ANRStackTrace threadStackTrace = new ANRStackTrace(threadTitle, mainStackTrace);
        //构建thread对象
        ANRStackTrace.MyThread thread = threadStackTrace.new MyThread(null);
        //最后返回构造好的ANRError实例
        return new ANRError(thread, duration);
    }

    private static String getThreadTitle(Thread thread) {
        return thread.getName() + " (state = " + thread.getState() + ")";
    }
}