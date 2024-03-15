package com.yc.networklib.setting;

public interface INetRequest {

    void registerNetStatusListener(OnNetCallback listener);

    boolean unregisterNetStatusListener(OnNetCallback listener);

    /**
     * 注册网络变化监听
     * 虽然默认网络是大多数应用的唯一相关网络，但某些应用可能希望使用其他可用网络。
     * 为了查找这些网络，应用会构建与其需求匹配的 NetworkRequest。
     * 如果只是接收已扫描网络的通知而不需要主动切换，请使用 registerNetworkCallback() 方法
     */
    void registerNetworkCallback();

    /**
     * 注册默认网络回调
     * 监听系统默认的网络，如果知道默认网络发生变化的时间对应用很重要，它会按这种方式注册默认网络回调
     */
    void registerDefaultNetworkCallback();

    /**
     * 解绑注册监听
     */
    void unregisterNetworkCallback();

    /**
     * 指定某个请求采用指定的网络进行发送
     * 例如在设备连接到 Wi-Fi 网络的情况下尝试启动移动网络
     * 如果您想在检测到合适的网络时主动切换到该网络，请使用 requestNetwork() 方法；
     *
     * @param type 类型
     */
    void requestNetwork(@NetRequestHelper.NetworkType String type);

    /**
     * 销毁时调用
     */
    void destroy();
}
