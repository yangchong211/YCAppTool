package com.yc.ycthreadpoollib.easy;


import android.os.Handler;

import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 具有生命周期的Runnable
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
public class LifecycleRunnable implements Runnable {

    private Runnable mOriginRunnable;
    private LifecycleOwner mLifecycleOwner;
    private GenericLifecycleObserver mLifecycleObserver;


    LifecycleRunnable(LifecycleOwner lifecycleOwner, final Handler handler,
                      final Lifecycle.Event targetEvent, final Runnable originRunnable) {
        if (originRunnable == null || lifecycleOwner == null) {
            return;
        }
        this.mLifecycleOwner = lifecycleOwner;
        this.mOriginRunnable = originRunnable;
        mLifecycleObserver = new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                if (event == targetEvent) {
                    if (mLifecycleOwner != null) {
                        mLifecycleOwner.getLifecycle().removeObserver(this);
                    }
                    handler.removeCallbacks(LifecycleRunnable.this);
                }
            }
        };
        if (DelegateTaskExecutor.getInstance().isMainThread()) {
            mLifecycleOwner.getLifecycle().addObserver(mLifecycleObserver);
        } else {
            DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                @Override
                public void run() {
                    mLifecycleOwner.getLifecycle().addObserver(mLifecycleObserver);
                }
            });
        }
    }


    @Override
    public void run() {
        if (mOriginRunnable != null && mLifecycleOwner != null) {
            mOriginRunnable.run();
            mLifecycleOwner.getLifecycle().removeObserver(mLifecycleObserver);
        }

    }
}
