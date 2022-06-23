package com.yc.monitortimelib;

public interface OnMonitorListener {
    /**
     * 回调
     * @param processName       进程名称
     * @param result            返回数据
     */
    void onMonitorResult(String processName, String result);
}
