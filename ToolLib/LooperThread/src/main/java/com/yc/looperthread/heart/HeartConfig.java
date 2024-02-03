package com.yc.looperthread.heart;

import android.content.Context;

public class HeartConfig {

    private Context context;

    public Context getContext() {
        return this.context;
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

        public HeartConfig build() {
            return this.config;
        }
    }

}
