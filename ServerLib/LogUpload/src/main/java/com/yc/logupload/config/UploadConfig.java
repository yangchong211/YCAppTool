package com.yc.logupload.config;


import android.content.Context;

import com.yc.logupload.inter.IUploadLog;
import com.yc.logupload.report.HttpUploadImpl;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 配置文件
 *     revise :
 * </pre>
 */
public final class UploadConfig {

    /**
     * 网络上报接口
     */
    private final String serverHost;
    /**
     * 总的文件大小限制
     */
    private final long totalFileSize;
    /**
     * 单个文件最大大小
     */
    private final long maxFileSize;
    /**
     * 是否是debug环境
     */
    private final boolean debuggable;
    /**
     * log路径
     */
    private final File logDir;
    /**
     * 额外的log路径
     */
    private final File extraLogDir;
    /**
     * 上传方式
     */
    private final IUploadLog iUploadLog;

    public String getServerHost() {
        return serverHost;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public boolean isDebuggable() {
        return debuggable;
    }

    public File getExtraLogDir() {
        return extraLogDir;
    }

    public File getLogDir() {
        return logDir;
    }

    public IUploadLog getUploadLog() {
        return iUploadLog;
    }

    private UploadConfig(Builder builder) {
        serverHost = builder.serverHost;
        if(serverHost==null || serverHost.length()==0){
            throw new NullPointerException("serverHost must be not null");
        }
        totalFileSize = builder.totalFileSize;
        maxFileSize = builder.maxFileSize;
        debuggable = builder.debuggable;
        extraLogDir = builder.extraLogDir;
        logDir = builder.logDir;
        iUploadLog = builder.iUploadLog;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String serverHost;
        private long totalFileSize = 50 * 1024 * 1024;
        private long maxFileSize = 5 * 1024 * 1024;
        private boolean debuggable;
        private File logDir;
        private File extraLogDir;
        private IUploadLog iUploadLog;

        public Builder() {

        }

        public Builder serverHost(String serverHost) {
            this.serverHost = serverHost;
            return this;
        }

        public Builder totalFileSize(long totalFileSize) {
            this.totalFileSize = totalFileSize;
            return this;
        }

        public Builder maxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
            return this;
        }

        public Builder debuggable(boolean debuggable) {
            this.debuggable = debuggable;
            return this;
        }

        public Builder extraLogDir(File extraLogDir) {
            this.extraLogDir = extraLogDir;
            return this;
        }

        public Builder logDir(File logDir) {
            this.logDir = logDir;
            return this;
        }

        public Builder iUploadLog(IUploadLog iUploadLog) {
            this.iUploadLog = iUploadLog;
            return this;
        }

        public UploadConfig build(Context context) {
            if (iUploadLog == null){
                iUploadLog = new HttpUploadImpl(context);
            }
            return new UploadConfig(this);
        }
    }
}
