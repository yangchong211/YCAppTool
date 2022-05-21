package com.yc.longalive;


import android.app.Application;
import android.util.Log;

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
    private final ILongAliveMonitorToggle mToggle;
    private final ILongAliveEventTrack mEventTrack;
    private final ILongAliveLogger mLogger;

    private LongAliveMonitorConfig(Builder builder) {
        this.mApplication = builder.mApplication;
        if (builder.mToggle != null) {
            this.mToggle = builder.mToggle;
        } else {
            this.mToggle = new ILongAliveMonitorToggle() {
                @Override
                public boolean isOpen() {
                    return false;
                }
            };
        }

        if (builder.mEventTrack != null) {
            this.mEventTrack = builder.mEventTrack;
        } else {
            this.mEventTrack = new ILongAliveEventTrack() {
                @Override
                public void onEvent(HashMap<String, String> params) {
                }
            };
        }

        if (builder.mLogger != null) {
            this.mLogger = builder.mLogger;
        } else {
            this.mLogger = new ILongAliveLogger() {
                @Override
                public void log(String log) {
                    Log.i("LongevityMonitor",log);
                }
            };
        }

    }

    Application getApplication() {
        return this.mApplication;
    }

    ILongAliveMonitorToggle getToggle() {
        return this.mToggle;
    }

    ILongAliveEventTrack getEventTrack() {
        return this.mEventTrack;
    }

    ILongAliveLogger getLogger() {
        return this.mLogger;
    }

    public static class Builder {
        private final Application mApplication;
        private ILongAliveMonitorToggle mToggle;
        private ILongAliveEventTrack mEventTrack;
        private ILongAliveLogger mLogger;

        public Builder(Application application) {
            this.mApplication = application;
        }

        public Builder setToggle(ILongAliveMonitorToggle toggle) {
            this.mToggle = toggle;
            return this;
        }

        public Builder setEventTrack(ILongAliveEventTrack eventTrack) {
            this.mEventTrack = eventTrack;
            return this;
        }

        public Builder setLogger(ILongAliveLogger logger) {
            this.mLogger = logger;
            return this;
        }

        public LongAliveMonitorConfig build() {
            return new LongAliveMonitorConfig(this);
        }
    }
}

