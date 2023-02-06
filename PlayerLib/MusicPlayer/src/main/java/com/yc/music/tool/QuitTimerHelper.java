package com.yc.music.tool;

import android.os.Handler;
import android.text.format.DateUtils;
import androidx.annotation.NonNull;
import com.yc.music.service.PlayAudioService;


/**
 * 定时器
 */
public class QuitTimerHelper {

    private PlayAudioService mPlayService;
    private EventCallback<Long> mTimerCallback;
    private Handler mHandler;
    private long mTimerRemain;

    public static QuitTimerHelper getInstance() {
        //使用单例模式
        return SingletonHolder.QUIT_TIMER_INSTANCE;
    }

    private static class SingletonHolder {
        private static final QuitTimerHelper QUIT_TIMER_INSTANCE = new QuitTimerHelper();
    }

    private QuitTimerHelper() {}

    /**
     * 初始化      用@NonNull注解，表示不能为null
     * @param playService               playService
     * @param handler                   handler
     * @param timerCallback             timerCallback
     */
    public void init(@NonNull PlayAudioService playService, @NonNull Handler handler,
                     @NonNull EventCallback<Long> timerCallback) {
        mPlayService = playService;
        mHandler = handler;
        mTimerCallback = timerCallback;
    }

    public Handler getHandler(){
        return mHandler;
    }

    public void start(long milli) {
        if(mHandler==null){
            //ToastUtils.showShort("请先进行初始化");
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
        if (mHandler!=null){
            mHandler.removeCallbacks(mQuitRunnable);
        }
    }

    private final Runnable mQuitRunnable = new Runnable() {
        //开启runnable
        @Override
        public void run() {
            mTimerRemain -= DateUtils.SECOND_IN_MILLIS;
            if (mTimerRemain > 0) {
                mTimerCallback.onEvent(mTimerRemain);
                //一秒种发送一次消息
                mHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            } else {
                mPlayService.quit();
            }
        }
    };

    public interface EventCallback<T> {
        void onEvent(T t);
    }
}
