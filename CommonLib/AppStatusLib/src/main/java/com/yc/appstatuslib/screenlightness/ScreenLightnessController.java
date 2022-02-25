package com.yc.appstatuslib.screenlightness;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;


public class ScreenLightnessController {

    public interface DimScreenSaver {
        boolean isScreenNeverDim();

        int getScreenMinLightness();

        int getDimDelay();
    }

    private static final String TAG = "ScreenLightnessControll";
    private static final int CHANGE_BRIGHTNESS = 1;
    private float mCurrentScreenBrightness = -120.0F;
    private DimScreenSaver mDimScreenSaver;
    private Activity mActivity;
    private Handler mScreenLightHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 1:
                    ScreenLightnessController.this.setLight((float)msg.arg1, true);
                default:
            }
        }
    };
    private int mDimDelayMillis;

    public ScreenLightnessController(@NonNull Activity activity, @NonNull DimScreenSaver dimScreenSaver) {
        this.mDimScreenSaver = dimScreenSaver;
        this.mActivity = activity;
    }

    public void onPause() {
        if (this.mScreenLightHandler != null) {
            this.mScreenLightHandler.removeMessages(1);
        }

    }

    public void onDestroy() {
        if (this.mScreenLightHandler != null) {
            this.mScreenLightHandler.removeMessages(1);
            this.mScreenLightHandler = null;
        }

    }

    public void lightUp(boolean autoDim) {
        this.setLight(-100.0F, autoDim);
    }

    protected void setLight(float screenBrightness, boolean autoDim) {
        try {
            if (this.mScreenLightHandler == null) {
                return;
            }

            this.mScreenLightHandler.removeMessages(1);
            if (autoDim && (screenBrightness != 0.0F || this.mDimDelayMillis != this.mDimScreenSaver.getDimDelay())) {
                this.mDimDelayMillis = this.mDimScreenSaver.getDimDelay();
                if (this.mDimDelayMillis > 0 && !this.mDimScreenSaver.isScreenNeverDim()) {
                    Message mDimScreenMessage = this.mScreenLightHandler.obtainMessage(1, this.mDimScreenSaver.getScreenMinLightness(), 0);
                    this.mScreenLightHandler.sendMessageDelayed(mDimScreenMessage, (long)this.mDimDelayMillis);
                }
            }

            if (this.mCurrentScreenBrightness == screenBrightness) {
                return;
            }

            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return;
            }

            if (VERSION.SDK_INT >= 17 && this.mActivity.isDestroyed()) {
                return;
            }

            LayoutParams lp = this.mActivity.getWindow().getAttributes();
            float screenBrightnessFloat = screenBrightness / 100.0F;
            lp.screenBrightness = screenBrightnessFloat;
            this.mActivity.getWindow().setAttributes(lp);
            this.mCurrentScreenBrightness = screenBrightness;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public void dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == 0) {
            this.lightUp(true);
        }

    }

    public void dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            this.lightUp(true);
        }

    }
}

