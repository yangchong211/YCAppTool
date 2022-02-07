package com.yc.anrtoollib.watch;

public interface ANRInterceptor {
    /**
     * Called when main thread has froze more time than defined by the timeout.
     *
     * @param duration The minimum time (in ms) the main thread has been frozen (may be more).
     * @return 0 or negative if the ANR should be reported immediately. A positive number of ms to postpone the reporting.
     */
    long intercept(long duration);
}
