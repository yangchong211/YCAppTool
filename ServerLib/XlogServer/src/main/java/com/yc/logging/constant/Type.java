package com.yc.logging.constant;

public enum Type {
    /**
     * logback
     */
    LOGBACK("logback"),
    /**
     * binary
     */
    BINARY("binary");

    public final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
