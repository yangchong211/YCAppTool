package com.yc.catonhelperlib.fps;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.os.MessageQueue.IdleHandler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

public abstract class BaseFloatPage {

    private static final String TAG = "BaseFloatPage";
    private View mRootView;
    private LayoutParams mLayoutParams;
    private Handler mHandler;
    private BaseFloatPage.InnerReceiver mInnerReceiver = new BaseFloatPage.InnerReceiver();
    private String mTag;
    private Bundle mBundle;

    public BaseFloatPage() {
    }

    public void performCreate(Context context) {
        this.mHandler = new Handler(Looper.myLooper());
        this.onCreate(context);
        this.mRootView = new FrameLayout(context) {
            public boolean dispatchKeyEvent(KeyEvent event) {
                return event.getAction() != 1 || event.getKeyCode() != 4 && event.getKeyCode() != 3 ? super.dispatchKeyEvent(event) : BaseFloatPage.this.onBackPressed();
            }
        };
        View view = this.onCreateView(context, (ViewGroup)this.mRootView);
        ((ViewGroup)this.mRootView).addView(view);
        this.onViewCreated(this.mRootView);
        this.mLayoutParams = new LayoutParams();
        if (VERSION.SDK_INT >= 26) {
            this.mLayoutParams.type = 2038;
        } else {
            this.mLayoutParams.type = 2002;
        }

        this.mLayoutParams.format = -2;
        this.mLayoutParams.gravity = 51;
        this.onLayoutParamsCreated(this.mLayoutParams);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(this.mInnerReceiver, intentFilter);
    }

    public void performDestroy() {
        this.getContext().unregisterReceiver(this.mInnerReceiver);
        this.mHandler = null;
        this.mRootView = null;
        this.onDestroy();
    }

    public Context getContext() {
        return this.mRootView != null ? this.mRootView.getContext() : null;
    }

    public Resources getResources() {
        return this.getContext() == null ? null : this.getContext().getResources();
    }

    public String getString(@StringRes int resId) {
        return this.getContext() == null ? null : this.getContext().getString(resId);
    }

    protected void onViewCreated(View view) {
    }

    protected abstract View onCreateView(Context var1, ViewGroup var2);

    protected void onLayoutParamsCreated(LayoutParams params) {
    }

    protected void onCreate(Context context) {
    }

    protected void onDestroy() {
    }

    public boolean isShow() {
        return this.mRootView.isShown();
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return this.mRootView.findViewById(id);
    }

    public View getRootView() {
        return this.mRootView;
    }

    public LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    public void post(Runnable r) {
        this.mHandler.post(r);
    }

    public void postDelayed(Runnable r, long delayMillis) {
        this.mHandler.postDelayed(r, delayMillis);
    }

    public void runAfterRenderFinish(final Runnable runnable) {
        Looper.myQueue().addIdleHandler(new IdleHandler() {
            public boolean queueIdle() {
                if (runnable != null) {
                    runnable.run();
                }

                Looper.myQueue().removeIdleHandler(this);
                return false;
            }
        });
    }

    public void finish() {
        FloatPageManager.getInstance().remove(this);
    }

    public void onEnterBackground() {
    }

    public void onEnterForeground() {
    }

    public void onHomeKeyPress() {
    }

    public void onRecentAppKeyPress() {
    }

    protected boolean onBackPressed() {
        return false;
    }

    public void setBundle(Bundle bundle) {
        this.mBundle = bundle;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    private class InnerReceiver extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY;
        final String SYSTEM_DIALOG_REASON_RECENT_APPS;
        final String SYSTEM_DIALOG_REASON_HOME_KEY;

        private InnerReceiver() {
            this.SYSTEM_DIALOG_REASON_KEY = "reason";
            this.SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
            this.SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        BaseFloatPage.this.onHomeKeyPress();
                    } else if (reason.equals("recentapps")) {
                        BaseFloatPage.this.onRecentAppKeyPress();
                    }
                }
            }
        }
    }
}
