package me.pqpo.librarylog4a;

import android.util.Log;

/**
 * Created by pqpo on 2017/11/16.
 */
public class LogBuffer {

    private static final String TAG = "LogBuffer";

    private long ptr = 0;
    private String logPath;
    private String bufferPath;
    private int bufferSize;
    private boolean compress;

    public LogBuffer(String bufferPath, int capacity, String logPath, boolean compress) {
        this.bufferPath = bufferPath;
        this.bufferSize = capacity;
        this.logPath = logPath;
        this.compress = compress;
        try {
            ptr = initNative(bufferPath, capacity, logPath, compress);
        }catch (Exception e) {
            Log.e(TAG, Log4a.getStackTraceString(e));
        }
    }

    public void changeLogPath(String logPath) {
        if (ptr != 0) {
            try {
                changeLogPathNative(ptr, logPath);
                this.logPath = logPath;
            }catch (Exception e) {
                Log.e(TAG, Log4a.getStackTraceString(e));
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
            }catch (Exception e) {
                Log.e(TAG, Log4a.getStackTraceString(e));
            }
        }
    }

    public void flushAsync() {
        if (ptr != 0) {
            try {
                flushAsyncNative(ptr);
            }catch (Exception e) {
                Log.e(TAG, Log4a.getStackTraceString(e));
            }
        }
    }

    public void release() {
        if (ptr != 0) {
            try {
                releaseNative(ptr);
            }catch (Exception e) {
                Log.e(TAG, Log4a.getStackTraceString(e));
            }
            ptr = 0;
        }
    }

    static {
        System.loadLibrary("log4a-lib");
    }

    private native static long initNative(String bufferPath, int capacity, String logPath, boolean compress);

    private native void writeNative(long ptr, String log);

    private native void flushAsyncNative(long ptr);

    private native void releaseNative(long ptr);

    private native void changeLogPathNative(long ptr, String logPath);

}
