package com.yc.privacymonitor.config;


import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 配置类
 *     revise:
 * </pre>
 */
public final class PrivacyConfig {

    private final Context mApplication;
    private final IPrivacyLogger mLogger;

    private PrivacyConfig(Builder builder) {
        this.mApplication = builder.mApplication;
        if (builder.mLogger != null) {
            this.mLogger = builder.mLogger;
        } else {
            this.mLogger = new IPrivacyLogger() {
                @Override
                public void log(String log) {
                    Log.i("PrivacyHelper: ",log);
                }
            };
        }
    }

    public Context getApplication() {
        return this.mApplication;
    }

    public IPrivacyLogger getLogger() {
        return this.mLogger;
    }

    public static class Builder {
        private final Context mApplication;
        private IPrivacyLogger mLogger;

        public Builder(Context context) {
            this.mApplication = context;
        }

        public Builder setLogger(IPrivacyLogger logger) {
            this.mLogger = logger;
            return this;
        }

        public PrivacyConfig build() {
            return new PrivacyConfig(this);
        }
    }
}

