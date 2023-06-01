package com.yc.monitorthread;

import java.io.Serializable;

public class RecordStackData implements Serializable {
    private final String              name;
    private final StackTraceElement[] stackTrace;

    public class Cause extends Throwable {
        public Cause(Cause other) {
            super(name, other);
        }

        @Override
        public Throwable fillInStackTrace() {
            setStackTrace(stackTrace);
            return this;
        }
    }

    public RecordStackData(String name, StackTraceElement[] stackTrace) {
        this.name = name;
        this.stackTrace = stackTrace;
    }
}
