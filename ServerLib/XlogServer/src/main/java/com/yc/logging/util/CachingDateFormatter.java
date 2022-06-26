package com.yc.logging.util;

import androidx.annotation.RestrictTo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class CachingDateFormatter {

    private long mLastTimeStamp = -1;

    private String mCachedStr = null;

    private final SimpleDateFormat mSimpleDateFormat;

    public CachingDateFormatter(final String pattern) {
        this(pattern, Locale.getDefault());
    }

    public CachingDateFormatter(final String pattern, final Locale locale) {
        this.mSimpleDateFormat = new SimpleDateFormat(pattern, locale);
    }

    public final String format(long now) {
        synchronized (this) {
            if (now != this.mLastTimeStamp) {
                this.mLastTimeStamp = now;
                this.mCachedStr = this.mSimpleDateFormat.format(new Date(now));
            }

            return this.mCachedStr;
        }
    }

    public void setTimeZone(final TimeZone tz) {
        this.mSimpleDateFormat.setTimeZone(tz);
    }
}
