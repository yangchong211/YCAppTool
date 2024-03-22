package com.yc.localelib.locale.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yc.localelib.locale.service.LocaleService;

import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 配置保存类
 *     revise :
 * </pre>
 */
public final class LocaleSpUtils {

    private static final String KEY_LANGUAGE = "key_language";
    private static final String KEY_COUNTRY = "key_country";
    private static String sSharedPreferencesName = "language_setting";
    private static volatile Locale sCurrentLanguage;

    private LocaleSpUtils(){

    }

    /**
     * 设置保存的 SharedPreferences 文件名（请在 Application 初始化之前设置，可以放在 Application 中的代码块或者静态代码块）
     */
    public static synchronized void setSharedPreferencesName(String name) {
        sSharedPreferencesName = name;
    }

    private static synchronized SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(sSharedPreferencesName, Context.MODE_PRIVATE);
    }

    public static synchronized void setAppLanguage(Context context, Locale locale) {
        sCurrentLanguage = locale;
        getSharedPreferences(context).edit()
                .putString(KEY_LANGUAGE, locale.getLanguage())
                .putString(KEY_COUNTRY, locale.getCountry())
                .apply();
    }

    public static synchronized Locale getAppLanguage(Context context) {
        if (sCurrentLanguage == null) {
            String language = getSharedPreferences(context).getString(KEY_LANGUAGE, null);
            String country = getSharedPreferences(context).getString(KEY_COUNTRY, null);
            if (!TextUtils.isEmpty(language)) {
                sCurrentLanguage = new Locale(language, country);
            } else {
                sCurrentLanguage = LocaleToolUtils.getLocale(context);
            }
        }
        return sCurrentLanguage;
    }

    public static synchronized boolean isSystemLanguage(Context context) {
        String language = getSharedPreferences(context).getString(KEY_LANGUAGE, null);
        return language == null || "".equals(language);
    }

    public static synchronized void clearLanguage(Context context) {
        sCurrentLanguage = LocaleService.getInstance().getSystemLocale();
        getSharedPreferences(context).edit()
                .remove(KEY_LANGUAGE)
                .remove(KEY_COUNTRY)
                .apply();
    }
}