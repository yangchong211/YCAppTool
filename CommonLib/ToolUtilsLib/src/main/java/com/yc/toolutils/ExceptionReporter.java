package com.yc.toolutils;

import androidx.annotation.NonNull;

public abstract class ExceptionReporter {
    protected abstract void reportCrash(Throwable throwable);

    private static ExceptionReporter sExceptionReporter;

    public static void setExceptionReporter(@NonNull ExceptionReporter exceptionReporter) {
        sExceptionReporter = exceptionReporter;
    }

    public static void report(Throwable throwable) {
        if (sExceptionReporter != null) {
            sExceptionReporter.reportCrash(throwable);
        }
    }

}

