package com.yc.privateserver;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.yc.appcontextlib.AppToolUtils;
import com.yc.networklib.net.AddressToolUtils;
import com.yc.toolutils.AppDeviceUtils;

import java.lang.reflect.Method;

/**
 * 建议APP需遵循合理、正当、必要的原则收集用户个人信息，具体为：
 * 1.收集的个人信息的类型应与实现产品或服务的业务功能有直接关联，直接关联是指没有该信息的参与，产品或服务的功能无法实现；
 * 2.自动采集个人信息的频率应是实现产品或服务的业务功能所必需的最低频率；
 * 3.间接获取个人信息的数量应是实现产品或服务的业务功能所必需的最少数量。
 */
public final class SafePrivateHelper implements IPrivateDevice, IPrivateNet {

    private static volatile SafePrivateHelper singleton = null;

    static SafePrivateHelper getInstance() {
        if (singleton == null) {
            synchronized (SafePrivateHelper.class) {
                if (singleton == null) {
                    singleton = new SafePrivateHelper();
                }
            }
        }
        return singleton;
    }

    private SafePrivateHelper() {

    }

    /**
     * -------------------------------------------------------------------------------
     * getDeviceId
     */
    @Nullable
    @Override
    public String getDeviceId() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            //如果没有设置同意隐私政策，那么获取的设备参数传递空。这样避免调用在隐私协议政策前调用导致不合规！
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            return DeviceIdOnceGetter.DEVICE_ID;
        }
        return null;
    }

    /**
     * 必须先判断权限==
     */
    private static class DeviceIdOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @Nullable
        private static final String DEVICE_ID = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
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
    @Override
    public String getDeviceId(int index) {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            if (index == 0) {
                return DeviceId1OnceGetter.DEVICE_ID_0;
            } else if (index == 1) {
                return DeviceId2OnceGetter.DEVICE_ID_1;
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
        private static final String DEVICE_ID_0 = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
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
        private static final String DEVICE_ID_1 = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
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
    @Override
    public String getImei() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            return ImeiOnceGetter.IMEI;
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
        private static final String IMEI = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
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
    @Override
    public String getImei(int slotIndex) {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            if (slotIndex == 0) {
                return Imei1OnceGetter.IMEI_0;
            } else {
                return Imei2OnceGetter.IMEI_1;
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
        private static final String IMEI_0 = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
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
        private static final String IMEI_1 = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
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
    @Override
    public String getMeid() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            return MeidOnceGetter.MEID;
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
        private static final String MEID = SafeGet();

        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telephonyManager = UserPrivacyInit.getTelephonyManager();
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
    @Override
    public String getMeid(int index) {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            if (index == 0) {
                return Meid1OnceGetter.MEID_0;
            } else if (index == 1) {
                return Meid2OnceGetter.MEID_1;
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
        private static final String MEID_0 = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"MissingPermission"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telephonyManager = UserPrivacyInit.getTelephonyManager();
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
        private static final String MEID_1 = SafeGet();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"MissingPermission"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telephonyManager = UserPrivacyInit.getTelephonyManager();
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
    @Override
    public String getSubscriberId() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            return SubscriberIdOnceGetter.SUBSCRIBER_ID;
        }
        return null;
    }

    /**
     * 必须要在检查权限之后调用
     */
    private static class SubscriberIdOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        private static final String SUBSCRIBER_ID = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
                return telManager.getSubscriberId();
            } catch (Throwable ignored) {
            }
            return null;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getSimOperatorName()
     *
     * @return empty string for fail.
     */
    @NonNull
    @Override
    public String getSimOperatorName() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return "";
        }
        return OperatorNameOnceGetter.SIM_OPERATOR;
    }

    private static class OperatorNameOnceGetter {
        @NonNull
        private static final String SIM_OPERATOR = SafeGet();

        @SuppressLint({"HardwareIds"})
        @NonNull
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
                return telManager.getSimOperatorName();
            } catch (Throwable ignored) {
            }
            return "";
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getSimOperator()
     *
     * @return empty string for fail.
     */
    @NonNull
    @Override
    public String getSimOperator() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return "";
        }
        return SimOperatorOnceGetter.SIM_OPERATOR;
    }

    private static class SimOperatorOnceGetter {
        @NonNull
        private static final String SIM_OPERATOR = SafeGet();

        @NonNull
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
                return telManager.getSimOperator();
            } catch (Throwable ignored) {
            }
            return "";
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * getSimSerialNumber()
     *
     * @return null for fail.
     */

    @Nullable
    @Override
    public String getSimSerialNumber() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return null;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            return SimSerialNumberOnceGetter.SIM_SERIAL_NUMBER;
        }
        return null;
    }

    private static class SimSerialNumberOnceGetter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @Nullable
        private static final String SIM_SERIAL_NUMBER = SafeGet();

        @SuppressLint({"MissingPermission", "HardwareIds"})
        @Nullable
        private static String SafeGet() {
            try {
                TelephonyManager telManager = UserPrivacyInit.getTelephonyManager();
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
    @Override
    public String getSN1() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return DEFAULT_SN;
        }
        return SNOnce1Getter.SERIAL;
    }

    private static class SNOnce1Getter {
        @NonNull
        private static final String SERIAL = SafeGet();

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
    @Override
    public String getSN2() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return DEFAULT_SN;
        }
        return SNOnce2Getter.SERIAL_2;
    }

    private static class SNOnce2Getter {
        @NonNull
        private static final String SERIAL_2 = SafeGet();

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
    @Override
    public String getSN3() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return DEFAULT_SN;
        }
        if (UserPrivacyInit.hasReadPhoneStatePermission()) {
            return SNOnce3Getter.SERIAL;
        }
        return DEFAULT_SN;
    }

    private static class SNOnce3Getter {
        @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        private static final String SERIAL = SafeGet();

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
    @Override
    public String getAndroidId() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return "";
        }
        return AndroidIdOnceGetter.ANDROID_ID;
    }

    private static class AndroidIdOnceGetter {
        private static final String ANDROID_ID = SafeGet();

        @SuppressLint("HardwareIds")
        private static String SafeGet() {
            try {
                Context context = UserPrivacyInit.getContext();
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Throwable ignored) {
            }
            return "";
        }
    }


    @Override
    public String getMacAddress() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return "";
        }
        Application app = AppToolUtils.getApp();
        return AppDeviceUtils.getMacAddress(app);
    }

    @Override
    public String getIpAddress() {
        if (!UserPrivacyInit.isInitUserPrivacy()) {
            return "";
        }
        return AddressToolUtils.getIpAddress();
    }

    @Override
    public String getSsid() {
        return null;
    }

}
