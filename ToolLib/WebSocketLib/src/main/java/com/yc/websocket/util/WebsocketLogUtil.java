package com.yc.websocket.util;

import android.util.Log;

public class WebsocketLogUtil {
    public static void i(String tag, String message) {
        Log.i("websocketlog:  " + tag, message);
    }

    public static void e(String tag, String message, Exception e) {
        Log.e("websocketlog:  " + tag, message + "    " + e.getMessage());
    }
}
