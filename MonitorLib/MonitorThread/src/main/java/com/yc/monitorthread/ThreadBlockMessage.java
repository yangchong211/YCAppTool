package com.yc.monitorthread;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ThreadBlockMessage extends Error {

    private ThreadBlockMessage(RecordStackData.Cause st) {
        super("Thread is blocked", st);
    }

    @Override
    public Throwable fillInStackTrace() {
        setStackTrace(new StackTraceElement[]{});
        return this;
    }

    public static ThreadBlockMessage New(String prefix, boolean logThreadsWithoutStackTrace) {

        final Map<Thread, StackTraceElement[]> stackTraces = new TreeMap<Thread, StackTraceElement[]>(new Comparator<Thread>() {
            @Override
            public int compare(Thread lhs, Thread rhs) {
                if (lhs == rhs)
                    return 0;
                return rhs.getName().compareTo(lhs.getName());
            }
        });

        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            if ((entry.getKey().getName().startsWith(prefix) && (logThreadsWithoutStackTrace || entry.getValue().length > 0))) {
                stackTraces.put(entry.getKey(), entry.getValue());
            }
        }

        RecordStackData.Cause tst = null;
        for (Map.Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet()) {
            tst = new RecordStackData(getThreadTitle(entry.getKey()), entry.getValue()).new Cause(tst);
        }

        return new ThreadBlockMessage(tst);
    }

    private static String getThreadTitle(Thread thread) {
        return thread.getName() + " (state = " + thread.getState() + ")";
    }
}