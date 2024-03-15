package com.yc.networklib.wifi;

/**
 * Wi-Fi类型
 * OPEN, WEP, WPA, WPA2
 */
public enum WifiModeEnum {
    /**
     * 开放式网络：开放式网络是最简单的WiFi网络类型，不需要密码。
     */
    OPEN,
    /**
     * WEP是一种较老的加密协议，容易被攻击。有线等效加密。
     * 需密码
     */
    WEP,
    /**
     * WPA是较新的加密协议，更加安全。有分成家用的WPA-PSK (Pre-Shared Key)与企业用的WPA-Enterprise版本。
     */
    WPA,
    /**
     * WPA2顾名思义就是WPA的加强版
     * WPA2与WPA的差别在于，它使用更安全的加密技术AES，因此比WPA更难被破解、更安全。
     */
    WPA2,
    /**
     * 连接Wi-Fi后，开启浏览器尝试上网，并强制重导到认证网页要求输入账号密码
     * 然后ACG向RADIUS认证服务器来确认使用者的身分，认证通过才可以自由到其它的网站。
     */
    WEB
}
