package com.yc.alive.model;

import androidx.annotation.RestrictTo;

import com.yc.alive.constant.AliveRomConst.SupportType.TYPE;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 设备信息
 * <p>
 *  2018/5/16.
 */
@RestrictTo(LIBRARY)
public class AliveDeviceModel {

    // 生产商
    public String manufacturer;
    // 型号
    public String model;
    // sdk
    public int sdkInt;
    // sdk
    public String sdkStr;

    public String romVersionCode;
    public String romVersionName;

    @TYPE
    public int type;

    // oppo
    public String romOppoVersionName;
    public int romOppoVersionCode;

    // vivo
    public String romVivoVersionName;
    public String romVivoVersionCode;

    // emui
    public String romEmuiVersionName;
    public String romEmuiVersionCode;

    // miui
    public String romMiuiVersionName;
    public int romMiuiVersionCode;

    // samsung
    public String romSamsungVersionName;
    public int romSamsungVersionCode;

    // smartisan
    public String romSmartisanVersionName;

    @Override
    public String toString() {
        return "KADeviceModel{" + "manufacturer='" + manufacturer + '\'' + ", model='" + model + '\'' + ", sdkInt="
            + sdkInt + ", sdkStr='" + sdkStr + '\'' + ", romVersionCode=" + romVersionCode + ", romVersionName='"
            + romVersionName + '\'' + ", type=" + type + '}';
    }

    public String print() {
        return "KADeviceModel{" + "manufacturer='" + manufacturer + '\'' + ", model='" + model + '\'' + ", sdkInt="
            + sdkInt + ", sdkStr='" + sdkStr + '\'' + ", romVersionCode='" + romVersionCode + '\''
            + ", romVersionName='" + romVersionName + '\'' + ", type=" + type + ", romOppoVersionName='"
            + romOppoVersionName + '\'' + ", romOppoVersionCode=" + romOppoVersionCode + ", romVivoVersionName='"
            + romVivoVersionName + '\'' + ", romVivoVersionCode='" + romVivoVersionCode + '\''
            + ", romEmuiVersionName='" + romEmuiVersionName + '\'' + ", romEmuiVersionCode='" + romEmuiVersionCode
            + '\'' + ", romMiuiVersionName='" + romMiuiVersionName + '\'' + ", romMiuiVersionCode=" + romMiuiVersionCode
            + ", romSamsungVersionName='" + romSamsungVersionName + '\'' + ", romSamsungVersionCode="
            + romSamsungVersionCode + ", romSmartisanVersionName='" + romSmartisanVersionName + '}';
    }
}
