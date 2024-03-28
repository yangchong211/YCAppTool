package com.yc.mmaplog.inter;

public interface Formatter {
    String format(int logLevel, String tag, String msg);
}
