package com.yc.tracesdk;

import android.util.Log;

class LogcatPrinter implements InterPrinter {
    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public void print(String tag, String format, Object... args) {
        Log.d(tag, String.format(format, args));
    }
}
