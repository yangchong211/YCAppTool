package com.yc.privateserver;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public final class PrivateService {

    /**
     * 获取MEID
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public static String getMEID(Context context) {
        return getMEID(context, "");
    }

    /**
     * 获取MEID
     *
     * @param context application context
     * @return return defaultValue if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getMEID(Context context, String defaultValue) {
        if (null != PrivateCache.MEID) {
            return PrivateCache.MEID;
        } else if (context != null) {
            UserPrivacyHolder.installAppContext(context);
            String str = SafePrivateHelper.getMeid();
            if (TextUtils.isEmpty(str)) {
                str = defaultValue;
            } else {
                PrivateCache.MEID = str;
            }
            return str;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取IMEI1
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public static String getImei1(Context context) {
        return getImei1(context, "");
    }

    /**
     * 获取IMEI1
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getImei1(Context context, String defaultValue) {
        if (null != PrivateCache.Imei1) {
            return PrivateCache.Imei1;
        } else if (context != null) {
            UserPrivacyHolder.installAppContext(context);
            String str = SafePrivateHelper.getImei(0);
            if (TextUtils.isEmpty(str)) {
                str = defaultValue;
            } else {
                PrivateCache.Imei1 = str;
            }
            return str;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取IMEI2
     *
     * @return return empty String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public static String getImei2(Context context) {
        return getImei2(context, "");
    }

    /**
     * 获取IMEI2
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getImei2(Context context, String defaultValue) {
        if (null != PrivateCache.Imei2) {
            return PrivateCache.Imei2;
        } else if (context != null) {
            UserPrivacyHolder.installAppContext(context);
            String str = SafePrivateHelper.getImei(1);
            if (TextUtils.isEmpty(str)) {
                str = defaultValue;
            } else {
                PrivateCache.Imei2 = str;
            }
            return str;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取IMEI
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getImei(Context context) {
        return getImei(context, "");
    }

    /**
     * 获取IMEI
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    public static String getImei(Context context, String defaultValue) {
        if (null != PrivateCache.IMEI) {
            return PrivateCache.IMEI;
        }
        UserPrivacyHolder.installAppContext(context);
        String imei;
        if (Build.VERSION.SDK_INT >= 26) {
            imei = SafePrivateHelper.getImei();
        } else {
            imei = SafePrivateHelper.getDeviceId();
        }

        if (!TextUtils.isEmpty(imei)) {
            PrivateCache.IMEI = imei;
        } else {
            imei = defaultValue;
        }
        return imei;
    }

    /**
     * 获取Android唯一标识符
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getAndroidId(Context context) {
        return getAndroidId(context, "");
    }

    /**
     * 获取Android唯一标识符
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    public static String getAndroidId(Context context, String defaultValue) {
        if (null != PrivateCache.ANDROID_ID) {
            return PrivateCache.ANDROID_ID;
        }
        UserPrivacyHolder.installAppContext(context);
        String androidId = SafePrivateHelper.getAndroidId();
        if (!TextUtils.isEmpty(androidId)) {
            PrivateCache.ANDROID_ID = androidId;
        } else {
            androidId = defaultValue;
        }
        return androidId;
    }

    /**
     * 获取手机的SN
     *
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getSN(Context context) {
        return getSN(context, "");
    }

    /**
     * 获取手机的SN
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getSN(Context context, String defaultValue) {
        if (null != PrivateCache.SN) {
            return PrivateCache.SN;
        } else if (null != context) {
            String sn = getDeviceSN(context);
            if(TextUtils.isEmpty(sn)){
                return defaultValue;
            }else {
                PrivateCache.SN = sn;
                return sn;
            }
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取手机的SN
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    private static String getDeviceSN(Context context) {
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
            UserPrivacyHolder.installAppContext(context);
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
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getProviderName(Context context) {
        return getProviderName(context, "");
    }

    /**
     * 获取手机运营商
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getProviderName(Context context, String defaultValue) {
        if (null != PrivateCache.PROVIDER_NAME) {
            return PrivateCache.PROVIDER_NAME;
        }
        String providerName = "";
        try {
            String operator = getOperatorId(context, null);
            if (operator != null) {
                if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                    providerName = "中国移动";
                } else if (operator.equals("46001")) {
                    providerName = "中国联通";
                } else if (operator.equals("46003")  || operator.equals("46011")) {
                    providerName = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(providerName)) {
            PrivateCache.PROVIDER_NAME = providerName;
        } else {
            providerName = defaultValue;
        }
        return providerName;
    }

    private static String getSimOperator(Context context, String defaultValue) {
        if (null != PrivateCache.SIM_OPERATOR) {
            return PrivateCache.SIM_OPERATOR;
        }
        UserPrivacyHolder.installAppContext(context);
        String operator = SafePrivateHelper.getSimOperator();
        if (!TextUtils.isEmpty(operator)) {
            PrivateCache.SIM_OPERATOR = operator;
        } else {
            operator = defaultValue;
        }
        return operator;
    }

    private static String getImsi(Context context, String defaultValue) {
        if (null != PrivateCache.IMSI) {
            return PrivateCache.IMSI;
        }
        UserPrivacyHolder.installAppContext(context);
        String IMSI = SafePrivateHelper.getSubscriberId();

        if (!TextUtils.isEmpty(IMSI)) {
            PrivateCache.IMSI = IMSI;
        } else {
            IMSI = defaultValue;
        }
        return IMSI;
    }

    /**
     * 返回sim 卡的运营商Id
     * 46000,46002 中国移动
     * 46001 中国联通
     * 46003 中国电信
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getOperatorId(Context context) {
        return getOperatorId(context, "");
    }

    /**
     * 返回sim 卡的运营商Id
     * 46000,46002 中国移动
     * 46001 中国联通
     * 46003 中国电信
     * @return return empty String if fetch failed.
     */
    public static String getOperatorId(Context context, String defaultValue) {
        if (null != PrivateCache.OPERATOR_ID) {
            return PrivateCache.OPERATOR_ID;
        }
        String operatorId = null;
        try {
            operatorId = getSimOperator(context, null);
            if (TextUtils.isEmpty(operatorId)) {
                String IMSI = getImsi(context, null);
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
            operatorId = defaultValue;
        }
        return operatorId;
    }

    /**
     * 返回sim 卡的运营商名称
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getOperatorName(Context context) {
        return getOperatorName(context, "");
    }

    /**
     * 返回sim 卡的运营商名称
     * @return return empty String if fetch failed.
     */
    public static String getOperatorName(Context context, String defaultValue) {
        if (null != PrivateCache.OPERATOR_NAME) {
            return PrivateCache.OPERATOR_NAME;
        }
        UserPrivacyHolder.installAppContext(context);
        String operatorName = SafePrivateHelper.getSimOperatorName();
        if (!TextUtils.isEmpty(operatorName)) {
            PrivateCache.OPERATOR_NAME = operatorName;
        } else {
            operatorName = defaultValue;
        }
        return operatorName;
    }

}
