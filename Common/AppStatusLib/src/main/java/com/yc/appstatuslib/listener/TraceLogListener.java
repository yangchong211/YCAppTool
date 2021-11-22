package com.yc.appstatuslib.listener;

import com.yc.appstatuslib.info.CollectionInfo;

public interface TraceLogListener {
    /**
     * 日志监听
     * @param info                          info信息
     */
    void log(CollectionInfo info);
}
