package com.yc.http.synthesis.lifecycle;

import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2021/11/22
 *    desc   : Fragment 生命周期管理基类
 */
@SuppressWarnings("deprecation")
public class LifecycleAppFragment extends Fragment implements LifecycleOwner {

    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        super.onStart();
    }

    @Override
    public void onResume() {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        super.onResume();
    }

    @Override
    public void onPause() {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }
}