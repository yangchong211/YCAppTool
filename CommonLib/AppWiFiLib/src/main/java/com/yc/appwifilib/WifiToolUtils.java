package com.yc.appwifilib;

import static com.yc.appwifilib.WifiHelper.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.yc.appwifilib.wifilibrary.SecurityModeEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class WifiToolUtils {

    /**
     * 请求修改系统设置Code
     */
    public static final int REQUEST_WRITE_SETTING_CODE = 1001;

    /**
     * 查看授权情况, 开启热点需要申请系统设置修改权限，如有必要，可提前申请
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestWriteSettings(Activity activity) {
        if (isGrantedWriteSettings(activity)) {
            Log.d(TAG, "已授权修改系统设置权限");
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_WRITE_SETTING_CODE);
    }


    /**
     * 返回应用程序是否可以修改系统设置
     *
     * @return {@code true}: yes
     * {@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isGrantedWriteSettings(Context context) {
        return Settings.System.canWrite(context);
    }

    /**
     * 获取Wi-Fi信号强度
     *
     * @param wifiRssi 信号强度
     * @return 信号强度
     */
    public static String getWifiLevel(int wifiRssi) {
        //获取wifi信号强度
        if (wifiRssi > -50 && wifiRssi < 0) {
            //最强
            return "最强";
        } else if (wifiRssi > -70 && wifiRssi < -50) {
            //较强
            return "较强";
        } else if (wifiRssi > -80 && wifiRssi < -70) {
            //较弱
            return "较弱";
        } else if (wifiRssi > -100 && wifiRssi < -80) {
            //微弱
            return "微弱";
        } else {
            return "微弱";
        }
    }

    /**
     * 获取WIFI的加密方式
     *
     * @param scanResult WIFI信息
     * @return 加密方式
     */
    public static WifiModeEnum getSecurityMode(@NonNull ScanResult scanResult) {
        String capabilities = scanResult.capabilities;
        if (capabilities.contains("WPA")) {
            return WifiModeEnum.WPA;
        } else if (capabilities.contains("WEP")) {
            return WifiModeEnum.WEP;
            //        } else if (capabilities.contains("EAP")) {
            //            return SecurityMode.WEP;
        } else if (capabilities.contains("EAP")) {
            return WifiModeEnum.WEP;
        } else {
            // 没有加密
            return WifiModeEnum.OPEN;
        }
    }


    /**
     * 添加双引号
     * 这里需要注意的是：
     * WifiConfiguration里封装的wifi的SSID是包含双引号的，而ScanResult的SSID是不包含双引号的，这里比较时需要注意！
     *
     * @param text 待处理字符串
     * @return 处理后字符串
     */
    public static String addDoubleQuotation(String text) {
        //直接判断equals(ssid)肯定是不存在的，因为“XXX_WIFI”才是配置中的SSID，它外面包了双引号
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return "\"" + text + "\"";
    }


    /**
     * 利用HashSet不能添加重复数据的特性
     *
     * @param list list集合
     */
    public static List<ScanResult> removeDuplicate(List<ScanResult> list) {
        HashSet<ScanResult> set = new HashSet<>(list.size());
        List<ScanResult> result = new ArrayList<>(list.size());
        for (ScanResult item : list) {
            if (set.add(item)) {
                result.add(item);
            }
        }
        list.clear();
        list.addAll(result);
        return list;
    }

    /**
     * 将ip数值，转为字符串
     *
     * @param paramInt ip整数
     * @return 字符串ip地址
     */
    public static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
                + (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
    }


}
