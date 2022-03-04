package com.yc.alive.constant;


import androidx.annotation.RestrictTo;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 设置常量
 */
@RestrictTo(LIBRARY)
public class AliveSettingConst {

    public static final String PACKAGE_SETTING = "com.android.settings";
    public static final String PACKAGE_SETTING_2 = "com.android.systemui";
    public static final String PACKAGE_SETTING_WIFI = "com.android.wifisettings";
    // 悬浮窗
    public static final String CLASS_FLOAT_WINDOW = "com.android.settings.Settings$AppDrawOverlaySettingsActivity";
    // 应用信息
    public static final String CLASS_APPLICATION_INFO = "com.android.settings.applications.InstalledAppDetailsTop";
    public static final String CLASS_APPLICATION_INFO_V2 = "com.android.settings.applications.InstalledAppDetailsActivity";
    // WIFI 设置（主页面）
    public static final String CLASS_WIFI_SETTING = "com.android.settings.Settings$WifiSettingsActivity";
    public static final String CLASS_WIFI_SETTING_V2 = "com.android.settings.wifi.WifiSettingsActivity";

    // WIFI 设置 - 高级界面 - 有休眠设置
    public static final String CLASS_WIFI_ADVANCED_SETTING = "com.android.settings.Settings$AdvancedWifiSettingsActivity";
    public static final String CLASS_WIFI_ADVANCED_SETTING_V2 = "com.android.wifisettings.Settings$AdvancedWifiSettingsActivity";
    public static final String CLASS_WIFI_ADVANCED_SETTING_V3 = "com.android.settings.Settings$ConfigureWifiSettingsActivity";

    // 设置二级页面
    public static final String CLASS_SUB_SETTING = "com.android.settings.SubSettings";
    // 弹窗
    public static final String CLASS_DIALOG = "android.app.AlertDialog";
    public static final String CLASS_DIALOG_V2 = "android.app.Dialog";

    ///////////////////////////////////////////////////////////////////////////
    // OPPO
    ///////////////////////////////////////////////////////////////////////////
    public static final String PACKAGE_SETTING_OPPO = "com.coloros.safecenter";
    public static final String PACKAGE_SETTING_NOTIFICATION_OPPO = "com.coloros.notificationmanager";
    public static final String PACKAGE_SETTING_WIFI_OPPO = "com.coloros.wirelesssettings";
    public static final String PACKAGE_SETTING_BATTERY_OPPO = "com.coloros.oppoguardelf";

    // 悬浮窗
    public static final String CLASS_FLOAT_WINDOW_OPPO = "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity";
    public static final String CLASS_FLOAT_WINDOW_OPPO_2 = "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity";
    public static final String CLASS_NOTIFICATION_OPPO = "com.coloros.notificationmanager.AppDetailPreferenceActivity";
    public static final String CLASS_NOTIFICATION_OPPO_2 = "com.coloros.notificationmanager.AppNotificationSettingsActivity";
    public static final String CLASS_WIFI_OPPO = "com.coloros.wirelesssettings.wifi.OppoWifiSettingsActivity";
    public static final String CLASS_WIFI_OPPO_2 = "com.coloros.wirelesssettings.OppoWirelessSettingsActivity";
    public static final String CLASS_PERMISSION_OPPO = "com.coloros.privacypermissionsentry.PermissionTopActivity";
    public static final String CLASS_SELF_START_OPPO = "com.coloros.safecenter.permission.startup.StartupAppListActivity";
    public static final String CLASS_SELF_START_OPPO_2 = "com.coloros.safecenter.startupapp.StartupAppListActivity";
    public static final String CLASS_BATTERY_OPPO = "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity";
    public static final String CLASS_BATTERY_LIST_OPPO = "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity";
    public static final String CLASS_BATTERY_APP_OPPO = "com.coloros.powermanager.fuelgaue.PowerAppsBgSetting";

    public static final String CLASS_FLOAT_WINDOW_OPPO_V3_0_0 = CLASS_FLOAT_WINDOW_OPPO;
    public static final String CLASS_FLOAT_WINDOW_OPPO_V3_0 = CLASS_FLOAT_WINDOW_OPPO_2;
    public static final String CLASS_NOTIFICATION_OPPO_V3_0_0 = CLASS_NOTIFICATION_OPPO;
    public static final String CLASS_NOTIFICATION_OPPO_V5_0 = CLASS_NOTIFICATION_OPPO_2;
    public static final String CLASS_WIFI_OPPO_V3_0_0 = CLASS_WIFI_OPPO;
    public static final String CLASS_SELF_START_OPPO_V3_0_0 = CLASS_SELF_START_OPPO;
    public static final String CLASS_SELF_START_OPPO_V3_0 = CLASS_SELF_START_OPPO_2;
    public static final String CLASS_BATTERY_OPPO_V3_0_0 = CLASS_BATTERY_OPPO;
    public static final String CLASS_BATTERY_LIST_OPPO_V3_0_0 = CLASS_BATTERY_LIST_OPPO;
    public static final String CLASS_BATTERY_APP_OPPO_V3_0_0 = CLASS_BATTERY_APP_OPPO;

    public static final String ID_FLOAT_WINDOW_LIST_ID_OPPO = "android:id/list";
    public static final String ID_WIFI_LIST_ID_OPPO = "android:id/list";
    public static final String ID_SELF_START_LIST_ID_OPPO = "android:id/list";
    public static final String ID_BATTERY_LIST_ID_OPPO = "android:id/list";

    ///////////////////////////////////////////////////////////////////////////
    // VIVO
    ///////////////////////////////////////////////////////////////////////////
    public static final String PACKAGE_SETTING_VIVO = "com.iqoo.secure";
    public static final String PACKAGE_SETTING_VIVO_BATTERY = "com.vivo.abe";
    public static final String PACKAGE_SETTING_VIVO_BATTERY_2 = "com.vivo.abeui";
    public static final String PACKAGE_PERMISSION_VIVO = "com.vivo.permissionmanager";
    public static final String CLASS_PERMISSION_VIVO = "com.vivo.permissionmanager.activity.PurviewTabActivity";
    public static final String CLASS_PERMISSION_APP_VIVO = "com.android.packageinstaller.permission.ui.ManagePermissionsActivity";
    public static final String CLASS_PERMISSION_APP_MANAGER_VIVO = "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity";
    // 自启动设置页面
    public static final String CLASS_SELF_START_VIVO = "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager";
    public static final String CLASS_SELF_START_VIVO_V2_5 = CLASS_SELF_START_VIVO;
    // 电池
    public static final String CLASS_BATTERY_VIVO = "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity";
    public static final String CLASS_BATTERY_VIVO_2 = "com.vivo.abeui.highpower.ExcessivePowerManagerActivity";
    public static final String CLASS_BATTERY_VIVO_V2_5 = CLASS_BATTERY_VIVO;
    public static final String CLASS_BATTERY_VIVO_V3_0 = CLASS_BATTERY_VIVO_2;

    ///////////////////////////////////////////////////////////////////////////
    // EMUI
    ///////////////////////////////////////////////////////////////////////////
    public static final String PACKAGE_SETTING_EMUI = "com.huawei.systemmanager";

    // 通知设置页面 HW-P9-VIE-AL10
    public static final String CLASS_APP_NOTIFICATION_EMUI = "com.huawei.notificationmanager.ui.NotificationSettingsActivity";
    // 自启动设置页面
    public static final String CLASS_SELF_START_EMUI = "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity";
    public static final String CLASS_SELF_START_EMUI_2 = "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity";
    // 电池保活
    public static final String CLASS_BATTERY_EMUI = "com.huawei.systemmanager.optimize.process.ProtectActivity";

    public static final String PACKAGE_SETTING_EMUI_V11 = PACKAGE_SETTING_EMUI;
    public static final String CLASS_APP_NOTIFICATION_EMUI_V11 = CLASS_APP_NOTIFICATION_EMUI;
    public static final String CLASS_SELF_START_EMUI_V11 = CLASS_SELF_START_EMUI;
    public static final String CLASS_SELF_START_EMUI_V14 = CLASS_SELF_START_EMUI_2;
    public static final String CLASS_BATTERY_EMUI_V11 = CLASS_BATTERY_EMUI;

    public static final String ID_BATTERY_LIST_ID_EMUI_V11 = "com.huawei.systemmanager:id/progress_manager_white_gridview";

    ///////////////////////////////////////////////////////////////////////////
    // MIUI
    ///////////////////////////////////////////////////////////////////////////
    public static final String PACKAGE_SETTING_MIUI = "com.miui.securitycenter";
    public static final String PACKAGE_SETTING_POWER_MIUI = "com.miui.powerkeeper";

    public static final String CLASS_DIALOG_MIUI = "miui.app.AlertDialog";
    public static final String CLASS_PERMISSION_MIUI = "com.miui.permcenter.permissions.PermissionsEditorActivity";
    public static final String CLASS_SELF_START_MIUI = "com.miui.permcenter.autostart.AutoStartManagementActivity";
    public static final String CLASS_SELF_START_DETAIL_MIUI = "com.miui.permcenter.autostart.AutoStartDetailManagementActivity";
    public static final String CLASS_NAME_SETTING_POWER_MIUI = "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity";
    public static final String CLASS_NAME_SETTING_POWER_SUB_MIUI = "com.miui.powerkeeper.ui.HiddenAppsConfigActivity";
    public static final String CLASS_APP_LIST_MIUI = "com.miui.appmanager.AppManagerMainActivity";

    public static final String CLASS_SELF_START_MIUI_V9 = CLASS_SELF_START_MIUI;
    public static final String CLASS_SELF_START_DETAIL_MIUI_V9 = CLASS_SELF_START_DETAIL_MIUI;
    public static final String CLASS_NAME_SETTING_POWER_MIUI_V9 = CLASS_NAME_SETTING_POWER_MIUI;
    public static final String CLASS_NAME_SETTING_POWER_SUB_MIUI_V9 = CLASS_NAME_SETTING_POWER_SUB_MIUI;

    ///////////////////////////////////////////////////////////////////////////
    // 锤子
    ///////////////////////////////////////////////////////////////////////////
    public static final String PACKAGE_SETTING_SMART = "com.smartisanos.security";

    public static final String CLASS_PERMISSION_SMART = "com.smartisanos.security.PackagesOverview";
    public static final String CLASS_PERMISSION_DETAIL_SMART = "com.smartisanos.security.PackageDetail";

    ///////////////////////////////////////////////////////////////////////////
    // samsung
    ///////////////////////////////////////////////////////////////////////////
    public static final String PACKAGE_SETTING_SAMSUNG = "com.samsung.android.sm_cn";

    public static final String CLASS_BATTERY_SAMSUNG = "com.samsung.android.sm.ui.battery.BatteryActivity";
    public static final String CLASS_SLEEP_LIST_SAMSUNG = "com.samsung.android.sm.ui.battery.AppSleepListActivity";

}
