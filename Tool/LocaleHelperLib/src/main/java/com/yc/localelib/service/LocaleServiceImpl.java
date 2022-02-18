package com.yc.localelib.service;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.yc.localelib.utils.LocaleToolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocaleServiceImpl implements LocaleServiceProvider {


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
    }

    @Override
    public Context attachBaseContext(Context context) {
        // 8.0需要使用createConfigurationContext处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // getSetLocale方法是获取新设置的语言
            Locale locale = LocaleToolUtils.tagToLocale(getRlabLocale());
            return LocaleToolUtils.setLocale(context,locale);
        } else {
            return context;
        }
    }

    @Override
    public Locale getCurrentLocale() {
        String localeFromPre = getLocaleFromPre();
        return LocaleToolUtils.tagToLocale(localeFromPre);
    }

    @Override
    public String getCurrentLocaleTag() {
        return getLocaleFromPre();
    }

    @Override
    public Locale getCurrentLang() {
        return LocaleToolUtils.tagToLocale(getLang());
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
        return switchLocale(intent, LocaleToolUtils.tagToLocale(targetLocaleTag));
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

    }

    private String getLocaleFromPre() {
        return "";
    }

    private String getRlabLocale() {
        return "";
    }

    private String getLang() {
        return "";
    }

}
