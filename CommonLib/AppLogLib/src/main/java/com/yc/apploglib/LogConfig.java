package com.yc.apploglib;

public class LogConfig {
    private boolean enableDbgLog;
    private int minLogLevel;

    private LogConfig(Builder builder) {
        this.enableDbgLog = builder.enableDbgLog;
        this.minLogLevel = builder.minLogLevel;
    }


    public boolean isEnableDbgLog() {
        return enableDbgLog;
    }

    public int getMinLogLevel() {
        return minLogLevel;
    }

    public static class Builder {
        private boolean enableDbgLog;
        private int minLogLevel = LogDispatcher.OFF;

        public Builder enableDbgLog(boolean debugLog) {
            this.enableDbgLog = debugLog;
            return this;
        }

        public Builder minLogLevel(int minLogLevel) {
            this.minLogLevel = minLogLevel;
            return this;
        }

        public LogConfig build() {
            return new LogConfig(this);
        }
    }

}

