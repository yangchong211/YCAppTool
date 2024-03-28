package com.yc.mmaplog.inter;


public interface InterAppender {

    void append(int logLevel, String tag, String msg);

    void flush();

    void release();

}
