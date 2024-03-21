package com.yc.audiofocus.focus;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 监听耳机按键逻辑
 *     revise:
 * </pre>
 */
public final class HeadsetHookHelper {

    /**
     * 单位: 毫秒 ms
     */
    public static final int DEFAULT_CLICK_INTERVAL = 300;
    private final ClickCounter mClickCounter;

    /**
     * 使用默认的点击事件时间间隔 {@link #DEFAULT_CLICK_INTERVAL} 构造一个 HeadsetHookHelper 对象。
     * <p>
     * 默认的点击事件时间间隔为 300 毫秒。
     *
     * @param listener 点击事件监听器，不能为 null
     */
    public HeadsetHookHelper(@NonNull OnHeadsetHookClickListener listener) {
        this(DEFAULT_CLICK_INTERVAL, listener);
    }

    /**
     * 使用 {@code clickInterval} 参数指定的点击事件时间间隔构造一个 HeadsetHookHelper 对象。
     *
     * @param clickInterval 两次点击事件的最大时间间隔（单位：毫秒）
     * @param listener      点击事件监听器，不能为 null
     */
    public HeadsetHookHelper(int clickInterval, @NonNull OnHeadsetHookClickListener listener) {
        mClickCounter = new ClickCounter(clickInterval, listener);
    }

    /**
     * 处理媒体按钮事件。
     *
     * @param intent 包含媒体按钮事件的 Intent 对象
     * @return 返回 true 表示当前 HeadsetHookHelper 消耗了这个媒体按钮事件，否则会返回 false
     */
    public boolean handleMediaButton(Intent intent) {
        KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (keyEvent == null) {
            return false;
        }

        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK) {
            consumeMediaButtonEvent(keyEvent);
            return true;
        }

        mClickCounter.reset();

        return false;
    }

    private void consumeMediaButtonEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
            mClickCounter.putEvent();
        }
    }

    /**
     * 用于监听 Headset Hook 按钮的点击次数。
     */
    public interface OnHeadsetHookClickListener {
        /**
         * 当检测到 Headset Hook 按钮被单次或连续点击时，会调用该方法。
         *
         * @param clickCount Headset Hook 按钮被点击的次数
         */
        void onHeadsetHookClicked(int clickCount);
    }

    private static class ClickCounter {
        private final int mClickInterval;
        private int mCount;
        private Timer mTimer;
        private final OnHeadsetHookClickListener mHeadsetHookClickListener;
        private final Handler mMainHandler;

        ClickCounter(int clickInterval, OnHeadsetHookClickListener listener) {
            mClickInterval = clickInterval;
            mHeadsetHookClickListener = listener;
            mMainHandler = new Handler(Looper.getMainLooper());
        }

        void reset() {
            cancelTimer();
            mCount = 0;
        }

        void putEvent() {
            mCount += 1;
            startTimer();
        }

        private void startTimer() {
            cancelTimer();

            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    notifyClicked();
                }
            }, mClickInterval);
        }

        private void notifyClicked() {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHeadsetHookClickListener.onHeadsetHookClicked(mCount);
                    mCount = 0;
                }
            });
        }

        private void cancelTimer() {
            if (mTimer != null) {
                mTimer.cancel();
            }
        }
    }
}

