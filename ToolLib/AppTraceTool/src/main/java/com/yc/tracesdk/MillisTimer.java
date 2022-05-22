package com.yc.tracesdk;

import android.os.SystemClock;

class MillisTimer implements InterTimer {
    @Override
    public long currentTime() {
        return SystemClock.elapsedRealtime();
    }

    @Override
    public String unitName() {
        return "ms";
    }
}
