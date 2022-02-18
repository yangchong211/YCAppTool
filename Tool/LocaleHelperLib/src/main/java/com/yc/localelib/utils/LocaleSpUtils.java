package com.yc.localelib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yc.localelib.observer.MultiLanguages;

import java.util.Locale;

/**
 * 语种配置保存类
 */
public final class LocaleSpUtils {

    private static final String KEY_LANGUAGE = "key_language";
    private static final String KEY_COUNTRY = "key_country";
    private static String sSharedPreferencesName = "language_setting";
    private static volatile Locale sCurrentLanguage;

    private LocaleSpUtils(){

    }

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
                sCurrentLanguage = LanguagesUtils.getLocale(context);
            }
        }
        return sCurrentLanguage;
    }

    public static synchronized boolean isSystemLanguage(Context context) {
        String language = getSharedPreferences(context).getString(KEY_LANGUAGE, null);
        return language == null || "".equals(language);
    }

    public static synchronized void clearLanguage(Context context) {
        sCurrentLanguage = MultiLanguages.getSystemLanguage();
        getSharedPreferences(context).edit()
                .remove(KEY_LANGUAGE)
                .remove(KEY_COUNTRY)
                .apply();
    }
}