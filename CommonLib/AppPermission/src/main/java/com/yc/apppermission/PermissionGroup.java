package com.yc.apppermission;

import android.Manifest;


/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2016/09/23
 *     desc  : 危险权限列表
 *     revise:
 * </pre>
 */
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
     * 读写权限
     */
    public static final String[] CALENDAR = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
    };

    /**
     * 相机权限
     */
    public static final String[] CAMERA = {
            Manifest.permission.CAMERA,
    };

    /**
     * 联系人权限
     */
    public static final String[] CONTACTS = {
            Manifest.permission.READ_CONTACTS,
            //允许程序写入联系人,但不可读取
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
    };

    /**
     * 位置权限
     */
    public static final String[] LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
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
            Manifest.permission.BODY_SENSORS,
    };

    /**
     * 短信权限
     */
    public static final String[] SMS = {
            //允许程序发送短信
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
    };

    /**
     * 手机权限
     */
    private static final String[] PHONE = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            //允许程序写入（但是不能读）用户的联系人数据
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ADD_VOICEMAIL,
            //允许程序使用SIP视频服务
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
    };

}
