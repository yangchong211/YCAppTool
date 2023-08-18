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

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2018/04/15
 * @desc : 前后台监听
 * @revise : 如果对前后台实时性要求不是那么严格，建议使用该类
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public class AppStateLifecycle extends BaseLifecycleCallbacks implements ComponentCallbacks2 {

    public static final String TAG = "AppStateMonitor";
    private static final AppStateLifecycle S_INSTANCE = new AppStateLifecycle();
    private final ArrayList<OnStateListener> mStateListeners = new ArrayList<>();
    /**
     * 表示前台
     */
    public static final int STATE_FOREGROUND = 1;
    /**
     * 表示后台
     */
    public static final int STATE_BACKGROUND = 0;
    private int mState = STATE_BACKGROUND;
    /**
     * 使用 HandlerThread + Handler
     */
    private final Handler mHandler = BackgroundThread.get().getHandler();
    /**
     * 判断是否初始化
     */
    private static final AtomicBoolean M_INITIALIZED = new AtomicBoolean(false);
    /**
     * 记录start次数
     */
    private int mStartedCounter = 0;
    /**
     * 记录resume次数
     */
    private int mResumedCounter = 0;
    /**
     * 是否发送pause事件
     */
    private boolean mPauseSent = true;
    /**
     * 是否执行stop方法
     */
    private boolean mStopSent = true;
    /**
     * 延迟500ms
     */
    private static final long TIMEOUT_MS = 500;
    protected boolean mIsDebug;

    private final Runnable mDelayedPauseRunnable = new Runnable() {
        @Override
        public void run() {
            dispatchPauseIfNeeded();
            dispatchStopIfNeeded();
        }
    };

    public static AppStateLifecycle getInstance() {
        //静态单例
        return S_INSTANCE;
    }

    public void init(Context context) {
        if (M_INITIALIZED.get()) {
            return;
        }
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
        M_INITIALIZED.set(true);
    }

    public void setDebug(boolean debug){
        mIsDebug = debug;
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
        //start调用+1
        mStartedCounter++;
        //start调用，并且之前stop是true
        if (mStartedCounter == 1 && mStopSent) {
            onStateChanged(STATE_FOREGROUND);
            //设置stop当前状态是false
            mStopSent = false;
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        super.onActivityStopped(activity);
        mStartedCounter--;
        dispatchStopIfNeeded();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        super.onActivityPaused(activity);
        mResumedCounter--;
        if (mResumedCounter == 0) {
            mHandler.postDelayed(mDelayedPauseRunnable, TIMEOUT_MS);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        super.onActivityResumed(activity);
        mResumedCounter++;
        if (mResumedCounter == 1) {
            if (mPauseSent) {
                mPauseSent = false;
            } else {
                mHandler.removeCallbacks(mDelayedPauseRunnable);
            }
        }
    }

    void dispatchPauseIfNeeded() {
        if (mResumedCounter == 0) {
            mPauseSent = true;
        }
    }

    void dispatchStopIfNeeded() {
        if (mStartedCounter == 0 && mPauseSent) {
            mStopSent = true;
            onStateChanged(STATE_BACKGROUND);
        }
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

    private void loggingAppState(String msg) {
        if (mIsDebug && msg != null) {
            Log.d("app state lifecycle : ", msg);
        }
    }

}
