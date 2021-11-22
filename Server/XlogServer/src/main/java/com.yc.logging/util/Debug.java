package com.yc.logging.util;

import android.support.annotation.RestrictTo;
import android.util.Log;
import com.yc.logging.LoggerFactory;
import com.yc.logging.BuildConfig;


@RestrictTo(RestrictTo.Scope.LIBRARY)
public class Debug {

    public static boolean isDebuggable() {
        return LoggerFactory.getConfig().isDebuggable() || BuildConfig.DEBUG;
    }

    private static final String LOG_TAG = "logging";

    public static void d(String msg) {
        if (isDebuggable()) Log.d(LOG_TAG, msg);
    }

    public static void i(String msg) {
        if (isDebuggable()) Log.i(LOG_TAG, msg);
    }

    public static void e(String msg) {
        if (isDebuggable()) Log.e(LOG_TAG, msg);
    }

    public static void e(String msg, Throwable e) {
        if (isDebuggable()) Log.e(LOG_TAG, msg, e);
    }

    public static void logOrThrow(String msg, Throwable throwable) {
        if (isDebuggable()) {
            throw new RuntimeException(msg, throwable);
        } else {
            Log.e(LOG_TAG, msg, throwable);
        }
    }
}
