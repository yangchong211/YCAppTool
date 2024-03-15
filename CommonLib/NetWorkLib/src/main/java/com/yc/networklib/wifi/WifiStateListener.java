package com.yc.networklib.wifi;

import android.net.wifi.ScanResult;

import java.util.List;

public interface WifiStateListener {

    /**
     * WIFI开关的回调
     *
     * @param enabled true 可用 false 不可用
     */
    void onWifiEnabled(boolean enabled);

    /**
     * 热点打开的回调
     *
     * @param enabled true 打开 false 关闭
     */
    void onHotpotEnabled(boolean enabled);

    /**
     * WIFI连接状态的回调
     *
     * @param SSID    热点名
     * @param enabled true表示WIFI连接成功的回调 false表示WIFI连接失败的回调
     */
    void onWiFiConnectState(String SSID, boolean enabled);

    /**
     * 扫描结果的回调
     *
     * @param scanResults 扫描结果
     */
    void onScanResults(boolean success, List<ScanResult> scanResults);
}
