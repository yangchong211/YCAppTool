package com.yc.netsettinglib;

public interface INetRequest {

    void registerNetStatusListener(OnNetCallback listener);

    boolean unregisterNetStatusListener(OnNetCallback listener);

    /**
     * 注册网络变化监听
     */
    void registerNetworkCallback();

    /**
     * 注册默认网络回调
     */
    void registerDefaultNetworkCallback();

    /**
     * 解绑注册监听
     */
    void unregisterNetworkCallback();

    /**
     * 指定某个请求采用指定的网络进行发送
     *
     * @param type 类型
     */
    void requestNetwork(@NetRequestHelper.NetworkType String type);

    /**
     * 销毁时调用
     */
    void destroy();
}
