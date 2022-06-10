package com.yc.anrtoollib.watch;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ANRStackTrace implements Serializable {

    public final String name;
    public final StackTraceElement[] stackTrace;

    public class MyThread extends Throwable {

        public MyThread(ANRStackTrace.MyThread other) {
            super(name, other);
        }

        @Override
        @NonNull
        public Throwable fillInStackTrace() {
            setStackTrace(stackTrace);
            return this;
        }
    }

    public ANRStackTrace(String name, StackTraceElement[] stackTrace) {
        this.name = name;
        this.stackTrace = stackTrace;
    }
}
