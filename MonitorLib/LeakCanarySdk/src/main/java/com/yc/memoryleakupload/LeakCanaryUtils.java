package com.yc.memoryleakupload;

import android.util.Log;

public class LeakCanaryUtils {

    /**
     * 打印日志
     *
     * @param tag tag
     * @param text text
     */
    public static void debug(String tag, String text) {
        Log.d(tag, text);
    }
}
