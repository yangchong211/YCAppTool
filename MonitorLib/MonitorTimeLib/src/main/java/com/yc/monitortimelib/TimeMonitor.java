package com.yc.monitortimelib;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class TimeMonitor {

    private final String TAG = "TimeMonitor: ";
    private final ArrayList<TimeTraceBean> mTimeTicks = new ArrayList<>();
    private final String mProcessName;
    private long mStartTime;
    private boolean isEnd = true;

    TimeMonitor(String processName) {
        mProcessName = processName;
    }

    void start() {
        isEnd = false;
        synchronized (mTimeTicks) {
            mTimeTicks.clear();
        }
        mStartTime = System.currentTimeMillis();
    }

    void start(String actionName) {
        start();
        recoding(actionName);
    }

    void recoding(String actionName) {
        if (actionName == null || actionName.isEmpty()) {
            return;
        }
        synchronized (mTimeTicks) {
            long time = System.currentTimeMillis() - mStartTime;
            if (isEnd) {
                isEnd = false;
                mStartTime = time;
            }
            mTimeTicks.add(new TimeTraceBean(actionName, time));
        }
    }


    void end(String actionName, OnMonitorListener listener) {
        recoding(actionName);
        end(listener);
    }

    void end(OnMonitorListener listener) {
        isEnd = true;
        debug(listener);
    }

    void debug(OnMonitorListener listener) {
        if (mTimeTicks.isEmpty()) {
            AppLogUtils.w(TAG, "mTimeTag is empty!");
            return;
        }

        if (listener != null) {
            PrintFormatAdapter formatAdapter = TimeMonitorHelper.getFormatAdapter();
            String format = formatAdapter.onFormat(mProcessName, mStartTime, timeTicks2LinkedHashMap());
            listener.onMonitorResult(mProcessName, format);
            return;
        }
        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                synchronized (mTimeTicks) {
                    Iterator<TimeTraceBean> iterator = mTimeTicks.iterator();
                    AppLogUtils.d(TAG, mProcessName + ": [ ");
                    while (iterator.hasNext()) {
                        TimeTraceBean tag = (TimeTraceBean) iterator.next();
                        AppLogUtils.d(TAG, mProcessName + " " + tag.toString());
                    }
                    AppLogUtils.d(TAG, " ] " + mProcessName);
                }
            }
        });
    }

    LinkedHashMap<String, Long> timeTicks2LinkedHashMap() {
        int count = mTimeTicks.size();
        LinkedHashMap<String, Long> map = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            map.put(mTimeTicks.get(i).getActionName(), mTimeTicks.get(i).getActionTime());
        }
        return map;
    }

}
