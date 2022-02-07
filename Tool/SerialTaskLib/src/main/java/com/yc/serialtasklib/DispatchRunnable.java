package com.yc.serialtasklib;


import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

public interface DispatchRunnable {

    @WorkerThread
    void onWorkThread();

    @MainThread
    void onMainThread();

}
