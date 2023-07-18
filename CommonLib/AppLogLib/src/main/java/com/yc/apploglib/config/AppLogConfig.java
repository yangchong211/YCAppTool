package com.yc.apploglib.config;


/**
 * @author yangchong
 * @GitHub : <a href="https://github.com/yangchong211/YCCommonLib">...</a>
 * @email : yangchong211@163.com
 * @time  : 2018/11/9
 * @desc  : log日志配置类
 * @revise:
 */
public final class AppLogConfig {

    /**
     * 是否支持debug
     */
    private final boolean enableDbgLog;
    /**
     * log等级
     */
    private final int minLogLevel;
    /**
     * 是否写入文件
     */
    private final boolean isWriteFile;
    /**
     * 文件路径
     */
    private final String filePath;
    /**
     * 日志标签tag
     */
    private final String tag;

    private AppLogConfig(Builder builder) {
        this.enableDbgLog = builder.enableDbgLog;
        this.minLogLevel = builder.minLogLevel;
        this.filePath = builder.filePath;
        this.isWriteFile = builder.isWriteFile;
        this.tag = builder.tag;
    }


    public boolean isEnableDbgLog() {
        return enableDbgLog;
    }

    public int getMinLogLevel() {
        return minLogLevel;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isWriteFile() {
        return isWriteFile;
    }

    public String getTag() {
        if (tag == null || tag.length() == 0) {
            return "AppLogHelper: ";
        }
        return tag;
    }

    public static class Builder {
        private boolean enableDbgLog;
        private int minLogLevel = LogDispatcher.OFF;
        private String filePath;
        private boolean isWriteFile;
        private String tag;

        public Builder enableDbgLog(boolean debugLog) {
            this.enableDbgLog = debugLog;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setLogTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder isWriteFile(boolean isWriteFile) {
            this.isWriteFile = isWriteFile;
            return this;
        }

        public Builder minLogLevel(int minLogLevel) {
            this.minLogLevel = minLogLevel;
            return this;
        }

        public AppLogConfig build() {
            return new AppLogConfig(this);
        }
    }

}

