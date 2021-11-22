package com.yc.logging;

public enum Type {
    LOGBACK("logback"),
    BINARY("binary");

    public final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
