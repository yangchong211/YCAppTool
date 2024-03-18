package com.yc.http.synthesis.lifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2021/03/08
 *    desc   : Service 生命周期管理基类
 */
public abstract class LifecycleService extends Service implements LifecycleOwner {

    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    @Override
    public void onCreate() {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        super.onStart(intent, startId);
    }

    /**
     * 为什么不在这个方法里面派发 Lifecycle 回调？因为它最后会调用 {@link #onStart(Intent, int)}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }
}