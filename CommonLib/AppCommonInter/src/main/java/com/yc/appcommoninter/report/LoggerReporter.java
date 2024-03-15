package com.yc.appcommoninter.report;

import androidx.annotation.NonNull;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 日志上报帮助类
 * revise:
 */
public abstract class LoggerReporter {

    protected abstract void reportLog(String log);

    protected abstract void reportLog(String log, String message);

    private static LoggerReporter sLoggerReporter;

    public static void setLogReporter(@NonNull LoggerReporter logReporter) {
        sLoggerReporter = logReporter;
    }

    public static void report(String log) {
        if (sLoggerReporter != null) {
            sLoggerReporter.reportLog(log);
        }
    }

    public static void report(String log, String message) {
        if (sLoggerReporter != null) {
            sLoggerReporter.reportLog(log, message);
        }
    }

}

