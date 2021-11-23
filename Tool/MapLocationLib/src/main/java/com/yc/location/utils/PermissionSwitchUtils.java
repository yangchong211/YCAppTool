package com.yc.location.utils;

import android.content.Context;

import com.yc.location.constant.Constants;
import com.yc.location.monitor.LocationSensorMonitor;

/**
 * 定位权限、开关相关的工具类
 */

public final class PermissionSwitchUtils {
    /**
     * 定位相关权限、开关的状态
     */
    public enum PermissionSwitchState {
        /**
         * 系统定位开关关闭
         */
        LOCATION_SWITCH_CLOSE("location switch is closed"),
        /**
         * 定位开关中只开了GPS
         */
        LOCATION_SWITCH_GPS_ONLY("only switch on gps to locate"),
        /**
         * 定位开关中只开了网络定位
         */
        LOCATION_SWITCH_NETWORK_ONLY("only switch on network to locate"),
        /**
         * 无法扫描到wifi结果：wifi关闭或wif不允许扫描
         */
        WIFI_SCAN_DISABLED("wifi closed or not allowed scan"),
        /**
         * 定位相关的开关状态正常
         */
        NORMAL_LOC_SWITCH("location related switches are open"),
        /**
         * 无法判断
         */
        UNKNOWN("unkonwn state");
        private String message;
        private PermissionSwitchState(String message) {
            this.message = message;
        }

        /**
         * 返回定位相关权限、开关的状态的说明
         * @return
         */
        public String getMessage() {
            return this.message;
        }
    }

    public static PermissionSwitchState getPermissionSwitchState(Context context) {
        int level = LocationUtils.getLocationSwitchLevel(context);
        switch (level) {
            case Constants.LOCATION_LEVEL_OFF:
                return PermissionSwitchState.LOCATION_SWITCH_CLOSE;
            case Constants.LOCATION_LEVEL_SENSORS_ONLY:
                return PermissionSwitchState.LOCATION_SWITCH_GPS_ONLY;
            case Constants.LOCATION_LEVEL_BATTERY_SAVING:
                return PermissionSwitchState.LOCATION_SWITCH_NETWORK_ONLY;
            case Constants.LOCATION_LEVEL_HIGH_ACCURACY:
                LocationSensorMonitor sensorMonitor = LocationSensorMonitor.getInstance(context);
                if (!sensorMonitor.isWifiEnabled() && !sensorMonitor.isWifiAllowScan()) {
                    return PermissionSwitchState.WIFI_SCAN_DISABLED;
                } else {
                    return PermissionSwitchState.NORMAL_LOC_SWITCH;
                }
            default:
                return PermissionSwitchState.UNKNOWN;
        }
    }
}