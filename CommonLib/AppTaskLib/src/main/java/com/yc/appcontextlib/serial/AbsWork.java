package com.yc.appcontextlib.serial;

public abstract class AbsWork implements IDispatchRunnable {

    volatile boolean cancel;
    volatile boolean finished;

    AbsWork() {

    }

    abstract void onCancel();

    abstract String getCategory();
}
