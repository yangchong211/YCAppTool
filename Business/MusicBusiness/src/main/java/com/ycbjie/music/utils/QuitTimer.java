package com.ycbjie.music.utils;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.ycbjie.music.inter.EventCallback;
import com.ycbjie.music.service.PlayService;


/**
 * 定时器
 */
public class QuitTimer {

    private PlayService mPlayService;
    private EventCallback<Long> mTimerCallback;
    private Handler mHandler;
    private long mTimerRemain;

    public static QuitTimer getInstance() {
        return SingletonHolder.QUIT_TIMER_INSTANCE;
    }

    private static class SingletonHolder {
        private static final QuitTimer QUIT_TIMER_INSTANCE = new QuitTimer();
    }

    private QuitTimer() {}

    /**
     * 初始化      用@NonNull注解，表示不能为null
     * @param playService               playService
     * @param handler                   handler
     * @param timerCallback             timerCallback
     */
    public void init(@NonNull PlayService playService, @NonNull Handler handler,
                     @NonNull EventCallback<Long> timerCallback) {
        mPlayService = playService;
        mHandler = handler;
        mTimerCallback = timerCallback;
    }

    public void start(long milli) {
        if(mHandler==null){
            ToastUtils.showShort("请先进行初始化");
            return;
        }
        stop();
        if (milli > 0) {
            mTimerRemain = milli + DateUtils.SECOND_IN_MILLIS;
            mHandler.post(mQuitRunnable);
        } else {
            mTimerRemain = 0;
            mTimerCallback.onEvent(mTimerRemain);
        }
    }

    public void stop() {
        mHandler.removeCallbacks(mQuitRunnable);
    }

    private Runnable mQuitRunnable = new Runnable() {
        @Override
        public void run() {
            mTimerRemain -= DateUtils.SECOND_IN_MILLIS;
            if (mTimerRemain > 0) {
                mTimerCallback.onEvent(mTimerRemain);
                mHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            } else {
                mPlayService.quit();
            }
        }
    };
}
