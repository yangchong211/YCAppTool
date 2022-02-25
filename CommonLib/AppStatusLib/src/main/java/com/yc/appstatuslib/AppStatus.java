package com.yc.appstatuslib;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.SparseArray;
import com.yc.appstatuslib.listener.AppStatusListener;

import java.util.ArrayList;
import java.util.List;

public class AppStatus {

    private static final String TAG = "AppStatus";
    private final List<AppStatusListener> mAppStatusListener = new ArrayList<>();
    private final AppStatus.AppStatusLifecycleCallbacks mAppCallbacks =
            new AppStatus.AppStatusLifecycleCallbacks();
    private Application mApplication;
    private int mActivityStartedCount;
    private int mActivityResumedCount;
    private final AppStatusManager mResourceManager;

    AppStatus(AppStatusManager manager) {
        //直接传入对象
        mResourceManager = manager;
    }

    void registerAppStatusListener(AppStatusListener listener) {
        if (listener != null) {
            synchronized(mAppStatusListener) {
                mAppStatusListener.add(listener);
            }
        }
    }

    boolean unregisterAppStatusListener(AppStatusListener listener) {
        if (listener == null) {
            return false;
        } else {
            synchronized(mAppStatusListener) {
                return mAppStatusListener.remove(listener);
            }
        }
    }

    int getAppStatus() {
        return mActivityResumedCount;
    }

    public void destory() {
        mAppStatusListener.clear();
        mApplication.unregisterActivityLifecycleCallbacks(mAppCallbacks);
    }

    void init(Application application) {
        mApplication = application;
        application.registerActivityLifecycleCallbacks(mAppCallbacks);
    }

    private void dispatchAppOnFrontOrBack(boolean front) {
        if (mAppStatusListener != null && mAppStatusListener.size() != 0) {
            Object[] listeners = mAppStatusListener.toArray();
            int index;
            if (front) {
                for(index = 0; index < listeners.length; ++index) {
                    ((AppStatusListener)listeners[index]).appOnFrontOrBackChange(false);
                    mResourceManager.dispatcherThreadInfo();
                }
            } else {
                for(index = 0; index < listeners.length; ++index) {
                    ((AppStatusListener)listeners[index]).appOnFrontOrBackChange(true);
                    mResourceManager.dispatcherThreadInfo();
                }
            }
        }
    }

    private class AppStatusLifecycleCallbacks implements ActivityLifecycleCallbacks {

        private final SparseArray<Integer> mResumedActivities = new SparseArray<>();

        AppStatusLifecycleCallbacks() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
            if (activity != null) {
                int hash = activity.hashCode();
                if (mResumedActivities.indexOfKey(hash) > 0) {
                    return;
                }

                mResumedActivities.append(hash, 0);
            }
            mActivityResumedCount++;
            if (mActivityResumedCount == 1) {
                dispatchAppOnFrontOrBack(true);
                mResourceManager.collection();
            }
        }

        public void onActivityStopped(Activity activity) {
            if (activity != null) {
                int hash = activity.hashCode();
                if (mResumedActivities.indexOfKey(hash) > 0) {
                    mResumedActivities.remove(hash);
                }
            }

            mActivityResumedCount--;
            if (mActivityResumedCount == 0) {
                dispatchAppOnFrontOrBack(false);
                mResourceManager.collection();
            }

        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }
    }
}
