package com.yc.serialtasklib;

public abstract class Task implements DispatchRunnable {

    public Task() {

    }

    public abstract void onCancel();

    public String getCategory() {
        Class<? extends Task> aClass = this.getClass();
        return aClass.getName();
    }
}
