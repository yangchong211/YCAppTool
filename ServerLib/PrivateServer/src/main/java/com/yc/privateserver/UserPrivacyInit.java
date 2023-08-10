package com.yc.privateserver;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * 建议APP需遵循合理、正当、必要的原则收集用户个人信息，具体为：
 * 1.收集的个人信息的类型应与实现产品或服务的业务功能有直接关联，直接关联是指没有该信息的参与，产品或服务的功能无法实现；
 * 2.自动采集个人信息的频率应是实现产品或服务的业务功能所必需的最低频率；
 * 3.间接获取个人信息的数量应是实现产品或服务的业务功能所必需的最少数量。
 */
public final class UserPrivacyInit {

    private static volatile Context appContext;
    private static volatile boolean isInitUserPrivacy;
    private static volatile boolean isQaOrDebug;

    public static void installApp(@NonNull Context context) {
        if (appContext == null) {
            if (context instanceof Application) {
                appContext = context;
            } else {
                appContext = context.getApplicationContext();
            }
        }
    }

    static Context getContext() {
        if (appContext == null) {
            throw new NullPointerException("please set installApp at first");
        }
        return appContext;
    }

    static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    static boolean hasReadPhoneStatePermission() {
        return ContextCompat.checkSelfPermission(appContext,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED;
    }

    public static boolean isInitUserPrivacy() {
        if (!isInitUserPrivacy && isQaOrDebug) {
            throw new RuntimeException("not ask permission before agreeing to the privacy");
        }
        return isInitUserPrivacy;
    }

    public static void setIsInitUserPrivacy(boolean isInit, boolean isDebug) {
        isInitUserPrivacy = isInit;
        isQaOrDebug = isDebug;
    }

}
