package com.yc.monitortimelib;

import java.util.HashMap;

public final class TimeMonitorHelper {

    private static final Object object = new Object();
    private static final HashMap<String, TimeMonitor> mTimeMonitorCache = new HashMap<>();
    private static PrintFormatAdapter mPrintAdapter = PrintFormatAdapter.Factory.newDefaultLogAdapter();
    private static boolean isMonitor = true;

    private TimeMonitorHelper() {

    }

    public static void init(boolean isMonitor, PrintFormatAdapter adapter) {
        TimeMonitorHelper.isMonitor = isMonitor;
        if (adapter != null) {
            mPrintAdapter = adapter;
        }
    }

    public static void start(String processName) {
        start(processName, null);
    }

    public static void start(String processName, String actionName) {
        if (!isMonitor) {
            return;
        }
        synchronized (object) {
            TimeMonitor timeMonitor = mTimeMonitorCache.get(processName);
            if (mTimeMonitorCache.containsKey(processName)) {
                if (timeMonitor != null) {
                    timeMonitor.start(actionName);
                }
                return;
            }
            mTimeMonitorCache.put(processName, new TimeMonitor(processName));
            if (timeMonitor != null) {
                timeMonitor.start(actionName);
            }
        }
    }

    public static void recoding(String processName, String actionName) {
        if (!isMonitor) {
            return;
        }
        TimeMonitor timeMonitor = mTimeMonitorCache.get(processName);
        if (mTimeMonitorCache.containsKey(processName)) {
            if (timeMonitor != null) {
                timeMonitor.start(actionName);
            }
            return;
        }
        mTimeMonitorCache.put(processName, new TimeMonitor(processName));
        if (timeMonitor != null) {
            timeMonitor.recoding(actionName);
        }
    }

    public static void end(String processName) {
        end(processName, null);
    }

    public static void end(String processName, String actionName) {
        if (!isMonitor) {
            return;
        }
        TimeMonitor timeMonitor = mTimeMonitorCache.get(processName);
        if (timeMonitor != null) {
            timeMonitor.end(actionName, rstListener);
        }
    }

    private static OnMonitorListener rstListener;

    public static void setOnMonitorListener(OnMonitorListener listener) {
        rstListener = listener;
    }

    static PrintFormatAdapter getFormatAdapter() {
        return mPrintAdapter;
    }
}
