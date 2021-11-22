package com.yc.logging.impl;

import com.yc.logging.Level;
import com.yc.logging.Logger;
import com.yc.logging.LoggerFactory;


public class LoggerBinder {

    public static LoggerBinder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Level getDefaultLevel() {
        return Level.INFO;
    }

    public Logger getLogger(final String name) {
        return LoggerFactory.getLogger(name);
    }

    public Logger getLogger(final String name, String bufferId) {
        return LoggerFactory.getLogger(name, bufferId);
    }

    private static final class SingletonHolder {
        private static final LoggerBinder INSTANCE = new LoggerBinder();
    }
}
