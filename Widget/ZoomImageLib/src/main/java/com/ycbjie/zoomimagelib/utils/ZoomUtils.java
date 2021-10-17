package com.ycbjie.zoomimagelib.utils;

import android.util.Log;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/02/19
 *     desc  : log工具
 *     revise:
 * </pre>
 */
public final class ZoomUtils {

    private static final String TAG = "ZoomUtils";
    private static boolean isLog = true;

    public static void setLog(boolean isLog){
        ZoomUtils.isLog = isLog;
    }

    static void d(String message) {
        if(isLog){
            Log.d(TAG, message);
        }
    }

    static void i(String message) {
        if(isLog){
            Log.i(TAG, message);
        }

    }

    static void e(String message, Throwable throwable) {
        if(isLog){
            Log.e(TAG, message, throwable);
        }
    }

}
