package com.yc.privateserver;

/**
 * 收集的个人信息，设备相关的主要有
 * getDeviceId：获取设备id
 * getImei：获取imei号
 * getAndroidId：获取android_id
 * getSN：获取SERIAL号码
 * getSimOperator：获取sim卡id
 */
public interface IPrivateDevice {

    String getDeviceId();

    String getDeviceId(int index);

    String getImei();

    String getImei(int index);

    String getMeid();

    String getMeid(int index);

    String getSubscriberId();

    String getSimOperatorName();

    String getSimOperator();

    String getSimSerialNumber();

    String getSN1();

    String getSN2();

    String getSN3();

    String getAndroidId();

    String getMacAddress();
}
