package com.yc.localelib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.v4.os.LocaleListCompat;
import android.util.DisplayMetrics;

import java.util.Locale;

public final class LocaleUtils {

    static {
        //初始化
        initConfigLocale();
    }

    public static Locale defaultConfigLocale;

    /**
     * 获取config中的locale。即设置过的locale
     */
    public static void initConfigLocale() {
        LocaleUtils.defaultConfigLocale = LocaleUtils.getDefaultLocale();
    }

    /**
     * 获取默认的locale
     * @return                          Locale对象
     */
    public static Locale getDefaultLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0);
        } else {
            return Locale.getDefault();
        }
    }

    /**
     * 获取手机app的locale
     * @param context                   app上下文，分为两种，一种是全局application，一种是activity的
     * @return
     */
    public static Locale getAppLocale(Context context){
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        if (locale == null) {
            locale = new Locale("en-US");
        }
        return locale;
    }

    /**
     * 获取真正的，手机系统设置里的locale。
     * @retrun 手机系统设置里的locale，为null则返回en-US。
     */
    public static Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        if (locale == null) {
            locale = new Locale("en-US");
        }
        return locale;
    }

    /**
     * 根据localeTag获取对应的locale
     * @param localeTag                         localeTag
     * @return
     */
    public static Locale tagToLocale(String localeTag) {
        String realLocaleTag = localeTag.replace("_", "-");
        LocaleListCompat localeListCompat = LocaleListCompat.forLanguageTags(realLocaleTag);
        Locale locale = localeListCompat.get(0);
        return locale;
    }

    /**
     * 比较两个语言，是否相同
     * @return true：同一语言，false：不同语言
     */
    public static boolean equalLocales(Locale cur, Locale tar) {
        String curLang = cur.getLanguage();
        String tarLang = tar.getLanguage();
        return curLang.startsWith(tarLang);
    }

    /**
     * 比较两个语言，是否相同
     * @return true：同一语言，false：不同语言
     */
    public static boolean equalLocales(String curTag, String tarTag) {
        Locale cur = tagToLocale(curTag);
        Locale tar = tagToLocale(tarTag);
        return equalLocales(cur, tar);
    }

    /**
     * 更新locale
     * @param context               上下文
     * @param newLocale             新locale
     */
    public static void updateLocale(Context context, Locale newLocale){
        Resources resources = context.getApplicationContext() == null
                ? context.getResources()
                : context.getApplicationContext().getResources();
        //Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            configuration.setLocales(localeList);
            LocaleList.setDefault(localeList);
            Locale.setDefault(newLocale);
        } else {
            configuration.setLocale(newLocale);
        }
        resources.updateConfiguration(configuration,displayMetrics);
    }

    /**
     * 更新locale
     * 问题：
     *      Android7.0及之后版本，使用了LocaleList，Configuration中的语言设置可能获取的不同，而是生效于各自的Context。
     *      这会导致：Android7.0使用就的方式，有些Activity可能会显示为手机的系统语言。
     * 解决方案：
     *      Android7.0 优化了对多语言的支持，废弃了updateConfiguration()方法;
     *      替代方法：createConfigurationContext(), 而返回的是Context。
     * @param context               上下文
     * @param newLocale             新locale
     */
    public static Context updateLocale2(Context context, Locale newLocale){
        Resources resources = context.getApplicationContext() == null
                ? context.getResources()
                : context.getApplicationContext().getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            configuration.setLocales(localeList);
            LocaleList.setDefault(localeList);
            Locale.setDefault(newLocale);
        } else {
            configuration.setLocale(newLocale);
        }
        return context.createConfigurationContext(configuration);
    }

    /**
     * 更新locale
     * @param context               上下文
     * @param newLocale             新locale
     */
    public static void refreshLocale(Context context, String newLocale){
        Locale locale = LocaleUtils.tagToLocale(newLocale);
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            config.setLocales(localeList);
            LocaleList.setDefault(localeList);
            Locale.setDefault(locale);
        } else {
            config.locale = locale;
        }
        context.getResources().updateConfiguration(config, displayMetrics);
    }

    /**
     * 获取系统locale的tag
     * @return
     */
    public static String getSysLocaleTag() {
        Locale sysLocale = getSysLocale();
        String locale = localeToTag(sysLocale);
        return locale;
    }

    /**
     * 获取App的locale
     * @return
     */
    public static String getAppLocaleTag(Context context) {
        Locale appLocale = getAppLocale(context);
        String locale = localeToTag(appLocale);
        return locale;
    }

    public static String localeToTag(Locale locale) {
        LocaleListCompat localeListCompat = LocaleListCompat.create(locale);
        return localeListCompat.toLanguageTags();
    }

}
