package com.yc.logging;

import com.yc.logging.constant.Level;
import com.yc.logging.logger.Logger;


public class LoggerService {

    public static LoggerService getInstance() {
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
        private static final LoggerService INSTANCE = new LoggerService();
    }
}
