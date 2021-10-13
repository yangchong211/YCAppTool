package com.yc.mocklocationlib.gpsmock.utils;

import android.util.Log;

public final class LogMockUtils {

    public static boolean sShowLog = true;

    private LogMockUtils() {
    }

    public static void log(String tag, String msg) {
        if (sShowLog) {
            Log.d(tag, msg);
        }

    }


    public static void e(String tag, String msg) {
        if (sShowLog) {
            Log.e(tag, msg);
        }

    }
}
