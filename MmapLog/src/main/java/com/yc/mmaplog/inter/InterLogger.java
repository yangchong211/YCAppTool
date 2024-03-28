package com.yc.mmaplog.inter;

public interface InterLogger {

    void println(int priority, String tag, String msg);

    void flush();

    void release();
}
