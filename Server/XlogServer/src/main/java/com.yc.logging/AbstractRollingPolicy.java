package com.yc.logging;

import java.io.File;
import java.util.Date;


abstract class AbstractRollingPolicy {

    protected Date mDateInCurrentPeriod = new Date();

    public void setDateInCurrentPeriod(final long now) {
        mDateInCurrentPeriod.setTime(now);
    }

    abstract public boolean isTriggeringEvent(File activeFile);

    abstract public void rollover();

    abstract public String getActiveFile();

    abstract public File getActiveDir();

}
