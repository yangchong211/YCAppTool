package com.yc.tracesdk;

import com.yc.apploglib.SLog;

class SLogPrinter implements InterPrinter {
    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public void print(String tag, String format, Object... args) {
        SLog.d(tag, format, args);
    }
}
