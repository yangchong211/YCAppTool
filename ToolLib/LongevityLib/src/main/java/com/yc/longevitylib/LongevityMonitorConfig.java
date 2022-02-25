package com.yc.longevitylib;


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
public class LongevityMonitorConfig {

    private final Application mApplication;
    private final LongevityMonitorConfig.ILongevityMonitorApolloToggle mToggle;
    private final LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack mEventTrack;
    private final LongevityMonitorConfig.ILongevityMonitorLogger mLogger;

    private LongevityMonitorConfig(LongevityMonitorConfig.Builder builder) {
        this.mApplication = builder.mApplication;
        if (builder.mToggle != null) {
            this.mToggle = builder.mToggle;
        } else {
            this.mToggle = new LongevityMonitorConfig.ILongevityMonitorApolloToggle() {
                public boolean isOpen() {
                    return false;
                }
            };
        }

        if (builder.mEventTrack != null) {
            this.mEventTrack = builder.mEventTrack;
        } else {
            this.mEventTrack = new LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack() {
                public void onEvent(HashMap<String, String> params) {
                }
            };
        }

        if (builder.mLogger != null) {
            this.mLogger = builder.mLogger;
        } else {
            this.mLogger = new LongevityMonitorConfig.ILongevityMonitorLogger() {
                public void log(String log) {
                    Log.i("LongevityMonitor",log);
                }
            };
        }

    }

    Application getApplication() {
        return this.mApplication;
    }

    LongevityMonitorConfig.ILongevityMonitorApolloToggle getToggle() {
        return this.mToggle;
    }

    LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack getEventTrack() {
        return this.mEventTrack;
    }

    LongevityMonitorConfig.ILongevityMonitorLogger getLogger() {
        return this.mLogger;
    }

    public interface ILongevityMonitorLogger {
        void log(String var1);
    }

    public interface ILongevityMonitorOmegaEventTrack {
        void onEvent(HashMap<String, String> var1);
    }

    public interface ILongevityMonitorApolloToggle {
        boolean isOpen();
    }

    public static class Builder {
        private final Application mApplication;
        private LongevityMonitorConfig.ILongevityMonitorApolloToggle mToggle;
        private LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack mEventTrack;
        private LongevityMonitorConfig.ILongevityMonitorLogger mLogger;

        public Builder(Application application) {
            this.mApplication = application;
        }

        public LongevityMonitorConfig.Builder setToggle(LongevityMonitorConfig.ILongevityMonitorApolloToggle toggle) {
            this.mToggle = toggle;
            return this;
        }

        public LongevityMonitorConfig.Builder setEventTrack(LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack eventTrack) {
            this.mEventTrack = eventTrack;
            return this;
        }

        public LongevityMonitorConfig.Builder setLogger(LongevityMonitorConfig.ILongevityMonitorLogger logger) {
            this.mLogger = logger;
            return this;
        }

        public LongevityMonitorConfig build() {
            return new LongevityMonitorConfig(this);
        }
    }
}

