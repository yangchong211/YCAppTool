package com.yc.anrtoollib.watch;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ANRStackTrace implements Serializable {

    public final String _name;
    public final StackTraceElement[] _stackTrace;

    public class MyThread extends Throwable {

        public MyThread(ANRStackTrace.MyThread other) {
            super(_name, other);
        }

        @Override
        @NonNull
        public Throwable fillInStackTrace() {
            setStackTrace(_stackTrace);
            return this;
        }
    }

    public ANRStackTrace(String name, StackTraceElement[] stackTrace) {
        _name = name;
        _stackTrace = stackTrace;
    }
}
