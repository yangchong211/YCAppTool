package com.yc.monitortimelib.monitor;

import java.util.HashMap;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : time监控器帮助类
 *     revise:
 * </pre>
 */
public final class MonitorTimeHelper {

    private static final Object OBJECT = new Object();
    private static final HashMap<String, MonitorTime> TIME_MONITOR_CACHE = new HashMap<>();
    private static MonitorFormatAdapter mPrintAdapter = MonitorFormatAdapter.Factory.newDefaultLogAdapter();
    private static boolean isMonitor = true;

    private MonitorTimeHelper() {

    }

    public static void init(boolean isMonitor, MonitorFormatAdapter adapter) {
        MonitorTimeHelper.isMonitor = isMonitor;
        if (adapter != null) {
            mPrintAdapter = adapter;
        }
    }

    public static void start(String actionName) {
        if (!isMonitor) {
            return;
        }
        synchronized (OBJECT) {
            MonitorTime timeMonitor = TIME_MONITOR_CACHE.get(actionName);
            if (timeMonitor == null) {
                timeMonitor = new MonitorTime(actionName);
            }
            timeMonitor.start(actionName);
            TIME_MONITOR_CACHE.put(actionName, timeMonitor);
        }
    }

    public static void end(String actionName) {
        if (!isMonitor) {
            return;
        }
        synchronized (OBJECT) {
            MonitorTime timeMonitor = TIME_MONITOR_CACHE.get(actionName);
            if (timeMonitor == null) {
                timeMonitor = new MonitorTime(actionName);
            }
            timeMonitor.end(actionName, rstListener);
            TIME_MONITOR_CACHE.put(actionName, timeMonitor);
        }
    }

    private static MonitorListener rstListener;

    public static void setOnMonitorListener(MonitorListener listener) {
        rstListener = listener;
    }

    static MonitorFormatAdapter getFormatAdapter() {
        return mPrintAdapter;
    }
}
