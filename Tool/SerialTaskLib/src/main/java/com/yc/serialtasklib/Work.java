package com.yc.serialtasklib;

public abstract class Work implements DispatchRunnable {

    volatile boolean cancel;
    volatile boolean finished;

    Work() {

    }

    abstract void onCancel();

    abstract String getCategory();
}
