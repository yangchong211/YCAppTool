package com.yc.localelib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * 国际化工具类
 */
public final class LanguagesUtils {

    /**
     * 获取语种对象
     */
    public static Locale getLocale(Context context) {
        return getLocale(context.getResources().getConfiguration());
    }

    public static Locale getLocale(Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        } else  {
            return config.locale;
        }
    }

    /**
     * 设置语种对象
     */
    public static void setLocale(Configuration config, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList localeList = new LocaleList(locale);
                config.setLocales(localeList);
            } else {
                config.setLocale(locale);
            }
        } else {
            config.locale = locale;
        }
    }

    /**
     * 设置默认的语种环境（日期格式化会用到）
     */
    public static void setDefaultLocale(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.setDefault(configuration.getLocales());
        } else {
            Locale.setDefault(configuration.locale);
        }
    }

    /**
     * 绑定当前 App 的语种
     */
    public static Context attachLanguages(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        setLocale(config, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return context;
    }

    /**
     * 更新 Resources 语种
     */
    public static void updateLanguages(Resources resources, Locale locale) {
        Configuration config = resources.getConfiguration();
        setLocale(config, locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * 更新手机配置信息变化
     */
    public static void updateConfigurationChanged(Context context, Configuration newConfig) {
        Configuration config = new Configuration(newConfig);
        // 绑定当前语种到这个新的配置对象中
        LanguagesUtils.setLocale(config, LocaleSpUtils.getAppLanguage(context));
        Resources resources = context.getResources();
        // 更新上下文的配置信息
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * 获取某个语种下的 Resources 对象
     */
    public static Resources getLanguageResources(Context context, Locale locale) {
        Configuration config = new Configuration();
        setLocale(config, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return context.createConfigurationContext(config).getResources();
        }
        return new Resources(context.getAssets(),
                context.getResources().getDisplayMetrics(), config);
    }
}