package com.yc.http.synthesis.lifecycle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/09/01
 *    desc   : 全局的生命周期策略
 */
public final class ApplicationLifecycle implements LifecycleOwner {

    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }
}