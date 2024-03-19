package com.yc.audioplayer.powerful.bean;


import android.util.Log;

import com.yc.appcommoninter.IEventTrack;
import com.yc.appcommoninter.IExceptionTrack;
import com.yc.appcommoninter.ILogger;

/**
 * <pre>
 *     @author 杨充
 *     blog  : <a href="https://github.com/yangchong211">...</a>
 *     time  : 2017/01/30
 *     desc  : 配置类
 *     revise:
 * </pre>
 */
public final class TtsPlayerConfig {

    private final ILogger mLogger;
    private final IExceptionTrack mExceptionTrack;
    private final IEventTrack mEventTrack;
    private final boolean mIsTtsDeque;
    private final static String TAG = "TtsPlayer: ";

    private TtsPlayerConfig(Builder builder) {
        if (builder.mLogger != null) {
            this.mLogger = builder.mLogger;
        } else {
            this.mLogger = new ILogger() {
                @Override
                public void log(String log) {
                    Log.i(TAG,log);
                }

                @Override
                public void error(String error) {
                    Log.e(TAG,error);
                }
            };
        }
        this.mExceptionTrack = builder.mExceptionTrack;
        this.mEventTrack = builder.mEventTrack;
        this.mIsTtsDeque = builder.mIsTtsDeque;
    }


    public ILogger getLogger() {
        return this.mLogger;
    }

    public IExceptionTrack getExceptionTrack() {
        return mExceptionTrack;
    }

    public IEventTrack getEventTrack() {
        return mEventTrack;
    }

    public boolean getIsTtsDeque(){
        return mIsTtsDeque;
    }

    public static class Builder {
        private ILogger mLogger;
        private IExceptionTrack mExceptionTrack;
        private IEventTrack mEventTrack;
        private boolean mIsTtsDeque = true;

        public Builder() {

        }

        public Builder setLogger(ILogger logger) {
            this.mLogger = logger;
            return this;
        }

        public Builder setExceptionTrack(IExceptionTrack exceptionTrack) {
            this.mExceptionTrack = exceptionTrack;
            return this;
        }

        public Builder setEventTrack(IEventTrack eventTrack) {
            this.mEventTrack = eventTrack;
            return this;
        }

        public Builder setTtsDeque(boolean isTtsDeque) {
            this.mIsTtsDeque = isTtsDeque;
            return this;
        }

        public TtsPlayerConfig build() {
            return new TtsPlayerConfig(this);
        }
    }
}

