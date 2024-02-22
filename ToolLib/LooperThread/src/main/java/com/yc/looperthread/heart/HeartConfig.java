package com.yc.looperthread.heart;

import android.content.Context;

public class HeartConfig {

    /**
     * 上下文
     */
    private Context context;
    /**
     * 轮询间隔时间
     */
    private long looperTime = 10 * 1000;

    public Context getContext() {
        return this.context;
    }

    public long getLooperTime() {
        return looperTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final HeartConfig config;

        private Builder() {
            this.config = new HeartConfig();
        }

        public Builder setContext(Context context) {
            this.config.context = context;
            return this;
        }

        public Builder setLooperTime(long looperTime) {
            this.config.looperTime = looperTime;
            return this;
        }

        public HeartConfig build() {
            return this.config;
        }
    }
}
