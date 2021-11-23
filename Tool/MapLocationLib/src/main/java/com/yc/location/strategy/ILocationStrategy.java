package com.yc.location.strategy;

import android.os.Handler;

import com.yc.location.bean.ErrorInfo;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.listener.LocationUpdateInternalListener;

/**
 * 根据不同需求要使用的不同的定位策略要共同遵循的接口。
 * 定位策略：比如只为国外app定位的策略，完全使用腾讯SDK定位的策略，滴滴的综合定位策略等等
 */

public interface ILocationStrategy {
    /**
     * 开启定位
     * @param workHandler 定位过程中可能用到的异步线程的handler
     */
    void start(Handler workHandler);

    /**
     * 停止定位
     */
    void stop();

    /**
     * 获取定位
     * @param errInfo 带回错误信息的变量：如果在定位过程中出现错误，赋值到此变量中
     * @return
     */
    DefaultLocation retrieveLocation(ErrorInfo errInfo);

    /**
     * 更新外部最新监听定位频率。暂时只是为了可能去改变系统GPS监听频率、wifi扫描间隔等，以省电
     * @param interval 监听定位间隔
     */
    void updateLocListenInterval(long interval);

    /**
     * 注册内部用于位置相关监听的listener
     * @param locationUpdateInternalListener
     */
    void setInternalLocationListener(LocationUpdateInternalListener locationUpdateInternalListener);

    /**
     * 设置外部监听的listener的信息，便于跟进解决问题等。
     * @param mListenersInfo
     */
    void updateListenersInfo(StringBuilder mListenersInfo);
}
