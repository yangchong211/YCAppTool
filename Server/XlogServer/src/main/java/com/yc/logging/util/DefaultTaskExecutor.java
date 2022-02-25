package com.yc.logging.util;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;


@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DefaultTaskExecutor extends TaskExecutor {
    private final Object mLock = new Object();
    private ExecutorService mDiskIO = Executors.newCachedThreadPool(new ThreadFactory() {
        AtomicLong mCount = new AtomicLong(0);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "LoggerTask #" + mCount.getAndIncrement());
        }
    });

    @Nullable
    private volatile Handler mMainHandler;

    @Override
    public void executeOnDiskIO(Runnable runnable) {
        mDiskIO.execute(runnable);
    }

    @Override
    public void postToMainThread(Runnable runnable) {
        if (mMainHandler == null) {
            synchronized (mLock) {
                if (mMainHandler == null) {
                    mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        //noinspection ConstantConditions
        mMainHandler.post(runnable);
    }

    @Override
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
