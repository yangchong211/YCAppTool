package com.yc.appprocesslib;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2018/04/15
 * @desc : 前后台监听
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
@Deprecated()
public class AppStateMonitor extends BaseLifecycleCallbacks implements ComponentCallbacks2 {

    public static final String TAG = "AppStateMonitor";
    private static final AppStateMonitor S_INSTANCE = new AppStateMonitor();
    private final ArrayList<OnStateListener> mStateListeners = new ArrayList<>();
    private final AtomicInteger mActiveActivitiesCount = new AtomicInteger(0);
    public static final int STATE_FOREGROUND = 1;
    public static final int STATE_BACKGROUND = 0;
    private int mState = STATE_BACKGROUND;
    private final Handler mHandler = BackgroundThread.get().getHandler();
    /**
     * 判断是否初始化
     */
    private static final AtomicBoolean M_INITIALIZED = new AtomicBoolean(false);
    private Runnable mInitialReportTask;
    protected boolean mIsDebug;

    public static AppStateMonitor getInstance() {
        //静态单例
        return S_INSTANCE;
    }

    public void init(Context context) {
        if (M_INITIALIZED.get()) {
            return;
        }
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
        sendDelayedMessage();
        M_INITIALIZED.set(true);
    }

    public void setDebug(boolean debug){
        mIsDebug = debug;
    }

    private void sendDelayedMessage() {
        mInitialReportTask = new Runnable() {
            @Override
            public void run() {
                if (mActiveActivitiesCount.get() == 0) {
                    onStateChanged(STATE_BACKGROUND);
                }
            }
        };
        mHandler.postDelayed(mInitialReportTask, 15 * 1000);
    }

    /**
     * 获取状态
     * 前台切换到后台：onActivityPaused ---> onActivityStopped
     * 后台切换到前台：onActivityStarted ---> onActivityResumed
     *
     * @return state状态
     */
    public int getState() {
        return mState;
    }

    /**
     * 判断是否在前台
     *
     * @return 返回true表示前台
     */
    public synchronized boolean isInForeground() {
        return this.mState == STATE_FOREGROUND;
    }

    /**
     * 判断是否在后台
     *
     * @return 返回true表示后台
     */
    public synchronized boolean isInBackground() {
        return this.mState == STATE_BACKGROUND;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        super.onActivityStarted(activity);
        int old = mActiveActivitiesCount.getAndIncrement();
        if (old == 0) {
            onStateChanged(STATE_FOREGROUND);
        }

        if (mInitialReportTask != null) {
            mHandler.removeCallbacks(mInitialReportTask);
            mInitialReportTask = null;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        super.onActivityStopped(activity);
        int newState = mActiveActivitiesCount.decrementAndGet();
        if (newState == 0) {
            onStateChanged(STATE_BACKGROUND);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        String msg = TAG + "onTrimMemory: level:" + level;
        loggingAppState(msg);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        String msg = TAG + "onConfigurationChanged: " + newConfig;
        loggingAppState(msg);
    }

    @Override
    public void onLowMemory() {
        String msg = TAG + "onLowMemory: ";
        loggingAppState(msg);
    }

    private void onStateChanged(int newState) {
        mState = newState;
        if (newState == STATE_BACKGROUND) {
            loggingAppState("App into background");
            dispatchOnInBackground();
        }
        if (newState == STATE_FOREGROUND) {
            loggingAppState("App into foreground");
            dispatchOnInForeground();
        }
    }

    private void dispatchOnInBackground() {
        Object[] callbacks = collectStateListeners();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((OnStateListener) callback).onInBackground();
            }
        }
    }

    private void dispatchOnInForeground() {
        Object[] callbacks = collectStateListeners();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((OnStateListener) callback).onInForeground();
            }
        }
    }

    private Object[] collectStateListeners() {
        Object[] callbacks = null;
        synchronized (mStateListeners) {
            if (mStateListeners.size() > 0) {
                callbacks = mStateListeners.toArray();
            }
        }
        return callbacks;
    }

    public void registerStateListener(OnStateListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mStateListeners) {
            mStateListeners.add(listener);
        }
    }

    public void unregisterStateListener(OnStateListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mStateListeners) {
            mStateListeners.remove(listener);
        }
    }

    private void loggingAppState(String msg) {
        if (mIsDebug && msg != null) {
            Log.d("app state monitor : ", msg);
        }
    }

}
