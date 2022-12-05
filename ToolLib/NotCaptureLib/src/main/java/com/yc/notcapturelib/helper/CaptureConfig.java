package com.yc.notcapturelib.helper;


import android.graphics.Bitmap;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/11/30
 *    desc   : 配置类
 */
public final class CaptureConfig {

    private CaptureConfig(){

    }

    /**
     * 是否禁用代理
     */
    private boolean proxy;
    /**
     * 证书地址
     */
    private String cerPath;
    /**
     * 是否进行数据加密和解密
     */
    private boolean isEncrypt;

    public boolean isProxy() {
        return proxy;
    }

    public String getCerPath() {
        return cerPath;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private final CaptureConfig config;

        private Builder() {
            config = new CaptureConfig();
        }

        public Builder setProxy(boolean isProxy) {
            config.proxy = isProxy;
            return this;
        }

        public Builder setCerPath(String cerPath) {
            config.cerPath = cerPath;
            return this;
        }

        public Builder setEncrypt(boolean isEncrypt) {
            config.isEncrypt = isEncrypt;
            return this;
        }

        public CaptureConfig build() {
            return config;
        }
    }

}
