package com.yc.toolutils;

import androidx.annotation.NonNull;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 异常上报帮助类
 * revise:
 */
public abstract class ExceptionReporter {

    protected abstract void reportCrash(Throwable throwable);

    protected abstract void reportCrash(String tag, Throwable throwable);

    private static ExceptionReporter sExceptionReporter;

    public static void setExceptionReporter(@NonNull ExceptionReporter exceptionReporter) {
        sExceptionReporter = exceptionReporter;
    }

    public static void report(Throwable throwable) {
        if (sExceptionReporter != null) {
            sExceptionReporter.reportCrash(throwable);
        }
    }

    public static void report(String tag, Throwable throwable) {
        if (sExceptionReporter != null) {
            sExceptionReporter.reportCrash(tag, throwable);
        }
    }

}

