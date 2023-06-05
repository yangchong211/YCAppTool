package com.yc.appwifilib;

public interface WifiStateListener {
    /**
     * WiFi打开
     */
    void onWifiOpen();

    /**
     * WiFi关闭
     */
    void onWifiClose();

    /**
     * 热点打开
     */
    void onHotpotOpen();

    /**
     * 热点打开异常
     */
    void onHotpotOpenError();

    /**
     * 热点关闭
     */
    void onHotpotClose();

}
