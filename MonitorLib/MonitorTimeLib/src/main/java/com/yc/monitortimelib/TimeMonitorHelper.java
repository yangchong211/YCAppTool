package com.yc.monitortimelib;

import java.util.HashMap;

public final class TimeMonitorHelper {

    private static final Object OBJECT = new Object();
    private static final HashMap<String, TimeMonitor> timeMonitorCache = new HashMap<>();
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

    public static void start(String actionName) {
        if (!isMonitor) {
            return;
        }
        synchronized (OBJECT) {
            TimeMonitor timeMonitor = timeMonitorCache.get(actionName);
            if (timeMonitor == null){
                timeMonitor = new TimeMonitor(actionName);
            }
            timeMonitor.start(actionName);
            timeMonitorCache.put(actionName, timeMonitor);
        }
    }

    public static void end(String actionName) {
        if (!isMonitor) {
            return;
        }
        synchronized (OBJECT) {
            TimeMonitor timeMonitor = timeMonitorCache.get(actionName);
            if (timeMonitor == null){
                timeMonitor = new TimeMonitor(actionName);
            }
            timeMonitor.end(actionName,rstListener);
            timeMonitorCache.put(actionName, timeMonitor);
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
