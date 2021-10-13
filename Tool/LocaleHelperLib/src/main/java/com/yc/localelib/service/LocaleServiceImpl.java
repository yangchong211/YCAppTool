package com.yc.localelib.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;

import com.yc.localelib.utils.LocaleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocaleServiceImpl implements LocaleServiceProvider {

    private static final String KEY_SP = "locale_sp";
    //current_locale，旧版locale与lang混用，为了兼容旧apk，这个key不变
    private static final String KEY_CURRENT_LANG = "current_locale";
    private static final String KEY_CURRENT_REAL_LOCALE = "current_real_locale";
    private static final String KEY_CURRENT_RLAB_LOCALE = "current_rlab_locale";
    private final ArrayList<OnLocaleChangedListener> mListeners = new ArrayList<>();
    private SharedPreferences mPreferences;
    private Context mContext;
    private boolean mIsInitial;
    /**
     * 当前语言包
     */
    private String lang = "";
    /**
     * 公参Locale
     */
    private String locale = "";
    /**
     * SDK内部进行查表的locale
     */
    private String rlab_locale;

    public List<Locale> getDefaultList() {
        List<Locale> defaultList = new ArrayList<>();
        defaultList.add(new Locale("en"));
        defaultList.add(new Locale("es"));
        defaultList.add(new Locale("pt"));
        defaultList.add(new Locale("ja"));
        defaultList.add(new Locale("zh"));
        return defaultList;
    }

    @Override
    public void init(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null!");
        }
        mIsInitial = true;
        mContext = context;
        mPreferences = context.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE);
        LocaleUtils.initConfigLocale();
    }

    @Override
    public Context attachBaseContext(Context context) {
        // 8.0需要使用createConfigurationContext处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // getSetLocale方法是获取新设置的语言
            Locale locale = LocaleUtils.tagToLocale(getRlabLocale());
            return LocaleUtils.updateLocale2(context,locale);
        } else {
            return context;
        }
    }

    @Override
    public Locale getCurrentLocale() {
        String localeFromPre = getLocaleFromPre();
        return LocaleUtils.tagToLocale(localeFromPre);
    }

    @Override
    public String getCurrentLocaleTag() {
        return getLocaleFromPre();
    }

    @Override
    public Locale getCurrentLang() {
        return LocaleUtils.tagToLocale(getLang());
    }

    @Override
    public String getCurrentLangTag() {
        return getLang();
    }

    @Override
    public List<Locale> getSupportLocaleList() {
        List<Locale> resultList = new ArrayList<>();
        resultList.addAll(getDefaultList());
        return resultList;
    }

    @Override
    public Locale switchLocale(Intent intent, Locale targetLocale) {
       return null;
    }

    @Override
    public Locale switchLocale(Intent intent, String targetLocaleTag) {
        return switchLocale(intent, LocaleUtils.tagToLocale(targetLocaleTag));
    }

    @Override
    public void addOnLocaleChangedListener(OnLocaleChangedListener listener) {

    }

    @Override
    public void removeOnLocaleChangedListener(OnLocaleChangedListener listener) {

    }

    @Override
    public List<OnLocaleChangedListener> getOnLocaleChangedListeners() {
        return null;
    }

    @Override
    public void refreshLocale(Context context) {
        // getSetLocale方法是获取新设置的语言
        LocaleUtils.refreshLocale(context,getRlabLocale());
    }

    private String getLocaleFromPre() {
        if (mPreferences == null) {
            return "";
        }
        String localeTag = mPreferences.getString(KEY_CURRENT_REAL_LOCALE, locale);
        if (!TextUtils.isEmpty(localeTag)) {
            localeTag = localeTag.replace("_", "-");
        }
        if (localeTag == null) {
            localeTag = "en-US";
        }
        return localeTag;
    }

    private String getRlabLocale() {
        if (mPreferences == null) {
            return "";
        }
        return mPreferences.getString(KEY_CURRENT_RLAB_LOCALE, lang)
                .replace("_", "-");
    }

    private String getLang() {
        if (mPreferences == null) {
            return "";
        }
        return mPreferences.getString(KEY_CURRENT_LANG, lang);
    }

}
