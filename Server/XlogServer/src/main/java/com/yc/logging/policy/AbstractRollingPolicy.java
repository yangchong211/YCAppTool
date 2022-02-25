package com.yc.logging.policy;

import java.io.File;
import java.util.Date;


public abstract class AbstractRollingPolicy {

    protected Date mDateInCurrentPeriod = new Date();

    public void setDateInCurrentPeriod(final long now) {
        mDateInCurrentPeriod.setTime(now);
    }

    public abstract boolean isTriggeringEvent(File activeFile);

    public abstract void rollover();

    public abstract String getActiveFile();

    public abstract File getActiveDir();

}
