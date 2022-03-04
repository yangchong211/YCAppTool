package com.yc.alive.constant;


import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * 设置类型
 */
public class AliveSettingType {

    public static final int DEFAULT = -1;
    // 辅助功能
    public static final int TYPE_ACCESSIBILITY_SERVICE = 1;
    // 悬浮窗
    public static final int TYPE_FLOAT_WINDOW = 2;
    // 通知
    public static final int TYPE_NOTIFICATION = 3;
    // WiFi 休眠
    public static final int TYPE_WIFI_NEVER_SLEEP = 4;
    // 自启动
    public static final int TYPE_SELF_START = 5;
    // 电池
    public static final int TYPE_BATTERY = 6;

    @Documented
    @Retention(CLASS)
    @Target({ METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE })
    @IntDef({ TYPE_ACCESSIBILITY_SERVICE, TYPE_FLOAT_WINDOW, TYPE_NOTIFICATION, TYPE_WIFI_NEVER_SLEEP, TYPE_SELF_START,
        TYPE_BATTERY })
    public @interface TYPE {
    }
}
