package com.yc.networklib.wifi;

import androidx.annotation.NonNull;

interface IWifiFeature {

    /**
     * 判断Wi-Fi是否打开
     *
     * @return true表示打开Wi-Fi
     */
    boolean isWifiEnable();

    /**
     * 打开Wi-Fi
     *
     * @return true表示打开Wi-Fi
     */
    boolean openWifi();

    /**
     * 关闭Wi-Fi
     *
     * @return true表示关闭Wi-Fi
     */
    boolean closeWifi();

    /**
     * 连接加密WEP网络：使用ssid + pwd连接
     *
     * @param ssid Wi-Fi名称
     * @param pwd  Wi-Fi密码
     * @return true表示连接上Wi-Fi
     */
    boolean connect(String ssid, String pwd);

    /**
     * 连接到WPA2网络：使用ssid + pwd连接
     *
     * @param ssid Wi-Fi名称
     * @param pwd  Wi-Fi密码
     * @return true表示连接上Wi-Fi
     */
    boolean connectWpa(String ssid, String pwd);

    /**
     * 连接开放网络：使用ssid
     *
     * @param ssid Wi-Fi名称
     * @return true表示连接上Wi-Fi
     */
    boolean connect(String ssid);

    /**
     * 是否有某个Wi-Fi
     *
     * @param ssid Wi-Fi名称ssid
     * @return true表示有该Wi-Fi
     */
    boolean isHaveWifi(@NonNull String ssid);

    /**
     * 是否连接着指定WiFi,通过已连接的SSID判断
     *
     * @param ssid Wi-Fi名称ssid
     * @return true表示有该Wi-Fi已经连接
     */
    boolean isConnectedTargetSsid(String ssid);

    /**
     * 开启Wi-Fi扫描
     *
     * @return true表示开启扫描成功
     */
    boolean startScan();

    /**
     * 获取当前WiFi名称
     *
     * @return 返回不含双引号的SSID
     */
    String getSsid();

    /**
     * 注册Wi-Fi广播监听
     *
     * @param listener 监听listener
     */
    void registerWifiListener(WifiStateListener listener);

    /**
     * 解绑Wi-Fi广播监听
     *
     * @param listener 监听listener
     */
    boolean unregisterWifiListener(WifiStateListener listener);

    /**
     * 资源释放
     */
    void release();
}
