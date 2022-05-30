package com.yc.easyble.config;


import android.content.Context;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 配置文件
 *     revise :
 * </pre>
 */
public final class BleScanConfig {

    private static final int DEFAULT_MAX_MULTIPLE_DEVICE = 7;
    private static final int DEFAULT_OPERATE_TIME = 5000;

    private final int maxConnectCount;
    private final int operateTimeout;

    public int getMaxConnectCount() {
        return maxConnectCount;
    }

    public int getOperateTimeout() {
        return operateTimeout;
    }


    private BleScanConfig(Builder builder) {
        maxConnectCount = builder.maxConnectCount;
        operateTimeout = builder.operateTimeout;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private int maxConnectCount = DEFAULT_MAX_MULTIPLE_DEVICE;
        private int operateTimeout = DEFAULT_OPERATE_TIME;

        public Builder() {

        }

        public Builder setMaxConnectCount(int count) {
            if (count > DEFAULT_MAX_MULTIPLE_DEVICE) {
                count = DEFAULT_MAX_MULTIPLE_DEVICE;
            }
            this.maxConnectCount = count;
            return this;
        }

        public Builder setOperateTimeout(int count) {
            this.operateTimeout = count;
            return this;
        }

        public BleScanConfig build(Context context) {
            return new BleScanConfig(this);
        }
    }
}
