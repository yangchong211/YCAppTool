package com.yc.monitortimelib;

import com.yc.toolutils.AppLogUtils;

public class TimeMonitor {

    private final String TAG = "TimeMonitor: ";
    private final TimeTraceBean timeTraceBean;
    private final Object object = new Object();

    TimeMonitor(String actionName) {
        timeTraceBean = new TimeTraceBean(actionName);
    }

    void start(String actionName) {
        if (actionName == null || actionName.isEmpty()) {
            return;
        }
        long startTime = System.currentTimeMillis();
        synchronized (object) {
            timeTraceBean.setActionStartTime(startTime);
        }
    }

    void end(String actionName, OnMonitorListener listener) {
        if (actionName == null || actionName.isEmpty()) {
            return;
        }
        long endTime = System.currentTimeMillis();
        synchronized (object) {
            timeTraceBean.setActionEndTime(endTime);
        }
        debug(listener);
    }

    void debug(OnMonitorListener listener) {
        long monitorTime = timeTraceBean.getActionEndTime() - timeTraceBean.getActionStartTime();
        if (listener != null) {
            PrintFormatAdapter formatAdapter = TimeMonitorHelper.getFormatAdapter();
            String format = formatAdapter.onFormat(timeTraceBean.getActionName(), monitorTime);
            listener.onMonitorResult(timeTraceBean.getActionName(), format);
            return;
        }
        AppLogUtils.d(TAG, timeTraceBean.getActionName() + " " + monitorTime);
    }

}
