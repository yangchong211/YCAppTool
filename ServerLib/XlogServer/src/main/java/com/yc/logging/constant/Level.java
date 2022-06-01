package com.yc.logging.constant;


public enum Level {

    /**
     * t
     */
    TRACE(1, "T"),

    /**
     * d
     */
    DEBUG(2, "D"),

    /**
     * i
     */
    INFO(3, "I"),

    /**
     * w
     */
    WARN(4, "W"),

    /**
     * e
     */
    ERROR(5, "E");

    public final int level;
    public final String name;

    Level(final int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static Level getLevel(int level) {
        switch(level) {
            case 2:
                return DEBUG;
            case 3:
                return INFO;
            case 4:
                return WARN;
            case 5:
                return ERROR;
            default:
                return TRACE;
        }
    }
}
