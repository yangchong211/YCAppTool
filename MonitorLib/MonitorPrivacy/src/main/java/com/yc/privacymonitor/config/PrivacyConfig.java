package com.yc.privacymonitor.config;


import android.content.Context;
import android.util.Log;

import com.yc.appcommoninter.ILogger;
import com.yc.privacymonitor.helper.PrivacyHelper;

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
    private final ILogger mLogger;

    private PrivacyConfig(Builder builder) {
        this.mApplication = builder.mApplication;
        if (builder.mLogger != null) {
            this.mLogger = builder.mLogger;
        } else {
            this.mLogger = new ILogger() {
                @Override
                public void log(String log) {
                    Log.i(PrivacyHelper.TAG,log);
                }

                @Override
                public void error(String error) {
                    Log.e(PrivacyHelper.TAG,error);
                }
            };
        }
    }

    public Context getApplication() {
        return this.mApplication;
    }

    public ILogger getLogger() {
        return this.mLogger;
    }

    public static class Builder {
        private final Context mApplication;
        private ILogger mLogger;

        public Builder(Context context) {
            this.mApplication = context;
        }

        public Builder setLogger(ILogger logger) {
            this.mLogger = logger;
            return this;
        }

        public PrivacyConfig build() {
            return new PrivacyConfig(this);
        }
    }
}

