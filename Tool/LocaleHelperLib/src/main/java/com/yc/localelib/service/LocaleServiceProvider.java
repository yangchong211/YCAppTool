package com.yc.localelib.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.yc.localelib.listener.OnLocaleChangedListener;

import java.util.List;
import java.util.Locale;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 服务接口
 *     revise :
 * </pre>
 */
public interface LocaleServiceProvider {

    void init(Application context);


    Application getApplication();

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
     * 获取当前 系统 使用的 locale
     *
     * @return locale
     */
    Locale getSystemLanguage();

    boolean isSystemLanguage();
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

    boolean clearAppLanguage(Context context);

    boolean setAppLanguage(Context context, Locale newLocale);

    void addOnLocaleChangedListener(OnLocaleChangedListener listener);

    void removeOnLocaleChangedListener(OnLocaleChangedListener listener);

    List<OnLocaleChangedListener> getOnLocaleChangedListeners();

    void refreshLocale(Context context);

}
