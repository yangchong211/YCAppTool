package com.yc.eventuploadlib;

import androidx.annotation.NonNull;

import java.util.HashMap;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 日志上报帮助类
 * revise:
 */
public abstract class LoggerReporter {

    protected abstract void reportLog(String eventName);

    protected abstract void reportLog(String eventName, String message);

    private static LoggerReporter sEventReporter;

    public static void setEventReporter(@NonNull LoggerReporter exceptionReporter) {
        sEventReporter = exceptionReporter;
    }

    public static void report(String eventName) {
        if (sEventReporter != null) {
            sEventReporter.reportLog(eventName);
        }
    }

    public static void report(String eventName, String message) {
        if (sEventReporter != null) {
            sEventReporter.reportLog(eventName, message);
        }
    }

}

