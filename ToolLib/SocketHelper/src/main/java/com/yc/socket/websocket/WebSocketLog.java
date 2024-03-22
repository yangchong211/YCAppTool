package com.yc.socket.websocket;

import android.util.Log;

public class WebSocketLog {

    public static void i(String tag, String message) {
        Log.i("WebSocketLog:  " + tag, message);
    }

    public static void e(String tag, String message, Exception e) {
        Log.e("WebSocketLog:  " + tag, message + "    " + e.getMessage());
    }
}
