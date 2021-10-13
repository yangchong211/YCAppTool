package com.yc.localelib.utils;

import android.text.TextUtils;

public final class CountryUtils {

    //墨西哥
    private static final String MEXICO = "MX";
    //巴西
    private static final String BRAZIL = "BR";
    //日本
    private static final String JAPAN = "JP";
    //哥斯达黎加
    private static final String COSTA_RICA = "CR";
    //哥伦比亚
    private static final String COLOMBIA = "CO";
    //多米尼加
    private static final String DOMINICA = "DO";
    //智利
    private static final String CHILE = "CL";

    private CountryUtils() {

    }

    /**
     * 是否是墨西哥
     * @param country 国家简写
     * @return
     */
    public static boolean isMexico(String country) {
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return MEXICO.equalsIgnoreCase(country);
    }

    /**
     * 是否是日本
     * @param country 国家简写
     * @return
     */
    public static boolean isJapan(String country) {
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return JAPAN.equalsIgnoreCase(country);
    }

    /**
     * 是否是巴西
     * @param country 国家简写
     * @return
     */
    public static boolean isBrazil(String country) {
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return BRAZIL.equalsIgnoreCase(country);
    }

    /**
     * 是否是哥斯达黎加
     * @param country 国家简写
     * @return
     */
    public static boolean isCostaRica(String country) {
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return COSTA_RICA.equalsIgnoreCase(country);
    }

    /**
     * 是否是哥伦比亚
     * @param country 国家简写
     * @return
     */
    public static boolean isColombia(String country) {
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return COLOMBIA.equalsIgnoreCase(country);
    }


    /**
     * 是否是多米尼加
     * @param country 国家简写
     * @return
     */
    public static boolean isDominica(String country) {
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return DOMINICA.equalsIgnoreCase(country);
    }


    /**
     * 是否是智利
     * @param country 国家简写
     * @return
     */
    public static boolean isChile(String country) {
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return CHILE.equalsIgnoreCase(country);
    }
}
