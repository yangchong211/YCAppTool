

package com.yc.ipc.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yangchong on 16/4/7.
 */
public class TimeStampGenerator {
    private static AtomicLong sTimeStamp = new AtomicLong();

    public static long getTimeStamp() {
        return sTimeStamp.incrementAndGet();
    }
}
