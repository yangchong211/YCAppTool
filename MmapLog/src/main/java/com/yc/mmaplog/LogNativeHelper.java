package com.yc.mmaplog;

import android.util.Log;

public final class LogNativeHelper {

    private static final String TAG = "log_buffer";

    private long ptr = 0;
    private String logPath;
    private final String bufferPath;
    private final int bufferSize;
    private final boolean compress;

    public LogNativeHelper(String bufferPath, int capacity, String logPath, boolean compress) {
        this.bufferPath = bufferPath;
        this.bufferSize = capacity;
        this.logPath = logPath;
        this.compress = compress;
        try {
            ptr = initNative(bufferPath, capacity, logPath, compress);
        } catch (Exception e) {
            Log.e(TAG, LogManager.getStackTraceString(e));
        }
    }

    public void changeLogPath(String logPath) {
        if (ptr != 0) {
            try {
                changeLogPathNative(ptr, logPath);
                this.logPath = logPath;
            } catch (Exception e) {
                Log.e(TAG, LogManager.getStackTraceString(e));
            }
        }
    }

    public boolean isCompress() {
        return compress;
    }

    public String getLogPath() {
        return logPath;
    }

    public String getBufferPath() {
        return bufferPath;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void write(String log) {
        if (ptr != 0) {
            try {
                writeNative(ptr, log);
            } catch (Exception e) {
                Log.e(TAG, LogManager.getStackTraceString(e));
            }
        }
    }

    public void flushAsync() {
        if (ptr != 0) {
            try {
                flushAsyncNative(ptr);
            } catch (Exception e) {
                Log.e(TAG, LogManager.getStackTraceString(e));
            }
        }
    }

    public void release() {
        if (ptr != 0) {
            try {
                releaseNative(ptr);
            } catch (Exception e) {
                Log.e(TAG, LogManager.getStackTraceString(e));
            }
            ptr = 0;
        }
    }

    static {
        System.loadLibrary("log_manager");
    }

    /**
     * 初始化
     *
     * @param bufferPath 缓冲路径
     * @param bufferSize 缓冲大小，目前是4096
     * @param logPath    日志路径
     * @param compress   是否压缩
     * @return 返回
     */
    private native static long initNative(String bufferPath, int bufferSize, String logPath, boolean compress);

    private native void writeNative(long ptr, String log);

    private native void flushAsyncNative(long ptr);

    private native void releaseNative(long ptr);

    private native void changeLogPathNative(long ptr, String logPath);

}
