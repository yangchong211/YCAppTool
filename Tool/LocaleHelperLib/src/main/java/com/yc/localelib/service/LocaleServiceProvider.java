package com.yc.localelib.service;

import android.content.Context;
import android.content.Intent;
import java.util.List;
import java.util.Locale;


public interface LocaleServiceProvider {

    void init(Context context);

    /**
     * 用这个方法替换 Activity 的 attachBaseContext 内部处理了 7.0 及之后版本切换 locale 的逻辑
     *
     * @param context context
     * @return N 版本及之后版本，返回 ConfigurationContext，之前版本直接返回原本的 context
     */
    Context attachBaseContext(Context context);

    /**
     * 获取当前 app 使用的 locale
     *
     * @return locale
     */
    Locale getCurrentLocale();

    /**
     * 获取当前 app 使用的 locale tag
     *
     * @return locale tag
     */
    String getCurrentLocaleTag();

    /**
     * 获取当前 app 使用的 lang
     *
     * @return lang
     */
    Locale getCurrentLang();

    /**
     * 获取当前 app 使用的 lang tag
     *
     * @return lang tag
     */
    String getCurrentLangTag();

    /**
     * 获取支持的语言列表
     *
     * 用于侧边栏选择语言 可能是从Apollo读出来的
     */
    List<Locale> getSupportLocaleList();

    /**
     * @param intent 需要重启的 activity 的 intent，一般为初始页面；如果不传，则不重启，只修改 configuration
     * @param targetLocale 需要切换到的 locale
     * @return 切换后的 locale tag
     */
    Locale switchLocale(Intent intent, Locale targetLocale);

    /**
     * @param intent 需要重启的 activity 的 intent，一般为初始页面；如果不传，则不重启，只修改 configuration
     * @param targetLocaleTag 需要切换到的 locale tag
     * @return 切换后的 locale tag
     */
    Locale switchLocale(Intent intent, String targetLocaleTag);

    void addOnLocaleChangedListener(OnLocaleChangedListener listener);

    void removeOnLocaleChangedListener(OnLocaleChangedListener listener);

    List<OnLocaleChangedListener> getOnLocaleChangedListeners();

    void refreshLocale(Context context);

    interface OnLocaleChangedListener {
        void onLocaleChanged(Locale oldLocale, Locale newLocale);
    }


}
