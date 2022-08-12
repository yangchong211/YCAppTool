package com.yc.privateserver;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public final class PrivateService {


    private final static String INVALID_IMEI_1 = "000000000000000";
    private final static String INVALID_IMEI_2 = "111111111111111";
    private final static String INVALID_IMEI_3 = "123456789123456";

    public static final class Cache {
        /**
         * imei
         * IMEI由15-17位数字组成，每位数字仅使用0~9的数字，IMEI不一定是15位
         * 01为美国CTIA，35为英国BABT，86为中国TAF
         * 由15位数字组成，前6位(TAC)是型号核准号码，代表手机类型。接着2位(FAC)是最后装配号，代表产地。后6位(SNR)是串号，代表生产顺序号。最后1位(SP)是检验码
         */
        public static volatile String IMEI = null;
        /**
         * 16位16进制
         */
        public static volatile String ANDROID_ID = null;
        public static volatile String PROVIDER_NAME = null;
        public static volatile String OPERATOR_ID = null;
        /**
         * 由15位16进制数字组成(一般使用前14位)，前8位是生产商编号，后6位是串号，最后1位是检验码
         */
        public static volatile String MEID = null;
        public static volatile String Imei1 = null;
        public static volatile String Imei2 = null;
        public static volatile String SN = null;
        public static volatile String OPERATOR_NAME = null;
        /**
         * 总长度16位，有MCC+MNC+MSIN三部分组成。
         * MCC：移动国家码，三位数字，如中国460
         * MNC：移动网号，两个数字，如中国移动：00，联通：01
         * MSIN：移动客户识别号
         */
        public static volatile String IMSI = null;
        public static volatile String SIM_Operator = null;
    }

    /**
     * 获取MEID
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public static String getMEID(@NonNull Context context) {
        return getMEID(context, "");
    }

    /**
     * 获取MEID
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @param context application context
     * @return return defaultValue if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getMEID(@NonNull Context context, String defaultValue) {
        if (null != Cache.MEID) {
            return Cache.MEID;
        } else if (context != null) {
            UserPrivacyHolder.installAppContext(context);
            String str = SafePrivateHelper.getMeid();

            if (TextUtils.isEmpty(str)) {
                str = defaultValue;
            } else {
                Cache.MEID = str;
            }
            return str;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取IMEI1
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public static String getIMEI1(@NonNull Context context) {
        return getIMEI1(context, "");
    }

    /**
     * 获取IMEI1
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getIMEI1(@NonNull Context context, String defaultValue) {
        if (null != Cache.Imei1) {
            return Cache.Imei1;
        } else if (context != null) {

            UserPrivacyHolder.installAppContext(context);
            String str = SafePrivateHelper.getImei(0);

            if (TextUtils.isEmpty(str)) {
                str = defaultValue;
            } else {
                Cache.Imei1 = str;
            }
            return str;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取IMEI2
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @return return empty String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public static String getIMEI2(@NonNull Context context) {
        return getIMEI2(context, "");
    }

    /**
     * 获取IMEI2
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getIMEI2(@NonNull Context context, String defaultValue) {
        if (null != Cache.Imei2) {
            return Cache.Imei2;
        } else if (context != null) {
            UserPrivacyHolder.installAppContext(context);
            String str = SafePrivateHelper.getImei(1);

            if (TextUtils.isEmpty(str)) {
                str = defaultValue;
            } else {
                Cache.Imei2 = str;
            }
            return str;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取IMEI
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getImei(@NonNull Context context) {
        return getImei(context, "");
    }

    /**
     * 获取IMEI
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    public static String getImei(@NonNull Context context, String defaultValue) {
        if (null != Cache.IMEI) {
            return Cache.IMEI;
        }

        UserPrivacyHolder.installAppContext(context);
        String imei;
        if (Build.VERSION.SDK_INT >= 26) {
            imei = SafePrivateHelper.getImei();
        } else {
            imei = SafePrivateHelper.getDeviceId();
        }

        if (!TextUtils.isEmpty(imei)) {
            Cache.IMEI = imei;
        } else {
            imei = defaultValue;
        }
        return imei;
    }

    /**
     * 获取Android唯一标识符
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @param context application context
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getAndroidId(@NonNull Context context) {
        return getAndroidId(context, "");
    }

    /**
     * 获取Android唯一标识符
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @param context application context
     * @return return defaultValue String if fetch failed.
     */
    public static String getAndroidId(@NonNull Context context, String defaultValue) {
        if (null != Cache.ANDROID_ID) {
            return Cache.ANDROID_ID;
        }

        UserPrivacyHolder.installAppContext(context);
        String androidId = SafePrivateHelper.getAndroidId();

        if (!TextUtils.isEmpty(androidId)) {
            Cache.ANDROID_ID = androidId;
        } else {
            androidId = defaultValue;
        }
        return androidId;
    }

    /**
     * 获取手机的SN
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getSN(@NonNull Context context) {
        return getSN(context, "");
    }

    /**
     * 获取手机的SN
     * 来源：com.baidu.device.utils.AppUtil
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getSN(@NonNull Context context, String defaultValue) {
        if (null != Cache.SN) {
            return Cache.SN;
        } else if (null != context) {
            String sn = getDeviceSN(context);
            if(TextUtils.isEmpty(sn)){
                return defaultValue;
            }else {
                Cache.SN = sn;
                return sn;
            }
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取手机的SN
     * 来源：com.baidu.device.utils.AppUtil
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
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getProviderName(@NonNull Context context) {
        return getProviderName(context, "");
    }

    /**
     * 获取手机运营商
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @return return defaultValue String if fetch failed.
     */
    public static String getProviderName(@NonNull Context context, String defaultValue) {
        if (null != Cache.PROVIDER_NAME) {
            return Cache.PROVIDER_NAME;
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
            Cache.PROVIDER_NAME = providerName;
        } else {
            providerName = defaultValue;
        }
        return providerName;
    }

    private static String getSimOperator(@NonNull Context context, String defaultValue) {
        if (null != Cache.SIM_Operator) {
            return Cache.SIM_Operator;
        }

        UserPrivacyHolder.installAppContext(context);
        String operator = SafePrivateHelper.getSimOperator();

        if (!TextUtils.isEmpty(operator)) {
            Cache.SIM_Operator = operator;
        } else {
            operator = defaultValue;
        }
        return operator;
    }

    private static String getImsi(@NonNull Context context, String defaultValue) {
        if (null != Cache.IMSI) {
            return Cache.IMSI;
        }

        UserPrivacyHolder.installAppContext(context);
        String IMSI = SafePrivateHelper.getSubscriberId();

        if (!TextUtils.isEmpty(IMSI)) {
            Cache.IMSI = IMSI;
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
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getOperatorId(@NonNull Context context) {
        return getOperatorId(context, "");
    }

    /**
     * 返回sim 卡的运营商Id
     * 46000,46002 中国移动
     * 46001 中国联通
     * 46003 中国电信
     * 来源：com.baidu.homework.common.utils.DeviceUtils
     *
     * @return return empty String if fetch failed.
     */
    public static String getOperatorId(@NonNull Context context, String defaultValue) {
        if (null != Cache.OPERATOR_ID) {
            return Cache.OPERATOR_ID;
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
            Cache.OPERATOR_ID = operatorId;
        } else {
            operatorId = defaultValue;
        }
        return operatorId;
    }

    /**
     * 返回sim 卡的运营商名称
     * 来源：com.homework.lib_datareport.utils
     * @return return empty String if fetch failed.
     */
    @NonNull
    public static String getOperatorName(@NonNull Context context) {
        return getOperatorName(context, "");
    }

    /**
     * 返回sim 卡的运营商名称
     * 来源：com.homework.lib_datareport.utils
     * @return return empty String if fetch failed.
     */
    public static String getOperatorName(@NonNull Context context, String defaultValue) {
        if (null != Cache.OPERATOR_NAME) {
            return Cache.OPERATOR_NAME;
        }

        UserPrivacyHolder.installAppContext(context);
        String operatorName = SafePrivateHelper.getSimOperatorName();

        if (!TextUtils.isEmpty(operatorName)) {
            Cache.OPERATOR_NAME = operatorName;
        } else {
            operatorName = defaultValue;
        }
        return operatorName;
    }

}
