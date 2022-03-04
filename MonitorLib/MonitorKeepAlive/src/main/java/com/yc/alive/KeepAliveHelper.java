package com.yc.alive;

import android.content.Context;

import androidx.annotation.LayoutRes;

import com.yc.alive.constant.AliveSettingType;
import com.yc.alive.constant.AliveSettingType.TYPE;
import com.yc.alive.manager.AssistantManager;
import com.yc.alive.model.AliveIntentModel;
import com.yc.alive.util.AliveLogUtils;

import static com.yc.alive.constant.AliveSettingType.TYPE_BATTERY;
import static com.yc.alive.constant.AliveSettingType.TYPE_NOTIFICATION;
import static com.yc.alive.constant.AliveSettingType.TYPE_SELF_START;
import static com.yc.alive.constant.AliveSettingType.TYPE_WIFI_NEVER_SLEEP;

/**
 * 助手
 */
public final class KeepAliveHelper {

    /**
     * 初始化
     */
    public static void init(Context context) {
        if (context == null){
            throw new NullPointerException("context is null");
        }
        AssistantManager.getInstance().init(context);
    }

    /**
     * 是否支持当前设备
     */
    public static boolean isSupport() {
        return AssistantManager.getInstance().isSupport();
    }

    /**
     * 设置悬浮窗布局
     *
     * @param layoutId layoutId
     */
    public static void setFloatWindowLayoutId(@LayoutRes int layoutId) {
        AssistantManager.getInstance().setFloatWindowLayoutId(layoutId);
    }

    /**
     * 自动设置
     *
     * @param listener listener
     * @param types types {@link AliveSettingType}
     */
    public static void autoSetting(OnSettingListener listener, @TYPE int...types) {
        AssistantManager.getInstance().autoSetting(listener, types);
    }

    /**
     * 自动设置 - 全部
     *
     * @param listener listener
     */
    public static void autoSetting(OnSettingListener listener) {
        AssistantManager.getInstance().autoSetting(listener, TYPE_WIFI_NEVER_SLEEP, TYPE_NOTIFICATION,
            TYPE_BATTERY, TYPE_SELF_START);
    }

    /**
     * 获取设备型号
     */
    public static String getDeviceInfo() {
        return AssistantManager.getInstance().getDeviceInfo();
    }

    /**
     * 启动 1 像素 服务
     */
    public static void startOnePxService() {
        AssistantManager.getInstance().startOnePxService();
    }

    /**
     * 日志
     */
    public static void setLogger(Logger logger) {
        AliveLogUtils.ENABLE = logger != null;
        AliveLogUtils.logger = logger;
    }

    /**
     * 辅助功能是否可用
     */
    public static boolean isAccessibilityEnabled() {
        return AssistantManager.getInstance().isAccessibilityEnabled();
    }

    /**
     * wifi 从不休眠
     */
    public static boolean isWifiNeverSleepEnabled() {
        return AssistantManager.getInstance().isWifiNeverSleepEnabled();
    }

    /**
     * 通知功能是否可用
     */
    public static boolean isNotificationEnabled() {
        return AssistantManager.getInstance().isNotificationEnabled();
    }

    /**
     * 获取设置项跳转信息
     *
     * @param type 类型 {@link AliveSettingType}
     */
    public static AliveIntentModel getIntentModel(@TYPE int type) {
        return AssistantManager.getInstance().getIntentModel(type);
    }

    public interface Logger {
        void log(String tag, String message);
    }
}
