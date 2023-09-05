package com.yc.crash;

/**
 * 捕获信号，记录日志后的处理方式
 */
public enum NativeHandleMode {
    /**
     * 什么都不做
     */
    DO_NOTHING(0),
    /**
     * 将捕获到的SIGNAL重新抛出
     */
    RAISE_ERROR(1),
    /**
     * 通知Java端的Callback
     */
    NOTICE_CALLBACK(2);

    final int mode;

    NativeHandleMode(int mode) {
        this.mode = mode;
    }
}
