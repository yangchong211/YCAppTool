package com.yc.taskscheduler;

import android.os.Handler;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

public final class ThreadUtils {


    public static boolean isMainThread(Handler handler) {
        return Thread.currentThread()== handler.getLooper().getThread();
    }

}
