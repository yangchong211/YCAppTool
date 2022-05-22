package com.yc.tracesdk;

import android.os.Debug;

public class MethodTrace {
    private static boolean mTracing = false;

    // 100M
    private static final int BUFFER_SIZE = 1024 * 1024 * 100;

    public static void startTrace(String traceName) {
        if (!mTracing) {
            Debug.startMethodTracing(traceName, BUFFER_SIZE);
            mTracing = true;
        }
    }

    public static void stopTrace() {
        if (mTracing) {
            Debug.stopMethodTracing();
            mTracing = false;
        }
    }
}
