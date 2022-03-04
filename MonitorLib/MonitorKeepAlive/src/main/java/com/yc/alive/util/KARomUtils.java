package com.yc.alive.util;

import androidx.annotation.RestrictTo;

import com.yc.alive.model.AliveDeviceModel;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.util.KASystemPropertiesUtils.UNKNOWN;

/**
 * Rom 工具类
 */
@RestrictTo(LIBRARY)
public class KARomUtils {

    private static final String TAG = "KARomUtils";

    public static boolean isOppo(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }
        if (KAStringUtils.isEmpty(device.romOppoVersionName)) {
            return false;
        }
        if (device.romOppoVersionName.equals(UNKNOWN)) {
            return false;
        }

        return true;
    }

    public static boolean isVivo(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }
        if (KAStringUtils.isEmpty(device.romVivoVersionName)) {
            return false;
        }
        if (device.romVivoVersionName.equals(UNKNOWN)) {
            return false;
        }

        return true;
    }

    public static boolean isEmui(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }
        if (KAStringUtils.isEmpty(device.romEmuiVersionName)) {
            return false;
        }
        if (device.romEmuiVersionName.equals(UNKNOWN)) {
            return false;
        }

        return true;
    }

    public static boolean isMiui(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }
        if (KAStringUtils.isEmpty(device.romMiuiVersionName)) {
            return false;
        }
        if (device.romMiuiVersionName.equals(UNKNOWN)) {
            return false;
        }

        return true;
    }

    public static boolean isSamsung(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }
        if (KAStringUtils.isEmpty(device.manufacturer)) {
            return false;
        }
        if (device.manufacturer.contains("samsung")) {
            return true;
        }

        return false;
    }

    public static boolean isMeizu(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }

        return false;
    }

    public static boolean isSmart(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }
        if (KAStringUtils.isEmpty(device.romSmartisanVersionName)) {
            return false;
        }
        if (device.romSmartisanVersionName.equals(UNKNOWN)) {
            return false;
        }

        return true;
    }

    public static boolean isSunMi(AliveDeviceModel device) {
        if (device == null) {
            return false;
        }
        if (device.manufacturer.contains("SUNMI")) {
            return true;
        }
        return false;
    }
}
