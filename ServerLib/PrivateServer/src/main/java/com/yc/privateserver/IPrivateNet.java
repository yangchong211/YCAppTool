package com.yc.privateserver;

/**
 * 收集的个人信息，网络相关的主要有
 * getIpAddress：获取ip的mac地址
 * getSsid：获取网络Wi-Fi的名称
 */
public interface IPrivateNet {

    String getIpAddress();

    String getSsid();

}
