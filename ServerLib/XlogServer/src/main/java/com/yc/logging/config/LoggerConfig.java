package com.yc.logging.config;

import com.yc.logging.constant.Level;
import com.yc.logging.util.Supplier;
import com.yc.toolutils.ObjectsUtils;

import java.io.File;

public class LoggerConfig {

    private String serverHost;
    private final int fileMaxHistory;
    private final long totalFileSize;
    private final int fileSectionLength;
    private final long maxFileSize;
    private final Boolean fileLogEnabled;
    private final Boolean logcatLogEnabled;
    private final boolean encryptEnabled;
    private final boolean debuggable;
    private final Level fileLogLevel;
    private final Level logcatLogLevel;
    private final Supplier<String> phoneNumSupplier;
    private final File logDir;
    private final File extraLogDir;
    private final boolean extraLogCleanEnabled;


    public String getServerHost() {
        return serverHost;
    }

    public int getFileMaxHistory() {
        return fileMaxHistory;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public int getFileSectionLength() {
        return fileSectionLength;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public boolean isDebuggable() {
        return debuggable;
    }

    public Boolean isFileLogEnabled() {
        return fileLogEnabled;
    }

    public Boolean isLogcatLogEnabled() {
        return logcatLogEnabled;
    }

    public Boolean isEncryptEnabled() {
        return encryptEnabled;
    }

    public Level getFileLogLevel() {
        return fileLogLevel;
    }

    public Level getLogcatLogLevel() {
        return logcatLogLevel;
    }

    public Supplier<String> getPhoneNumSupplier() {
        return phoneNumSupplier;
    }

    public File getExtraLogDir() {
        return extraLogDir;
    }

    public File getLogDir() {
        return logDir;
    }

    public boolean isExtraLogCleanEnabled() {
        return extraLogCleanEnabled;
    }

    private LoggerConfig(Builder builder) {
        serverHost = builder.serverHost;
        if(serverHost==null || serverHost.length()==0){
            throw new NullPointerException("serverHost must be not null");
        }
        fileMaxHistory = builder.fileMaxHistory;
        totalFileSize = builder.totalFileSize;
        fileSectionLength = builder.fileSectionLength;
        maxFileSize = builder.maxFileSize;
        fileLogEnabled = builder.fileLogEnabled;
        logcatLogEnabled = builder.logcatLogEnabled;
        encryptEnabled = builder.encryptEnabled;
        debuggable = builder.debuggable;
        fileLogLevel = builder.fileLogLevel;
        logcatLogLevel = builder.logcatLogLevel;
        phoneNumSupplier = builder.phoneNumSupplier;
        extraLogDir = builder.extraLogDir;
        extraLogCleanEnabled = builder.extraLogCleanEnabled;
        logDir = builder.logDir;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String serverHost;
        private int fileMaxHistory = 7;
        private long totalFileSize = 50 * 1024 * 1024;   //70M
        private int fileSectionLength = 2 * 1024 * 1024; //2M
        private long maxFileSize = 5 * 1024 * 1024;      //5M
        private Boolean fileLogEnabled;//allow null
        private Boolean logcatLogEnabled;//allow null
        private boolean debuggable;
        private Level fileLogLevel = Level.INFO;
        private Level logcatLogLevel = Level.TRACE;
        private Supplier<String> phoneNumSupplier;
        private File logDir;
        private File extraLogDir;
        private boolean extraLogCleanEnabled;
        private boolean encryptEnabled = true;

        public Builder() {

        }

        public Builder serverHost(String serverHost) {
            ObjectsUtils.requireNonNull(serverHost);
            this.serverHost = serverHost;
            return this;
        }

        /**
         * 日志文件最多保留天数，默认 7 天，超出时限制将删除最老的文件。
         * @param fileMaxHistory                最大历史记录
         * @return
         */
        public Builder fileMaxHistory(int fileMaxHistory) {
            this.fileMaxHistory = fileMaxHistory;
            return this;
        }

        public Builder totalFileSize(long totalFileSize) {
            this.totalFileSize = totalFileSize;
            return this;
        }

        public Builder fileSectionLength(int fileSectionLength) {
            this.fileSectionLength = fileSectionLength;
            return this;
        }

        public Builder maxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
            return this;
        }

        public Builder fileLogEnabled(Boolean fileLogEnabled) {
            this.fileLogEnabled = fileLogEnabled;
            return this;
        }

        public Builder logcatLogEnabled(Boolean logcatLogEnabled) {
            this.logcatLogEnabled = logcatLogEnabled;
            return this;
        }

        public Builder fileLogEnabled(boolean fileLogEnabled) {
            this.fileLogEnabled = fileLogEnabled;
            return this;
        }

        public Builder logcatLogEnabled(boolean logcatLogEnabled) {
            this.logcatLogEnabled = logcatLogEnabled;
            return this;
        }

        public Builder encryptEnabled(boolean enabled) {
            this.encryptEnabled = enabled;
            return this;
        }

        public Builder debuggable(boolean debuggable) {
            this.debuggable = debuggable;
            return this;
        }

        public Builder fileLogLevel(Level fileLogLevel) {
            ObjectsUtils.requireNonNull(fileLogLevel);
            this.fileLogLevel = fileLogLevel;
            return this;
        }

        public Builder logcatLogLevel(Level logcatLogLevel) {
            ObjectsUtils.requireNonNull(logcatLogLevel);
            this.logcatLogLevel = logcatLogLevel;
            return this;
        }

        public Builder phoneNumSupplier(Supplier<String> phoneNumSupplier) {
            this.phoneNumSupplier = phoneNumSupplier;
            return this;
        }

        public Builder extraLogDir(File extraLogDir) {
            this.extraLogDir = extraLogDir;
            return this;
        }

        public Builder extraLogCleanEnabled(boolean extraLogCleanEnabled) {
            this.extraLogCleanEnabled = extraLogCleanEnabled;
            return this;
        }

        public Builder logDir(File logDir) {
            this.logDir = logDir;
            return this;
        }

        public LoggerConfig build() {
            return new LoggerConfig(this);
        }
    }
}
