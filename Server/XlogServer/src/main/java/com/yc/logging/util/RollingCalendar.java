package com.yc.logging.util;

import androidx.annotation.RestrictTo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class RollingCalendar extends GregorianCalendar {

    /**
     *
     */
    private static final long serialVersionUID = 410984041491053080L;

    public long periodsElapsed(long start, long end) {
        if (start > end) {
            throw new IllegalArgumentException("Start cannot come before end");
        }
        long diff = end - start;
        return diff / TimeUnit.DAYS.toMillis(1);
    }

    public Date getRelativeDate(Date now, int periods) {
        this.setTime(now);
        this.set(Calendar.HOUR_OF_DAY, 0);
        this.set(Calendar.MINUTE, 0);
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        this.add(Calendar.DATE, periods);
        return getTime();
    }

    public Date getNextTriggeringDate(Date now) {
        return getRelativeDate(now, 1);
    }

    public long getNextTriggeringMillis(Date now) {
        return getNextTriggeringDate(now).getTime();
    }
}
