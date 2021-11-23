package com.yc.location.listener;

import com.yc.location.bean.DefaultLocation;

/**
 * 定位模块内部使用的监听定位相关硬件模块的状态变化
 */

public interface StatusUpdateListener {
    /**
     * 手机GPS等状态变化时的回调函数。
     * Name：定位相关模块，取值{@link DefaultLocation} STATUS_CELL, STATUS_WIFI, STATUS_GPS.
     * Status：当前状态 {@link DefaultLocation}
     * Desc：状态描述
     */
    void onStatusUpdate(String name, int status, String desc);
}
