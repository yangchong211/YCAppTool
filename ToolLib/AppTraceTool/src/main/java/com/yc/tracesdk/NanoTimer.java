package com.yc.tracesdk;

import android.os.SystemClock;

class NanoTimer implements InterTimer {
    @Override
    public long currentTime() {
        return SystemClock.elapsedRealtimeNanos();
    }

    @Override
    public String unitName() {
        return "ns";
    }
}
