package com.yc.taskscheduler;


import android.os.Handler;

import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;


public class LifecycleRunnable implements Runnable {

    private Runnable mOriginRunnable;
    private LifecycleOwner mLifecycleOwner;
    private GenericLifecycleObserver mLifecycleObserver;


    LifecycleRunnable(LifecycleOwner lifecycleOwner, final Handler handler,
                      final Lifecycle.Event targetEvent,
                      final Runnable originRunnable) {
        if(originRunnable == null || lifecycleOwner == null) {
            return;
        }
        this.mLifecycleOwner = lifecycleOwner;
        this.mOriginRunnable = originRunnable;
        mLifecycleObserver = new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {

                if(event == targetEvent) {
                    if(mLifecycleOwner!=null ) {
                        mLifecycleOwner.getLifecycle().removeObserver(this);
                    }
                    handler.removeCallbacks(LifecycleRunnable.this);
                }
            }
        };
        if(TaskScheduler.isMainThread()) {
            mLifecycleOwner.getLifecycle().addObserver(mLifecycleObserver);
        }else {
            TaskScheduler.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mLifecycleOwner.getLifecycle().addObserver(mLifecycleObserver);
                }
            });
        }

    }


    @Override
    public void run() {
        if(mOriginRunnable!=null && mLifecycleOwner!=null) {
            mOriginRunnable.run();
            mLifecycleOwner.getLifecycle().removeObserver(mLifecycleObserver);
        }

    }
}
