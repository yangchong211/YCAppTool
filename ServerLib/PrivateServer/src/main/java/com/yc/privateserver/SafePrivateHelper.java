package com.yc.privateserver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import java.lang.reflect.Method;

public class SafePrivateHelper {

    /**
     * -------------------------------------------------------------------------------
     * getDeviceId
     */
    @Nullable
    protected static String getDeviceId() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            return DeviceIdOnceGetter.val;
        }
        return null;
    }

    /**
     * 必须先判断权限==
     */
    private static class DeviceIdOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @Nullable
        private static final String val = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getDeviceId();
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getDeviceId(index)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    protected static String getDeviceId(int index) {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            if (index == 0) {
                return DeviceId1OnceGetter.val;
            } else if (index == 1) {
                return DeviceId2OnceGetter.val;
            }
        }
        return null;
    }

    /**
     * 必须先判断权限==
     */
    private static class DeviceId1OnceGetter {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @Nullable
        private static final String val = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getDeviceId(0);
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * 必须先判断权限==
     */
    private static class DeviceId2OnceGetter {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @Nullable
        private static final String val = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getDeviceId(1);
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getImei()
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    protected static String getImei() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            return ImeiOnceGetter.val;
        }
        return null;
    }

    /**
     * 必须先判断权限==
     */
    private static class ImeiOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static final String val = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getImei();
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getImei(int)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    protected static String getImei(int slotIndex) {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            if (slotIndex == 0) {
                return Imei1OnceGetter.val;
            } else {
                return Imei2OnceGetter.val;
            }
        }
        return null;
    }

    /**
     * 卡槽1
     */
    private static class Imei1OnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static final String val = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getImei(0);
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * 卡槽2
     */
    private static class Imei2OnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static final String val = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getImei(1);
            } catch (Throwable ignored) {
            }
            return null;
        }
    }


    /**
     * -------------------------------------------------------------------------------
     * getMeid()
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    protected static String getMeid() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            return MeidOnceGetter.val;
        }
        return null;
    }

    /**
     * 必须自己判断权限==
     */
    private static class MeidOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static final String val = SafeGet();

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telephonyManager = UserPrivacyHolder.getTelephonyManager();
                return telephonyManager.getMeid();
            } catch (Throwable ignored) {
            }
            return null;
        }
    }



    /**
     * -------------------------------------------------------------------------------
     * getMeid(int)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    protected static String getMeid(int index) {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            if (index == 0) {
                return Meid1OnceGetter.val;
            } else if (index == 1) {
                return Meid2OnceGetter.val;
            }
        }
        return null;
    }

    /**
     * 必须自己判断权限==
     */
    private static class Meid1OnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static final String val = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"MissingPermission"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telephonyManager = UserPrivacyHolder.getTelephonyManager();
                return telephonyManager.getMeid(0);
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * 必须自己判断权限==
     */
    private static class Meid2OnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static final String val = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"MissingPermission"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telephonyManager = UserPrivacyHolder.getTelephonyManager();
                return telephonyManager.getMeid(1);
            } catch (Throwable ignored) {
            }
            return null;
        }
    }


    /**
     * -------------------------------------------------------------------------------
     * getSubscriberId()
     */
    @Nullable
    protected static String getSubscriberId() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            return SubscriberIdOnceGetter.val;
        }
        return null;
    }

    /**
     * 必须要在检查权限之后调用
     */
    private static class SubscriberIdOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        private static final String val = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getSubscriberId();
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getSimOperatorName()
     * @return empty string for fail.
     */
    @NonNull
    protected static String getSimOperatorName() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return "";
        }
        return OperatorNameOnceGetter.val;
    }

    private static class OperatorNameOnceGetter {
        @NonNull
        private static final String val = SafeGet();

        @SuppressLint({"HardwareIds"})
        @NonNull
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getSimOperatorName();
            } catch (Throwable ignored) {
            }
            return "";
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getSimOperator()
     * @return empty string for fail.
     */
    @NonNull
    protected static String getSimOperator() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return "";
        }
        return SimOperatorOnceGetter.val;
    }

    private static class SimOperatorOnceGetter {
        @NonNull
        private static final String val = SafeGet();

        @NonNull
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getSimOperator();
            } catch (Throwable ignored) {
            }
            return "";
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getSimSerialNumber()
     * @return null for fail.
     */

    @Nullable
    protected static String getSimSerialNumber() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return null;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            return SimSerialNumberOnceGetter.val;
        }
        return null;
    }

    private static class SimSerialNumberOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @Nullable
        private static final String val = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyHolder.getTelephonyManager();
                return telManager.getSimSerialNumber();
            } catch (Throwable ignored) {
            }
            return "";
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * android.os.Build.SERIAL
     */
    protected static final String DEFAULT_SN = "unknown";

    @NonNull
    protected static String getSN1() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return DEFAULT_SN;
        }
        return SNOnce1Getter.val;
    }

    private static class SNOnce1Getter {
        @NonNull
        private static final String val = SafeGet();

        @SuppressLint("HardwareIds")
        @NonNull
        private static String SafeGet() {
            try {
                return android.os.Build.SERIAL;
            } catch (Throwable ignored) {
            }
            return DEFAULT_SN;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * SystemProperties.get()
     */
    @NonNull
    protected static String getSN2() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return DEFAULT_SN;
        }
        return SNOnce2Getter.val;
    }

    private static class SNOnce2Getter {
        @NonNull
        private static final String val = SafeGet();

        @SuppressLint({"HardwareIds", "PrivateApi"})
        @NonNull
        private static String SafeGet() {
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                return (String) get.invoke(c, "ro.serialno");
            } catch (Throwable ignored) {
            }
            return DEFAULT_SN;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * Build.getSerial()
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    protected static String getSN3() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return DEFAULT_SN;
        }
        if (UserPrivacyHolder.hasReadPhoneStatePermission()) {
            return SNOnce3Getter.val;
        }
        return DEFAULT_SN;
    }

    private static class SNOnce3Getter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        private static final String val = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"HardwareIds", "MissingPermission"})
        @NonNull
        private static String SafeGet() {
            try {
                return Build.getSerial();
            } catch (Throwable ignored) {
            }
            return DEFAULT_SN;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getAndroidId()
     */
    protected static String getAndroidId() {
        if(!UserPrivacyHolder.isInitUserPrivacy()){
            return "";
        }
        return AndroidIdOnceGetter.ANDROID_ID;
    }

    private static class AndroidIdOnceGetter {
        private static final String ANDROID_ID = SafeGet();
        @SuppressLint("HardwareIds")
        private static String SafeGet() {
            try {
                Context context = UserPrivacyHolder.getContext();
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Throwable ignored) {
            }
            return "";
        }
    }


}
