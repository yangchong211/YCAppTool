package com.yc.logging.log;

import androidx.annotation.RestrictTo;

import com.yc.logging.constant.Level;
import com.yc.logging.logger.AbstractLogger;
import com.yc.logging.LoggerService;

import java.util.Map;


@RestrictTo(RestrictTo.Scope.LIBRARY)
public class FormatLog extends AbstractLog {

    private String event;
    private Map<?, ?> map;

    public FormatLog(AbstractLogger logger, Level level, String event, Map<?, ?> map) {
        this.logger = logger;
        this.logLevel = level;
        this.event = event;
        this.map = map;
    }

    @Override
    public String getContent() {
        if (logLevel.level < LoggerService.getInstance().getDefaultLevel().level) {
            return null;
        }
        return logger.formatEvent(logLevel, event, map);
    }

    @Override
    public String getMsg() {
        return getContent();
    }

    @Override
    public String getTag() {
        return event;
    }

    @Override
    public byte[] getData() {
        throw new UnsupportedOperationException();
    }
}
