package com.yc.mmaplog.appender;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yc.mmaplog.LogLevel;
import com.yc.mmaplog.LogNativeHelper;
import com.yc.mmaplog.inter.InterFormatter;
import com.yc.mmaplog.inter.Interceptor;

public class FileAppender extends AbsAppender {

    private final LogNativeHelper logNativeHelper;
    private InterFormatter formatter;

    protected FileAppender(Builder builder) {
        logNativeHelper = new LogNativeHelper(builder.bufferFilePath, builder.bufferSize,
                builder.logFilePath, builder.compress);
        setMaxSingleLength(builder.bufferSize);
        setLevel(builder.level);
        addInterceptor(builder.interceptors);
        setFormatter(builder.formatter);
    }

    public String getBufferPath() {
        return logNativeHelper.getBufferPath();
    }

    public int getBufferSize() {
        return logNativeHelper.getBufferSize();
    }

    public String getLogPath() {
        return logNativeHelper.getLogPath();
    }

    public void changeLogPath(String logPath) {
        logNativeHelper.changeLogPath(logPath);
    }

    public void setFormatter(InterFormatter formatter) {
        if (formatter != null) {
            this.formatter = formatter;
        }
    }

    @Override
    protected void doAppend(int logLevel, String tag, String msg) {
        logNativeHelper.write(formatter.format(logLevel, tag, msg));
    }

    @Override
    public void flush() {
        super.flush();
        logNativeHelper.flushAsync();
    }

    @Override
    public void release() {
        super.release();
        logNativeHelper.release();
    }

    public static class Builder {
        private final Context context;
        private String bufferFilePath;
        private String logFilePath;
        private int bufferSize = 4096;
        private int level = LogLevel.VERBOSE;
        private List<Interceptor> interceptors;
        private InterFormatter formatter;
        private boolean compress;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder setBufferFilePath(String bufferFilePath) {
            this.bufferFilePath = bufferFilePath;
            return this;
        }

        public Builder setLogFilePath(String logFilePath) {
            this.logFilePath = logFilePath;
            return this;
        }

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        public Builder setFormatter(InterFormatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public Builder setCompress(boolean compress) {
            this.compress = compress;
            return this;
        }

        public FileAppender create() {
            if (logFilePath == null) {
                throw new IllegalArgumentException("logFilePath cannot be null");
            }
            if (bufferFilePath == null) {
                bufferFilePath = getDefaultBufferPath(context);
            }
            if (formatter == null) {
                formatter = new InterFormatter() {
                    @Override
                    public String format(int logLevel, String tag, String msg) {
                        return String.format("%s/%s: %s\n",  LogLevel.getShortLevelName(logLevel), tag, msg);
                    }
                };
            }
            return new FileAppender(this);
        }

        /**
         * 获取缓冲路径
         * @param context       上下文
         * @return              路径
         */
        private String getDefaultBufferPath(Context context) {
            File bufferFile;
            if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)
                    && context.getExternalFilesDir("log4a") != null) {
                bufferFile = context.getExternalFilesDir("log4a");
            } else {
                bufferFile = new File(context.getFilesDir(), "log4a");
            }
            if (bufferFile != null && !bufferFile.exists()) {
                bufferFile.mkdirs();
            }
            return new File(bufferFile, ".log4aCache").getAbsolutePath();
        }

    }

}
