package com.yc.privateserver;

import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;

public final class PrivateService {

    /**
     * 获取设备id
     *
     * @return 设备id
     */
    public static String getDeviceId() {
        if (null != PrivateCache.DEVICE_ID) {
            return PrivateCache.DEVICE_ID;
        } else {
            String str = SafePrivateHelper.getDeviceId();
            if (TextUtils.isEmpty(str)) {
                str = "";
            } else {
                PrivateCache.MEID = str;
            }
            return str;
        }
    }

    /**
     * 获取MEID
     *
     * @return return defaultValue if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getMEID() {
        if (null != PrivateCache.MEID) {
            return PrivateCache.MEID;
        } else {
            String str = SafePrivateHelper.getMeid();
            if (TextUtils.isEmpty(str)) {
                str = "";
            } else {
                PrivateCache.MEID = str;
            }
            return str;
        }
    }

    /**
     * 获取IMEI1
     *
     * @return return defaultValue String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getImei1() {
        if (null != PrivateCache.Imei1) {
            return PrivateCache.Imei1;
        } else {
            String str = SafePrivateHelper.getImei(0);
            if (TextUtils.isEmpty(str)) {
                str = "";
            } else {
                PrivateCache.Imei1 = str;
            }
            return str;
        }
    }

    /**
     * 获取IMEI2
     *
     * @return return defaultValue String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getImei2() {
        if (null != PrivateCache.Imei2) {
            return PrivateCache.Imei2;
        } else {
            String str = SafePrivateHelper.getImei(1);
            if (TextUtils.isEmpty(str)) {
                str = "";
            } else {
                PrivateCache.Imei2 = str;
            }
            return str;
        }
    }

    /**
     * 获取IMEI
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getImei() {
        if (null != PrivateCache.IMEI) {
            return PrivateCache.IMEI;
        }
        String imei;
        if (Build.VERSION.SDK_INT >= 26) {
            imei = SafePrivateHelper.getImei();
        } else {
            imei = SafePrivateHelper.getDeviceId();
        }

        if (!TextUtils.isEmpty(imei)) {
            PrivateCache.IMEI = imei;
        } else {
            imei = "";
        }
        return imei;
    }

    /**
     * 获取Android唯一标识符
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getAndroidId() {
        if (null != PrivateCache.ANDROID_ID) {
            return PrivateCache.ANDROID_ID;
        }
        String androidId = SafePrivateHelper.getAndroidId();
        if (!TextUtils.isEmpty(androidId)) {
            PrivateCache.ANDROID_ID = androidId;
        } else {
            androidId = "";
        }
        return androidId;
    }

    /**
     * 获取手机的SN
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getSN() {
        if (null != PrivateCache.SN) {
            return PrivateCache.SN;
        } else {
            String sn = getDeviceSN();
            if (TextUtils.isEmpty(sn)) {
                return "";
            } else {
                PrivateCache.SN = sn;
                return sn;
            }
        }
    }

    /**
     * 获取手机的SN
     *
     * @return return empty String if fetch failed.
     */
    private static String getDeviceSN() {
        //通过android.os获取sn号
        String serial = SafePrivateHelper.getSN1();
        if (!TextUtils.isEmpty(serial) && !serial.equals("unknown")) {
            return serial;
        }

        //通过反射获取sn号
        serial = SafePrivateHelper.getSN2();
        if (!TextUtils.isEmpty(serial) && !serial.equals("unknown")) {
            return serial;
        }

        try {
            //9.0及以上无法获取到sn，此方法S为补充，能够获取到多数高版本手机 sn
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = SafePrivateHelper.getSN3();
            }
        } catch (Exception e) {
            serial = "";
        }
        if (TextUtils.equals(serial, "unknown")) {
            return "";
        }
        return serial;
    }

    /**
     * 获取手机运营商
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getProviderName() {
        if (null != PrivateCache.PROVIDER_NAME) {
            return PrivateCache.PROVIDER_NAME;
        }
        String providerName = "";
        try {
            String operator = getOperatorId();
            if (operator != null) {
                if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                    providerName = "中国移动";
                } else if (operator.equals("46001")) {
                    providerName = "中国联通";
                } else if (operator.equals("46003") || operator.equals("46011")) {
                    providerName = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(providerName)) {
            PrivateCache.PROVIDER_NAME = providerName;
        } else {
            providerName = "";
        }
        return providerName;
    }

    private static String getSimOperator() {
        if (null != PrivateCache.SIM_OPERATOR) {
            return PrivateCache.SIM_OPERATOR;
        }
        String operator = SafePrivateHelper.getSimOperator();
        if (!TextUtils.isEmpty(operator)) {
            PrivateCache.SIM_OPERATOR = operator;
        } else {
            operator = "";
        }
        return operator;
    }

    private static String getImsi() {
        if (null != PrivateCache.IMSI) {
            return PrivateCache.IMSI;
        }
        String IMSI = SafePrivateHelper.getSubscriberId();
        if (!TextUtils.isEmpty(IMSI)) {
            PrivateCache.IMSI = IMSI;
        } else {
            IMSI = "";
        }
        return IMSI;
    }

    /**
     * 返回sim 卡的运营商Id
     * 46000,46002 中国移动
     * 46001 中国联通
     * 46003 中国电信
     *
     * @return return empty String if fetch failed.
     */
    public static String getOperatorId() {
        if (null != PrivateCache.OPERATOR_ID) {
            return PrivateCache.OPERATOR_ID;
        }
        String operatorId = null;
        try {
            operatorId = getSimOperator();
            if (TextUtils.isEmpty(operatorId)) {
                String IMSI = getImsi();
                if (!TextUtils.isEmpty(IMSI)) {
                    if (IMSI.length() <= 5) {
                        operatorId = IMSI;
                    } else {
                        operatorId = IMSI.substring(0, 5);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (!TextUtils.isEmpty(operatorId)) {
            PrivateCache.OPERATOR_ID = operatorId;
        } else {
            operatorId = "";
        }
        return operatorId;
    }

    /**
     * 返回sim 卡的运营商名称
     *
     * @return return empty String if fetch failed.
     */
    public static String getOperatorName() {
        if (null != PrivateCache.OPERATOR_NAME) {
            return PrivateCache.OPERATOR_NAME;
        }
        String operatorName = SafePrivateHelper.getSimOperatorName();
        if (!TextUtils.isEmpty(operatorName)) {
            PrivateCache.OPERATOR_NAME = operatorName;
        } else {
            operatorName = "";
        }
        return operatorName;
    }

}
