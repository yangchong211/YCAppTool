package com.yc.appcommoninter.report;

import androidx.annotation.NonNull;

import java.util.HashMap;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 事件上报帮助类
 * revise:
 */
public abstract class EventReporter {

    protected abstract void reportEvent(String eventName);

    protected abstract void reportEvent(String eventName, HashMap<String, String> eventMap);

    private static EventReporter sEventReporter;

    public static void setEventReporter(@NonNull EventReporter eventReporter) {
        sEventReporter = eventReporter;
    }

    public static void report(String eventName) {
        if (sEventReporter != null) {
            sEventReporter.reportEvent(eventName);
        }
    }

    public static void report(String eventName, HashMap<String, String> eventMap) {
        if (sEventReporter != null) {
            sEventReporter.reportEvent(eventName, eventMap);
        }
    }

}

