package com.yc.privateserver;

import android.text.TextUtils;

import java.util.regex.Pattern;

public final class AppVerifyUtils {

    private final static String INVALID_ANDROID_ID = "123456789123456";

    /**
     * 校验meid是否合法
     *
     * @param str meid
     * @return 是否合法
     */
    public static boolean verifyMeid(String str) {
        return Pattern.matches("[0-9A-F]{14}", str.toUpperCase());
    }


    /**
     * 校验imei是否合法
     *
     * @param str imei
     * @return 是否合法
     */
    public static boolean verifyImei(String str) {
        return Pattern.matches("[0-9]{15}", str);
    }

    /**
     * 校验androidId是否合法
     *
     * @param str android_id
     * @return 是否合法
     */
    public static boolean verifyAndroidId(String str) {
        if (TextUtils.equals(str, INVALID_ANDROID_ID)) {
            return false;
        }
        return Pattern.matches("[0-9a-f]{16}", str);
    }

}
