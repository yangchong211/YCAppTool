package me.pqpo.librarylog4a.appender;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.pqpo.librarylog4a.Level;
import me.pqpo.librarylog4a.LogBuffer;
import me.pqpo.librarylog4a.formatter.Formatter;
import me.pqpo.librarylog4a.interceptor.Interceptor;

public class FileAppender extends AbsAppender {

    private final LogBuffer logBuffer;

    private Formatter formatter;

    protected FileAppender(Builder builder) {
        logBuffer = new LogBuffer(builder.bufferFilePath, builder.bufferSize, builder.logFilePath, builder.compress);
        setMaxSingleLength(builder.bufferSize);
        setLevel(builder.level);
        addInterceptor(builder.interceptors);
        setFormatter(builder.formatter);
    }

    public String getBufferPath() {
        return logBuffer.getBufferPath();
    }

    public int getBufferSize() {
        return logBuffer.getBufferSize();
    }

    public String getLogPath() {
        return logBuffer.getLogPath();
    }

    public void changeLogPath(String logPath) {
        logBuffer.changeLogPath(logPath);
    }

    public void setFormatter(Formatter formatter) {
        if (formatter != null) {
            this.formatter = formatter;
        }
    }

    @Override
    protected void doAppend(int logLevel, String tag, String msg) {
        logBuffer.write(formatter.format(logLevel, tag, msg));
    }

    @Override
    public void flush() {
        super.flush();
        logBuffer.flushAsync();
    }

    @Override
    public void release() {
        super.release();
        logBuffer.release();
    }

    public static class Builder {

        private Context context;

        private String bufferFilePath;
        private String logFilePath;
        private int bufferSize = 4096;
        private int level = Level.VERBOSE;
        private List<Interceptor> interceptors;
        private Formatter formatter;
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

        public Builder setFormatter(Formatter formatter) {
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
                formatter = new Formatter() {
                    @Override
                    public String format(int logLevel, String tag, String msg) {
                        return String.format("%s/%s: %s\n",  Level.getShortLevelName(logLevel), tag, msg);
                    }
                };
            }
            return new FileAppender(this);
        }

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
