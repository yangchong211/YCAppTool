package com.yc.activitymanager;

/**
 * 监听app退出
 */
public interface OnExitListener {

    /**
     * 资源释放
     */
    void deInit();

}
