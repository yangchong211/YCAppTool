package com.yc.audioplayer.bean;


import android.content.Context;
import android.util.Log;

import com.yc.appcommoninter.IEventTrack;
import com.yc.appcommoninter.IExceptionTrack;
import com.yc.appcommoninter.ILogger;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 配置类
 *     revise:
 * </pre>
 */
public final class TtsPlayerConfig {

    private final Context mApplication;
    private final ILogger mLogger;
    private final IExceptionTrack mExceptionTrack;
    private final IEventTrack mEventTrack;
    private final static String TAG = "TtsPlayer: ";

    private TtsPlayerConfig(Builder builder) {
        this.mApplication = builder.mApplication;
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
    }

    public Context getApplication() {
        return this.mApplication;
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

    public static class Builder {
        private final Context mApplication;
        private ILogger mLogger;
        private IExceptionTrack mExceptionTrack;
        private IEventTrack mEventTrack;

        public Builder(Context context) {
            this.mApplication = context;
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

        public TtsPlayerConfig build() {
            return new TtsPlayerConfig(this);
        }
    }
}

