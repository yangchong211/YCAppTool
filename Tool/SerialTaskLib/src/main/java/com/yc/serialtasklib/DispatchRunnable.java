package com.yc.serialtasklib;


import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

public interface DispatchRunnable {

    @WorkerThread
    void onWorkThread();

    @MainThread
    void onMainThread();

}
