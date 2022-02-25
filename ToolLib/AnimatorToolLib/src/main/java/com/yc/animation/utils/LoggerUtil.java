package com.yc.animation.utils;

import android.util.Log;

/**
 * @author 杨充
 * @date 2017/5/11 11:14
 */
public class LoggerUtil {
    private LoggerUtil() {}

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void e(String tag, String errorMsg) {
        Log.e(tag, errorMsg);
    }
}
