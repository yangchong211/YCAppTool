package com.yc.privateserver;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public final class UserPrivacyHolder {

    private static volatile Context appContext;
    private static volatile boolean isInitUserPrivacy;
    private static volatile boolean isQaOrDebug;

    public static void installAppContext(@NonNull Context context) {
        if (appContext == null) {
            if (context instanceof Application) {
                appContext = context;
            } else {
                appContext = context.getApplicationContext();
            }
        }
    }

    static Context getContext() {
        if (appContext == null){
            throw new NullPointerException("please set installAppContext at first");
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

    public static boolean isInitUserPrivacy(){
        if(!isInitUserPrivacy && isQaOrDebug){
            throw new RuntimeException("not ask permission before agreeing to the privacy");
        }
        return isInitUserPrivacy;
    }

    public static void setIsInitUserPrivacy(boolean isInit,boolean isDebug){
        isInitUserPrivacy = isInit;
        isQaOrDebug = isDebug;
    }

}
