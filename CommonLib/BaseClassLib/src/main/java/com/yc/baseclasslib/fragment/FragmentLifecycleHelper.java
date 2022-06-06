package com.yc.baseclasslib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yc.baseclasslib.BuildConfig;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2019/5/11
 *     desc   : fragment生命周期管理
 *     revise :
 * </pre>
 */
public final class FragmentLifecycleHelper {

    private static final String TAG = "FragmentLifecycle: ";
    private volatile static FragmentLifecycleHelper INSTANCE;

    public static FragmentLifecycleHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (FragmentLifecycleHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FragmentLifecycleHelper();
                }
            }
        }
        return INSTANCE;
    }


    public void addFragmentLifecycle(Fragment fragment) {
        if (fragment != null && fragment.getFragmentManager() != null) {
            //recursive字段设置true，将自动为所有子fragmentManager注册这个回调
            fragment.getFragmentManager().registerFragmentLifecycleCallbacks(callbacks, true);
            info("add fragment lifecycle : " + fragment.getClass().getSimpleName());
        }
    }

    public void removeFragmentLifecycle(Fragment fragment) {
        if (fragment != null && fragment.getFragmentManager() != null) {
            fragment.getFragmentManager().unregisterFragmentLifecycleCallbacks(callbacks);
            info("remove fragment lifecycle : " + fragment.getClass().getSimpleName());
        }
    }

    private final FragmentManager.FragmentLifecycleCallbacks callbacks =
            new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f,
                                               @NonNull Context context) {
                    super.onFragmentAttached(fm, f, context);
                }

                @Override
                public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f,
                                              @Nullable Bundle savedInstanceState) {
                    super.onFragmentCreated(fm, f, savedInstanceState);
                    info("fragment lifecycle  onFragmentCreated : " + f.getClass().getSimpleName());
                }

                @Override
                public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    super.onFragmentStarted(fm, f);
                }

                @Override
                public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    super.onFragmentResumed(fm, f);
                }

                @Override
                public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    super.onFragmentPaused(fm, f);
                }

                @Override
                public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    super.onFragmentStopped(fm, f);
                }

                @Override
                public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                    info("fragment lifecycle  onFragmentDestroyed : " + f.getClass().getSimpleName());
                }

                @Override
                public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    super.onFragmentDetached(fm, f);
                }
            };


    private void info(String s) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, s);
        }
    }

}
