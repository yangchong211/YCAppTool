package com.yc.notcapturelib.helper;
import com.yc.appcommoninter.IMonitorToggle;

import java.util.ArrayList;

/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : 配置类
 */
public final class CaptureConfig {

    private CaptureConfig() {

    }

    /**
     * 是否是debug环境
     */
    private boolean isDebug;
    /**
     * 是否禁用代理
     */
    private boolean proxy;
    /**
     * 证书地址
     */
    private String cerPath;
    /**
     * 是否进行CA证书校验
     */
    private boolean isCaVerify;
    /**
     * 是否进行数据加密和解密
     */
    private boolean isEncrypt;
    /**
     * 加解密版本
     */
    private String encryptVersion = "2";
    /**
     * 参数
     */
    private ArrayList<String> reservedQueryParam;
    /**
     * 域名
     */
    private ArrayList<String> url;
    /**
     * 加解密的key
     */
    private String encryptKey;
    /**
     * 降级接口
     */
    private IMonitorToggle monitorToggle;

    public boolean isDebug() {
        return isDebug;
    }

    public boolean isProxy() {
        return proxy;
    }

    public String getCerPath() {
        return cerPath;
    }

    public boolean isCaVerify() {
        return isCaVerify;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public String getEncryptVersion() {
        return encryptVersion;
    }

    public ArrayList<String> getReservedQueryParam() {
        return reservedQueryParam;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public IMonitorToggle getMonitorToggle() {
        return monitorToggle;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private final CaptureConfig config;

        private Builder() {
            config = new CaptureConfig();
        }

        public Builder setDebug(boolean isDebug) {
            config.isDebug = isDebug;
            return this;
        }

        public Builder setProxy(boolean isProxy) {
            config.proxy = isProxy;
            return this;
        }

        public Builder setCerPath(String cerPath) {
            config.cerPath = cerPath;
            return this;
        }

        public Builder setCaVerify(boolean isCaVerify) {
            config.isCaVerify = isCaVerify;
            return this;
        }

        public Builder setEncrypt(boolean isEncrypt) {
            config.isEncrypt = isEncrypt;
            return this;
        }

        public Builder setEncryptVersion(String encryptVersion) {
            config.encryptVersion = encryptVersion;
            return this;
        }

        public Builder setReservedQueryParam(ArrayList<String> reservedQueryParam) {
            config.reservedQueryParam = reservedQueryParam;
            return this;
        }

        public Builder setEncryptKey(String encryptKey) {
            config.encryptKey = encryptKey;
            return this;
        }


        public Builder setHostUrl(ArrayList<String> url) {
            config.url = url;
            return this;
        }

        public Builder setMonitorToggle(IMonitorToggle monitorToggle) {
            config.monitorToggle = monitorToggle;
            return this;
        }

        public CaptureConfig build() {
            return config;
        }
    }

}
