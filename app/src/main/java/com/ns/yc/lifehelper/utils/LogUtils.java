package com.ns.yc.lifehelper.utils;

import android.util.Log;


public class LogUtils {


    public static boolean logDebug = false;
    private static String TAG = "Log日志";
    public static void e(String msg) {
        if (logDebug) {
            Log.e(TAG, msg);
        }
    }


    public static void i(String msg) {
        if (logDebug) {
            Log.i(TAG, msg);
        }
    }


}
