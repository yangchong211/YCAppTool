package com.yc.location.listener;


import com.yc.location.bean.ErrorInfo;
import com.yc.location.bean.DefaultLocation;

public interface LocationUpdateInternalListener {

    /**
     * 获取到位置的回调
     * @param loc 位置
     */
    void onLocationUpdate(DefaultLocation loc, long intervalCount);

    /**
     * 出错回调
     * @param errInfo 出错信息
     * @param deltaTime 本次loop开始到出错经历的时间（暂时只为统计）
     */
    void onLocationErr(ErrorInfo errInfo, int deltaTime);

    /**
     * 定位状态回调。定位相关模块的状态变化等。
     * @param name
     * @param status
     */
    void onStatusUpdate(String name, int status);
}