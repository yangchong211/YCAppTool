package com.yc.tracesdk;

class TimerHolder {
    static final InterTimer NANO_TIMER = new NanoTimer();
    static final InterTimer MILLIS_TIMER = new MillisTimer();

}
