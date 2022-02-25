package com.yc.logging.log;

import androidx.annotation.RestrictTo;

import com.yc.logging.constant.Level;
import com.yc.logging.logger.AbstractLogger;


@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class AbstractLog {

    public Level logLevel;

    public AbstractLogger logger;

    public abstract String getContent();

    public abstract String getTag();

    public abstract String getMsg();

    public abstract byte[] getData();

    public Level getLogLevel() {
        return logLevel;
    }
}
