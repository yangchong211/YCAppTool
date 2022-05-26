package com.yc.tracesdk;

import com.yc.apploglib.AppLogHelper;

class SLogPrinter implements InterPrinter {
    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public void print(String tag, String format, Object... args) {
        AppLogHelper.d(tag, format, args);
    }
}
