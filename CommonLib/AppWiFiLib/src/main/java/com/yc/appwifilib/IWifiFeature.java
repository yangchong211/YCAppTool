package com.yc.appwifilib;

import androidx.annotation.NonNull;

public interface IWifiFeature {

    /**
     * 打开Wi-Fi
     * @return      true表示打开Wi-Fi
     */
    boolean openWifi();


    /**
     * 关闭Wi-Fi
     * @return      true表示关闭Wi-Fi
     */
    boolean closeWifi();

    /**
     * 是否有某个Wi-Fi
     * @param ssid          Wi-Fi名称ssid
     * @return          true表示有该Wi-Fi
     */
    boolean isHaveWifi(@NonNull String ssid);
}
