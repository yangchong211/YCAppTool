package com.yc.logging;

import androidx.annotation.RestrictTo;


@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class AbstractLog {
    protected Level logLevel;
    protected AbstractLogger logger;

    abstract public String getContent();

    abstract public String getTag();

    abstract public String getMsg();

    abstract public byte[] getData();

    public Level getLogLevel() {
        return logLevel;
    }
}
