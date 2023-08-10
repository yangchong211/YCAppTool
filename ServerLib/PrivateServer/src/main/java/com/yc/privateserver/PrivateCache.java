package com.yc.privateserver;

/**
 * 建议APP需遵循合理、正当、必要的原则收集用户个人信息，具体为：
 * 1.收集的个人信息的类型应与实现产品或服务的业务功能有直接关联，直接关联是指没有该信息的参与，产品或服务的功能无法实现；
 * 2.自动采集个人信息的频率应是实现产品或服务的业务功能所必需的最低频率；
 * 3.间接获取个人信息的数量应是实现产品或服务的业务功能所必需的最少数量。
 */
public final class PrivateCache {

    /**
     * imei     国际移动设备标识
     * 通常所说的手机序列号，用于在手机网络中识别每一部独立的手机，是国际上公认的手机标志序号，相当于移动电话的身份证。
     * IMEI由15-17位数字组成，每位数字仅使用0~9的数字，IMEI不一定是15位
     * 01为美国CTIA，35为英国BABT，86为中国TAF
     * 由15位数字组成，前6位(TAC)是型号核准号码，代表手机类型。接着2位(FAC)是最后装配号，代表产地。
     * 后6位(SNR)是串号，代表生产顺序号。最后1位(SP)是检验码
     */
    public static volatile String IMEI = null;
    /**
     * android_id，Android设备id
     * ANDROID_ID是Android系统第一次启动时产生的一个64bit（16BYTES）数，如果设备被wipe还原后，该ID将被重置（变化）。
     * 16位16进制
     * ANDROID_ID缺点：
     * ①.设备刷机wipe数据或恢复出厂设置时ANDROID_ID值会被重置。
     * ②.现在网上已有修改设备ANDROID_ID值的APP应用。
     * ③.某些厂商定制的系统可能会导致不同的设备产生相同的ANDROID_ID。
     * ④.某些厂商定制的系统可能导致设备返回ANDROID_ID值为空。
     * ⑤.CDMA设备，ANDROID_ID和DeviceId返回的值相同
     */
    public static volatile String ANDROID_ID = null;
    /**
     * 手机运营商
     */
    public static volatile String PROVIDER_NAME = null;
    /**
     * sim 卡的运营商Id
     */
    public static volatile String OPERATOR_ID = null;
    /**
     * MEID
     * (Mobile Equipment Identifier) 移动设备识别码是CDMA手机的身份识别码，也是每台CDMA手机或通讯平板唯一的识别码。
     * 由15位16进制数字组成(一般使用前14位)，前8位是生产商编号，后6位是串号，最后1位是检验码
     */
    public static volatile String MEID = null;
    public static volatile String Imei1 = null;
    public static volatile String Imei2 = null;
    /**
     * 设备sn号码
     */
    public static volatile String SN = null;
    /**
     * sim 卡的运营商名称
     */
    public static volatile String OPERATOR_NAME = null;
    /**
     * IMSI     国际移动用户识别码的简称
     * 它是在公众陆地移动电话网（PLMN）中用于唯一识别移动用户的一个号码。在GSM网络，这个号码通常被存放在SIM卡中
     * 总长度16位，有MCC+MNC+MSIN三部分组成。
     * MCC：移动国家码，三位数字，如中国460
     * MNC：移动网号，两个数字，如中国移动：00，联通：01
     * MSIN：移动客户识别号
     */
    public static volatile String IMSI = null;
    /**
     * sim卡，卡的运营商Id
     */
    public static volatile String SIM_OPERATOR = null;
    /**
     * mac 地址
     * 6组16进制数，比如
     */
    public static volatile String MAC = null;
    /**
     * DEVICE_ID是设备ID标识，用于唯一标识设备，这个ID似乎并非是独立的一串数字，而会由于终端的硬件配置不同，所取到的结果不同。
     * 比如GSM手机DEVICE_ID可能是IMEI号，CDMA手机可能是MEID，不带MODEM的手机可能会返回NULL，也可能返回其它唯一值
     */
    public static volatile String DEVICE_ID = null;
}
