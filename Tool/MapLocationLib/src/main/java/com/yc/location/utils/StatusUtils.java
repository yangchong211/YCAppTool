package com.yc.location.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.monitor.LocationSensorMonitor;

public final class StatusUtils {

    /**
     * 获得手机GPS状态，返回结果会同时包含是否开启、权限是否被禁止，请用{@link DefaultLocation}中gps状态标示与此返回值与运算，得到所有状态。
     * 如返回值为status，则status&DIDILocation.STATUS_GPS_DISABLED = DIDILocation.STATUS_GPS_DISABLED,则代表gps没有打开。
     * 与运算用到的常量范围：{@link DefaultLocation} STATUS_GPS_DISABLED, STATUS_GPS_DENIED。
     * @return 手机GPS状态值。
     */
    public static int getGpsStatus(Context context) {
        int gpsEnableState = 0;
        boolean gpsenable = LocationSensorMonitor.getInstance(context).isGpsEnabled();
        gpsEnableState = !gpsenable ? DefaultLocation.STATUS_GPS_DISABLED : 0;
        int gpsPermissionState = 0;
        if (LocationUtils.checkSystemPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            gpsPermissionState = DefaultLocation.STATUS_GPS_DENIED;
        }

        return gpsEnableState|gpsPermissionState;
    }

    /**
     * 获得手机wifi状态，返回结果会同时包含是否开启、权限是否被禁止、定位开关是否关闭（在android M系统中，此时禁止进行wifi扫描），请用{@link DefaultLocation}中wifi状态标示与此返回值与运算，得到所有状态。
     * 如返回值为status，则status&DIDILocation.STATUS_WIFI_DISABLED = DIDILocation.STATUS_WIFI_DISABLED,则代表wifi没有打开。
     * 与运算用到的常量范围：{@link DefaultLocation} STATUS_WIFI_DISABLED, STATUS_WIFI_DENIED, STATUS_WIFI_LOCATION_SWITCH_OFF。
     * @return 手机wifi状态值。
     */
    public static int getWifiStatus(Context context) {
        boolean wifiEnabled = LocationSensorMonitor.getInstance(context).isWifiEnabled();
        int wifiEnableState = !wifiEnabled ? DefaultLocation.STATUS_WIFI_DISABLED : 0;
        int wifiPermissionState = 0;
        if (!LocationUtils.isLocationPermissionGranted(context)) {
            wifiPermissionState = DefaultLocation.STATUS_WIFI_DENIED;
        }
        int locationSwithState = LocationUtils.isLocationSwitchOff(context) ? DefaultLocation.STATUS_WIFI_LOCATION_SWITCH_OFF : 0;
        return wifiEnableState|wifiPermissionState|locationSwithState;
    }

    /**
     * 获得手机基站状态，返回结果会同时包含是否插入sim卡、权限是否被禁止（对android M以上系统不能使用基站定位），请用{@link DefaultLocation}中cell状态标示与此返回值与运算，得到所有状态。
     * 如返回值为status，则status&DIDILocation.STATUS_CELL_UNAVAILABLE = DIDILocation.STATUS_CELL_UNAVAILABLE,则代表基站信息不可得（没安装sim卡）。
     * 与运算用到的常量范围：{@link DefaultLocation} STATUS_CELL_UNAVAILABLE, STATUS_CELL_DENIED。
     * @return 手机基站（sim卡）状态值。
     */
    public static int getCellStatus(Context context) {
        TelephonyManager manager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();
        boolean hasSim = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
            case TelephonyManager.SIM_STATE_UNKNOWN:
                hasSim = false; // 没有SIM卡
                break;
            default:
                break;
        }
        int cellPermissionState = 0;
        if (!LocationUtils.isLocationPermissionGranted(context) && Build.VERSION.SDK_INT >= 23) {
            cellPermissionState = DefaultLocation.STATUS_CELL_DENIED;
        }
        int simAvailable = !hasSim ? DefaultLocation.STATUS_CELL_UNAVAILABLE : 0;
        return simAvailable|cellPermissionState;
    }


}
