package com.yc.toolutils.permission;

import android.Manifest;


/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2016/09/23
 *     desc  : 危险权限列表
 *     revise: 可以直接使用{@link PermissionConstants}
 * </pre>
 */
@Deprecated()
public final class PermissionGroup {

    /**
     * 读写权限
     */
    public static final String[] EXTERNAL_STORAGE = {
            //允许程序可以读取设备外部存储空间
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //允许程序写入外部存储,如SD卡上写文件
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    /**
     * 日历权限
     */
    public static final String[] CALENDAR = {
            //允许程序读取用户的日程信息
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
    };

    /**
     * 相机权限
     */
    public static final String[] CAMERA = {
            //允许程序访问摄像头进行拍照
            Manifest.permission.CAMERA,
    };

    /**
     * 联系人权限
     */
    public static final String[] CONTACTS = {
            //允许程序访问联系人通讯录信息
            Manifest.permission.READ_CONTACTS,
            //允许程序写入联系人,但不可读取
            Manifest.permission.WRITE_CONTACTS,
            //允许程序访问账户Gmail列表
            Manifest.permission.GET_ACCOUNTS,
    };

    /**
     * 位置权限
     */
    public static final String[] LOCATION = {
            //允许程序通过GPS芯片接收卫星的定位信息
            Manifest.permission.ACCESS_FINE_LOCATION,
            //允许程序通过WiFi或移动基站的方式获取用户错略的经纬度信息
            Manifest.permission.ACCESS_COARSE_LOCATION,
            //允许应用程序在后台访问位置
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    };

    /**
     * 麦克风权限
     */
    public static final String[] MICROPHONE = {
            //允许程序录制声音通过手机或耳机的麦克
            Manifest.permission.RECORD_AUDIO,
    };

    /**
     * 传感器权限
     */
    public static final String[] SENSORS = {
            //允许应用程序访问用户用来测量身体内部情况的传感器数据，例如心率
            Manifest.permission.BODY_SENSORS,
    };

    /**
     * 短信权限
     */
    public static final String[] SMS = {
            //允许程序发送短信
            Manifest.permission.SEND_SMS,
            //允许程序接收短信
            Manifest.permission.RECEIVE_SMS,
            //允许程序读取短信内容
            Manifest.permission.READ_SMS,
            //允许程序接收WAP PUSH信息
            Manifest.permission.RECEIVE_WAP_PUSH,
            //允许程序接收彩信
            Manifest.permission.RECEIVE_MMS,
    };

    /**
     * 手机权限
     */
    public static final String[] PHONE = {
            //允许程序访问电话状态
            Manifest.permission.READ_PHONE_STATE,
            //允许程序从非系统拨号器里拨打电话
            Manifest.permission.CALL_PHONE,
    };


    /**
     * 手机权限
     */
    public static final String[] PHONE_ALL = {
            //允许程序访问电话状态
            Manifest.permission.READ_PHONE_STATE,
            //允许程序从非系统拨号器里拨打电话
            Manifest.permission.CALL_PHONE,
            //允许程序读取设备的电话号码
            Manifest.permission.READ_PHONE_NUMBERS,
            //允许程序读入用户的联系人数据
            Manifest.permission.READ_CALL_LOG,
            //允许程序写入（但是不能读）用户的联系人数据
            Manifest.permission.WRITE_CALL_LOG,
            //允许程序添加语音邮件系统
            Manifest.permission.ADD_VOICEMAIL,
            //允许程序使用SIP视频服务
            Manifest.permission.USE_SIP,
            //允许程序监视，修改或放弃播出电话
            Manifest.permission.PROCESS_OUTGOING_CALLS,
    };

}
