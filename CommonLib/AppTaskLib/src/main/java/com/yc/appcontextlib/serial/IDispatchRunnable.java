package com.yc.appcontextlib.serial;


import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

public interface IDispatchRunnable {

    /**
     * 工作线程
     */
    @WorkerThread
    void onWorkThread();

    /**
     * 主线程
     */
    @MainThread
    void onMainThread();

}
