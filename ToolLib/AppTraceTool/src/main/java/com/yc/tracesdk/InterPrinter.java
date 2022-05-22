package com.yc.tracesdk;

public interface InterPrinter {
    boolean enable();

    void print(String tag, String format, Object... args);
}
