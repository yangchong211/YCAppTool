package com.yc.alive.manager;

import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RestrictTo;
import android.util.SparseArray;

import com.yc.alive.nova.ka.R;
import com.yc.alive.constant.AliveIntentType;
import com.yc.alive.constant.AliveSettingConst;
import com.yc.alive.model.AliveOptionModel;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveIntentType.CLASS_NAME;
import static com.yc.alive.constant.AliveSettingConst.CLASS_APP_LIST_MIUI;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_SAMSUNG;
import static com.yc.alive.constant.AliveSettingConst.CLASS_FLOAT_WINDOW_OPPO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_SMART;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_VIVO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_MIUI_V9;
import static com.yc.alive.constant.AliveSettingConst.CLASS_WIFI_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_PERMISSION_VIVO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_BATTERY_OPPO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_MIUI;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_OPPO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_SAMSUNG;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_SMART;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_WIFI_OPPO;
import static com.yc.alive.constant.AliveSettingType.TYPE_ACCESSIBILITY_SERVICE;
import static com.yc.alive.constant.AliveSettingType.TYPE_BATTERY;
import static com.yc.alive.constant.AliveSettingType.TYPE_FLOAT_WINDOW;
import static com.yc.alive.constant.AliveSettingType.TYPE_NOTIFICATION;
import static com.yc.alive.constant.AliveSettingType.TYPE_SELF_START;
import static com.yc.alive.constant.AliveSettingType.TYPE_WIFI_NEVER_SLEEP;

/**
 * 生成操作模型
 */
@RestrictTo(LIBRARY)
public class AliveOptionFactory {

    public static SparseArray<AliveOptionModel> createOppoDefault() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();

        AliveOptionModel notification = createNotification();
        notification.notificationRes = R.string.ka_notification_manager;
        notification.notificationAllowRes = R.string.ka_notification_allow;
        notification.intent.routeTip = R.string.ka_notification_route_tip_oppo_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_oppo_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_OPPO;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_OPPO_V3_0;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_oppo_default;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.needPackage = true;
        battery.intent.packageName = PACKAGE_SETTING_BATTERY_OPPO;
        battery.intent.className = CLASS_BATTERY_OPPO_V3_0_0;
        battery.intent.routeTip = R.string.ka_battery_route_tip_oppo_default;
        battery.batteryEnterRes = R.string.ka_battery_enter_protect;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createOppoV3_0() {
        return createOppoDefault();
    }

    public static SparseArray<AliveOptionModel> createOppoV3_0_0() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.intent.type = CLASS_NAME;
        floatWindow.intent.packageName = PACKAGE_SETTING_OPPO;
        floatWindow.intent.className = CLASS_FLOAT_WINDOW_OPPO;

        AliveOptionModel notification = createNotification();
        notification.notificationRes = R.string.ka_notification_manager;
        notification.notificationAllowRes = R.string.ka_notification_allow;
        notification.intent.routeTip = R.string.ka_notification_route_tip_oppo_v3_0_0;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.type = CLASS_NAME;
        wifiNeverSleep.intent.packageName = PACKAGE_SETTING_WIFI_OPPO;
        wifiNeverSleep.intent.className = CLASS_WIFI_OPPO_V3_0_0;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_oppo_v3_0_0;
        wifiNeverSleep.wifiConfigRes = R.string.ka_wifi_advanced_setting;
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_v2;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_OPPO;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_OPPO_V3_0_0;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_oppo_v3_0_0;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.needPackage = true;
        battery.intent.packageName = PACKAGE_SETTING_BATTERY_OPPO;
        battery.intent.className = CLASS_BATTERY_OPPO_V3_0_0;
        battery.intent.routeTip = R.string.ka_battery_route_tip_oppo_v3_0_0;
        battery.batteryEnterRes = R.string.ka_battery_enter_other;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createOppoV3_2() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();

        AliveOptionModel notification = createNotification();
        notification.notificationRes = R.string.ka_notification_manager;
        notification.notificationAllowRes = R.string.ka_notification_allow;
        notification.intent.routeTip = R.string.ka_notification_route_tip_oppo_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_oppo_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_OPPO;
        selfStart.intent.className = AliveSettingConst.CLASS_PERMISSION_OPPO;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_oppo_default;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.needPackage = true;
        battery.intent.packageName = PACKAGE_SETTING_BATTERY_OPPO;
        battery.intent.className = CLASS_BATTERY_OPPO_V3_0_0;
        battery.intent.routeTip = R.string.ka_battery_route_tip_oppo_default;
        battery.batteryEnterRes = R.string.ka_battery_enter_protect;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createOppoV5_0() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();

        AliveOptionModel notification = createNotification();
        notification.notificationRes = R.string.ka_notification_manager;
        notification.notificationAllowRes = R.string.ka_notification_allow;
        notification.intent.routeTip = R.string.ka_notification_route_tip_oppo_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_oppo_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_OPPO;
        selfStart.intent.className = AliveSettingConst.CLASS_PERMISSION_OPPO;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_oppo_default;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.needPackage = true;
        battery.intent.packageName = PACKAGE_SETTING_BATTERY_OPPO;
        battery.intent.className = CLASS_BATTERY_OPPO_V3_0_0;
        battery.intent.routeTip = R.string.ka_battery_route_tip_oppo_default;
        battery.batteryEnterRes = R.string.ka_battery_enter_protect;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createVivoDefault() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();

        AliveOptionModel notification = createNotification();
        notification.notificationAllowRes = R.string.ka_notification_show;
        notification.intent.routeTip = R.string.ka_notification_route_tip_vivo_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.isNotSupport = true;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_vivo_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_VIVO;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_VIVO_V2_5;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_vivo_default;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_VIVO_BATTERY;
        battery.intent.className = AliveSettingConst.CLASS_BATTERY_VIVO_V2_5;
        battery.intent.routeTip = R.string.ka_battery_route_tip_vivo_default;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createVivo2_5() {
        return createVivoDefault();
    }

    public static SparseArray<AliveOptionModel> createVivo3_1() {
        return createVivoDefault();
    }

    public static SparseArray<AliveOptionModel> createVivoV3_0() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_vivo_v3_0;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_v4;
        wifiNeverSleep.wifiKeepLiveAlwaysRes = R.string.ka_wifi_keep_live_always_v3;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_vivo_v3_0;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_VIVO;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_VIVO_V2_5;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_vivo_v3_0;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_VIVO_BATTERY;
        battery.intent.className = AliveSettingConst.CLASS_BATTERY_VIVO_V2_5;
        battery.intent.routeTip = R.string.ka_battery_route_tip_vivo_v3_0;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createVivo4_0() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.intent.type = CLASS_NAME;
        floatWindow.intent.packageName = PACKAGE_PERMISSION_VIVO;
        floatWindow.intent.className = CLASS_PERMISSION_VIVO;

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_vivo_v3_0;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_v4;
        wifiNeverSleep.wifiKeepLiveAlwaysRes = R.string.ka_wifi_keep_live_always_v3;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_vivo_v3_0;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_VIVO;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_VIVO_V2_5;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_vivo_v3_0;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_VIVO_BATTERY;
        battery.intent.className = AliveSettingConst.CLASS_BATTERY_VIVO_V2_5;
        battery.intent.routeTip = R.string.ka_battery_route_tip_vivo_v3_0;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }


    public static SparseArray<AliveOptionModel> createEMUIV15() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_emui_11;

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_emui_15;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_emui_15;
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_emui_v11;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_EMUI_V14;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_emui_15;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI;
        battery.intent.className = AliveSettingConst.CLASS_SELF_START_EMUI_V14;
        battery.intent.routeTip = R.string.ka_battery_route_tip_emui_15;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createEMUIV14() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_emui_11;

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_emui_14;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_emui_14;
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_emui_v11;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_EMUI_V14;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_emui_14;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI;
        battery.intent.className = AliveSettingConst.CLASS_SELF_START_EMUI_V14;
        battery.intent.routeTip = R.string.ka_battery_route_tip_emui_14;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createEMUIV13() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_emui_11;

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_emui_13;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_emui_13;
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_emui_v11;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI_V11;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_EMUI_V11;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_emui_13;
        selfStart.selfStartRes = R.string.ka_self_start_allow;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI_V11;
        battery.intent.className = AliveSettingConst.CLASS_BATTERY_EMUI_V11;
        battery.intent.routeTip = R.string.ka_battery_route_tip_emui_13;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createEMUIV11() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_emui_11;

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_emui_11;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_emui_v11;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_emui_11;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI_V11;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_EMUI_V11;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_emui_11;
        selfStart.selfStartRes = R.string.ka_self_start_allow;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI_V11;
        battery.intent.className = AliveSettingConst.CLASS_BATTERY_EMUI_V11;
        battery.intent.routeTip = R.string.ka_battery_route_tip_emui_11;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createEMUIV10() {
        return createEMUIDefault();
    }

    public static SparseArray<AliveOptionModel> createEMUIDefault() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_emui_11;

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_emui_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_emui_v11;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_emui_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI_V11;
        selfStart.intent.className = AliveSettingConst.CLASS_SELF_START_EMUI_V11;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_emui_default;
        selfStart.selfStartRes = R.string.ka_self_start_allow;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_EMUI_V11;
        battery.intent.className = AliveSettingConst.CLASS_BATTERY_EMUI_V11;
        battery.intent.routeTip = R.string.ka_battery_route_tip_emui_default;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createMIUIDefault() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_miui_11;

        AliveOptionModel notification = createNotification();
        notification.notificationRes = R.string.ka_notification_miui_v9;
        notification.intent.routeTip = R.string.ka_notification_route_tip_miui_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_IP_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_miui_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.className = CLASS_SELF_START_MIUI_V9;
        selfStart.intent.packageName = PACKAGE_SETTING_MIUI;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_miui_default;
        selfStart.selfStartSystemRes = R.string.ka_self_start_system_allow;
        selfStart.selfStartOtherRes = R.string.ka_self_start_other_allow;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_POWER_MIUI;
        battery.intent.className = AliveSettingConst.CLASS_NAME_SETTING_POWER_MIUI_V9;
        battery.intent.routeTip = R.string.ka_battery_route_tip_miui_default;
        battery.batteryNoLimitRes = R.string.ka_battery_no_limit;
        battery.batteryEnterRes = R.string.ka_battery_saving;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0 以上 之前的页面 获取 rootNode == null，这里换一个页面
            battery.intent.type = AliveIntentType.ACTION;
            battery.intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
            battery.intent.needPackage = true;
        }

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createMIUIV9() {
        return createMIUIDefault();
    }

    public static SparseArray<AliveOptionModel> createMIUIV9_MI_5() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_miui_11;

        AliveOptionModel notification = createNotification();
        notification.notificationRes = R.string.ka_notification_miui_v9;
        notification.intent.routeTip = R.string.ka_notification_route_tip_miui_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_IP_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_miui_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.className = CLASS_SELF_START_MIUI_V9;
        selfStart.intent.packageName = PACKAGE_SETTING_MIUI;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_miui_default;
        selfStart.selfStartSystemRes = R.string.ka_self_start_system_allow;
        selfStart.selfStartOtherRes = R.string.ka_self_start_other_allow;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_POWER_MIUI;
        battery.intent.className = AliveSettingConst.CLASS_NAME_SETTING_POWER_MIUI_V9;
        battery.intent.routeTip = R.string.ka_battery_route_tip_miui_default;
        battery.batteryNoLimitRes = R.string.ka_battery_no_limit;
        battery.batteryEnterRes = R.string.ka_battery_saving;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            // 7.0 以上 之前的页面 获取 rootNode == null，这里换一个页面
            battery.intent.type = AliveIntentType.ACTION;
            battery.intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
            battery.intent.needPackage = true;
        }

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createMIUIV9_MI_6() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_miui_11;

        AliveOptionModel notification = createNotification();
        notification.notificationRes = R.string.ka_notification_miui_v9;
        notification.intent.routeTip = R.string.ka_notification_route_tip_miui_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.isNotSupport = true;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_miui_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.className = CLASS_SELF_START_MIUI_V9;
        selfStart.intent.packageName = PACKAGE_SETTING_MIUI;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_miui_default;
        selfStart.selfStartSystemRes = R.string.ka_self_start_system_allow;
        selfStart.selfStartOtherRes = R.string.ka_self_start_other_allow;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_POWER_MIUI;
        battery.intent.className = AliveSettingConst.CLASS_NAME_SETTING_POWER_MIUI_V9;
        battery.intent.routeTip = R.string.ka_battery_route_tip_miui_default;
        battery.batteryNoLimitRes = R.string.ka_battery_no_limit;
        battery.batteryEnterRes = R.string.ka_battery_saving;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0 以上 之前的页面 获取 rootNode == null，这里换一个页面
            battery.intent.type = AliveIntentType.ACTION;
            battery.intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
            battery.intent.needPackage = true;
        }

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createMIUIV9_MIX_2() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app_miui_11;

        AliveOptionModel notification = createNotification();
        notification.intent.type = CLASS_NAME;
        notification.intent.packageName = PACKAGE_SETTING_MIUI;
        notification.intent.className = CLASS_APP_LIST_MIUI;
        notification.intent.routeTip = R.string.ka_notification_route_tip_miui_default;
        notification.notificationRes = R.string.ka_notification_miui_v9;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.isNotSupport = true;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_miui_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.className = CLASS_SELF_START_MIUI_V9;
        selfStart.intent.packageName = PACKAGE_SETTING_MIUI;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_miui_default;
        selfStart.selfStartSystemRes = R.string.ka_self_start_system_allow;
        selfStart.selfStartOtherRes = R.string.ka_self_start_other_allow;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = AliveSettingConst.PACKAGE_SETTING_POWER_MIUI;
        battery.intent.className = AliveSettingConst.CLASS_NAME_SETTING_POWER_MIUI_V9;
        battery.intent.routeTip = R.string.ka_battery_route_tip_miui_default;
        battery.batteryNoLimitRes = R.string.ka_battery_no_limit;
        battery.batteryEnterRes = R.string.ka_battery_saving;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0 以上 之前的页面 获取 rootNode == null，这里换一个页面
            battery.intent.packageName = PACKAGE_SETTING_MIUI;
            battery.intent.className = CLASS_APP_LIST_MIUI;
        }

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createSmartDefault() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.intent.type = CLASS_NAME;
        floatWindow.intent.packageName = PACKAGE_SETTING_SMART;
        floatWindow.intent.className = CLASS_PERMISSION_SMART;

        AliveOptionModel notification = createNotification();
        notification.notificationAllowRes = R.string.ka_notification_push_allow;
        notification.intent.routeTip = R.string.ka_notification_route_tip_smart_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_v5;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_smart_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = PACKAGE_SETTING_SMART;
        selfStart.intent.className = CLASS_PERMISSION_SMART;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_smart_default;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = PACKAGE_SETTING_SMART;
        battery.intent.className = CLASS_PERMISSION_SMART;
        battery.intent.routeTip = R.string.ka_battery_route_tip_smart_default;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createSmartV2_5() {
        return createSmartDefault();
    }

    public static SparseArray<AliveOptionModel> createSmartV4_2() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.intent.type = CLASS_NAME;
        floatWindow.intent.packageName = PACKAGE_SETTING_SMART;
        floatWindow.intent.className = CLASS_PERMISSION_SMART;

        AliveOptionModel notification = createNotification();
        notification.notificationAllowRes = R.string.ka_notification_push_allow;
        notification.intent.routeTip = R.string.ka_notification_route_tip_smart_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_v6;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_smart_v4_2;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.intent.type = CLASS_NAME;
        selfStart.intent.packageName = PACKAGE_SETTING_SMART;
        selfStart.intent.className = CLASS_PERMISSION_SMART;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_smart_v4_2;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = PACKAGE_SETTING_SMART;
        battery.intent.className = CLASS_PERMISSION_SMART;
        battery.intent.routeTip = R.string.ka_battery_route_tip_smart_default;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createSamsungDefault() {
        AliveOptionModel accessibility = createAccessibility();

        AliveOptionModel floatWindow = createFloatWindow();
        floatWindow.floatWindowRes = R.string.ka_float_window_allow_license;

        AliveOptionModel notification = createNotification();
        notification.intent.routeTip = R.string.ka_notification_route_tip_samsung_default;

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.wifiKeepLiveRes = R.string.ka_wifi_keep_live_v5;
        wifiNeverSleep.wifiKeepLiveAlwaysRes = R.string.ka_wifi_keep_live_always_v2;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_samsung_default;

        AliveOptionModel selfStart = new AliveOptionModel(TYPE_SELF_START);
        selfStart.isNotSupport = true;
        selfStart.intent.routeTip = R.string.ka_self_start_route_tip_samsung_default;

        AliveOptionModel battery = new AliveOptionModel(TYPE_BATTERY);
        battery.intent.type = CLASS_NAME;
        battery.intent.packageName = PACKAGE_SETTING_SAMSUNG;
        battery.intent.className = CLASS_BATTERY_SAMSUNG;
        battery.intent.routeTip = R.string.ka_battery_route_tip_samsung_default;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    public static SparseArray<AliveOptionModel> createSamsungV24() {
        return createSamsungDefault();
    }

    public static SparseArray<AliveOptionModel> createDefault() {
        AliveOptionModel accessibility = createAccessibility();
        AliveOptionModel floatWindow = createFloatWindow();
        AliveOptionModel notification = createNotification();
        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        AliveOptionModel selfStart = createSelfStart();
        AliveOptionModel battery = createBattery();
        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }

    private static AliveOptionModel createAccessibility() {
        AliveOptionModel option = new AliveOptionModel(TYPE_ACCESSIBILITY_SERVICE);
        option.intent.type = AliveIntentType.ACTION;
        option.intent.action = Settings.ACTION_ACCESSIBILITY_SETTINGS;
        return option;
    }

    private static AliveOptionModel createFloatWindow() {
        AliveOptionModel option = new AliveOptionModel(TYPE_FLOAT_WINDOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            option.intent.type = AliveIntentType.ACTION;
            option.intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
            option.intent.needPackage = true;
        }
        option.floatWindowRes = R.string.ka_float_window_allow_show_above_other_app;
        return option;
    }

    private static AliveOptionModel createNotification() {
        AliveOptionModel option = new AliveOptionModel(TYPE_NOTIFICATION);
        option.intent.type = AliveIntentType.ACTION;
        option.intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
        option.intent.needPackage = true;
        option.intent.routeTip = R.string.ka_notification_route_tip_default;

        option.notificationRes = R.string.ka_notification;
        option.notificationAllowRes = R.string.ka_notification_allow;
        return option;
    }

    private static AliveOptionModel createWifiNeverSleep() {
        AliveOptionModel option = new AliveOptionModel(TYPE_WIFI_NEVER_SLEEP);
        option.intent.type = AliveIntentType.ACTION;
        option.intent.action = Settings.ACTION_WIFI_IP_SETTINGS;
        option.intent.routeTip = R.string.ka_wifi_route_tip_default;

        option.wifiConfigRes = R.string.ka_wifi_configuration;
        option.wifiKeepLiveRes = R.string.ka_wifi_keep_live;
        option.wifiKeepLiveAlwaysRes = R.string.ka_wifi_keep_live_always;
        return option;
    }

    private static AliveOptionModel createBattery() {
        AliveOptionModel option = new AliveOptionModel(TYPE_BATTERY);
        option.intent.type = AliveIntentType.ACTION;
        option.intent.action = Settings.ACTION_SETTINGS;
        option.intent.routeTip = R.string.ka_battery_route_tip_default;
        return option;
    }

    private static AliveOptionModel createSelfStart() {
        AliveOptionModel option = new AliveOptionModel(TYPE_SELF_START);
        option.intent.type = AliveIntentType.ACTION;
        option.intent.action = Settings.ACTION_SETTINGS;
        option.intent.routeTip = R.string.ka_self_start_route_tip_default;
        return option;
    }

    private static SparseArray<AliveOptionModel> createOptionArray(AliveOptionModel...options) {
        SparseArray<AliveOptionModel> array = new SparseArray<>(options.length);
        for (AliveOptionModel option : options) {
            array.put(option.type, option);
        }
        return array;
    }

    public static SparseArray<AliveOptionModel> createSunMi711() {
        AliveOptionModel accessibility = createAccessibility();
        AliveOptionModel floatWindow = createFloatWindow();
        AliveOptionModel notification = createNotification();

        AliveOptionModel wifiNeverSleep = createWifiNeverSleep();
        wifiNeverSleep.intent.action = Settings.ACTION_WIFI_SETTINGS;
        wifiNeverSleep.intent.routeTip = R.string.ka_wifi_route_tip_sunmi_default;

        AliveOptionModel selfStart = createSelfStart();

        AliveOptionModel battery = createBattery();
        battery.intent.action = Settings.ACTION_SETTINGS;
        battery.intent.routeTip = R.string.ka_battery_route_tip_sunmi_default;

        return createOptionArray(accessibility, floatWindow, notification, wifiNeverSleep, selfStart, battery);
    }
}
