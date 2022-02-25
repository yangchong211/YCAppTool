package com.yc.logging.constant;

import com.yc.logging.annotation.KeepSource;

@KeepSource
public enum Level {

    TRACE(1, "T"),

    DEBUG(2, "D"),

    INFO(3, "I"),

    WARN(4, "W"),

    ERROR(5, "E");

    public final int level;
    public final String name;

    Level(final int level, String name) {
        this.level = level;
        this.name = name;
    }
}
