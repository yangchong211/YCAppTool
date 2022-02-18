package com.yc.localelib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import androidx.core.os.LocaleListCompat;
import android.util.DisplayMetrics;

import java.util.Locale;

public final class LocaleToolUtils {

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
            //大于24
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
     * 获取默认的locale
     * @return                          Locale对象
     */
    @Deprecated
    public static Locale getDefaultLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0);
        } else {
            return Locale.getDefault();
        }
    }


    public static Context attachBaseContext(Context context, Locale newLocale){
        if (newLocale == null){
            //如果配置里locale为空，则获取app本身的locale
            newLocale = LocaleToolUtils.getAppLocale(context.getApplicationContext());
        }
        return setLocale(context, newLocale);
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
    public static Context setLocale(Context context, Locale newLocale){
        Resources resources = context.getApplicationContext() == null
                ? context.getResources()
                : context.getApplicationContext().getResources();
        Configuration configuration = resources.getConfiguration();
        Locale.setDefault(newLocale);
        Context newBase;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            configuration.setLocales(localeList);
            LocaleList.setDefault(localeList);
            newBase = context.createConfigurationContext(configuration);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //7.0以上
                configuration.setLocale(newLocale);
                newBase = context.createConfigurationContext(configuration);
            } else {
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                context.getResources().updateConfiguration(configuration, displayMetrics);
                newBase = context;
            }
        }
        return newBase;
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

    public static String localeToTag(Locale locale) {
        LocaleListCompat localeListCompat = LocaleListCompat.create(locale);
        return localeListCompat.toLanguageTags();
    }

}
