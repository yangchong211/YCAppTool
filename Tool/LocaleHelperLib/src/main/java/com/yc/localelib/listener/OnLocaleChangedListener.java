package com.yc.localelib.listener;

import java.util.Locale;

/**
 * 语种变化监听器
 */
public interface OnLocaleChangedListener {

    /**
     * 当前应用语种发生变化时回调
     *
     * @param oldLocale         旧语种
     * @param newLocale         新语种
     */
    void onAppLocaleChange(Locale oldLocale, Locale newLocale);

    /**
     * 手机系统语种发生变化时回调
     *
     * @param oldLocale         旧语种
     * @param newLocale         新语种
     */
    void onSystemLocaleChange(Locale oldLocale, Locale newLocale);
}