package com.yc.monitortimelib;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class TimeMonitor {

    private final String TAG = "TimeMonitor: ";
    private final String mProcessName;
    private final TimeTraceBean timeTraceBean;
    private final Object object = new Object();

    TimeMonitor(String processName) {
        mProcessName = processName;
        timeTraceBean = new TimeTraceBean(mProcessName);
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
            String format = formatAdapter.onFormat(mProcessName, monitorTime);
            listener.onMonitorResult(mProcessName, format);
            return;
        }
        AppLogUtils.d(TAG, mProcessName + " " + monitorTime);
    }

}
