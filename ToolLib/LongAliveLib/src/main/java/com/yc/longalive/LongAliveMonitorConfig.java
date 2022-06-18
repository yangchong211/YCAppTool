package com.yc.longalive;


import android.app.Application;
import android.util.Log;

import com.yc.appcommoninter.IEventTrack;
import com.yc.appcommoninter.ILogger;
import com.yc.appcommoninter.IMonitorToggle;

import java.util.HashMap;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 配置类
 *     revise:
 * </pre>
 */
public final class LongAliveMonitorConfig {

    private final Application mApplication;
    private final IMonitorToggle mToggle;
    private final IEventTrack mEventTrack;
    private final ILogger mLogger;

    private LongAliveMonitorConfig(Builder builder) {
        this.mApplication = builder.mApplication;
        if (builder.mToggle != null) {
            this.mToggle = builder.mToggle;
        } else {
            this.mToggle = new IMonitorToggle() {
                @Override
                public boolean isOpen() {
                    return false;
                }
            };
        }

        if (builder.mEventTrack != null) {
            this.mEventTrack = builder.mEventTrack;
        } else {
            this.mEventTrack = new IEventTrack() {
                @Override
                public void onEvent(HashMap<String, String> params) {
                }
            };
        }

        if (builder.mLogger != null) {
            this.mLogger = builder.mLogger;
        } else {
            this.mLogger = new ILogger() {
                @Override
                public void log(String log) {
                    Log.i("LongevityMonitor",log);
                }

                @Override
                public void error(String error) {
                    Log.e("LongevityMonitor",error);
                }
            };
        }

    }

    Application getApplication() {
        return this.mApplication;
    }

    IMonitorToggle getToggle() {
        return this.mToggle;
    }

    IEventTrack getEventTrack() {
        return this.mEventTrack;
    }

    ILogger getLogger() {
        return this.mLogger;
    }

    public static class Builder {
        private final Application mApplication;
        private IMonitorToggle mToggle;
        private IEventTrack mEventTrack;
        private ILogger mLogger;

        public Builder(Application application) {
            this.mApplication = application;
        }

        public Builder setToggle(IMonitorToggle toggle) {
            this.mToggle = toggle;
            return this;
        }

        public Builder setEventTrack(IEventTrack eventTrack) {
            this.mEventTrack = eventTrack;
            return this;
        }

        public Builder setLogger(ILogger logger) {
            this.mLogger = logger;
            return this;
        }

        public LongAliveMonitorConfig build() {
            return new LongAliveMonitorConfig(this);
        }
    }
}

