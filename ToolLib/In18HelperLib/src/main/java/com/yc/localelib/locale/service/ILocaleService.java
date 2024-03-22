package com.yc.localelib.locale.service;

import android.app.Application;
import android.content.Context;

import com.yc.localelib.locale.listener.OnLocaleChangedListener;

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
public interface ILocaleService {

    /**
     * 初始化操作
     *
     * @param context 上下文
     */
    void init(Application context);

    /**
     * 获取上下文
     *
     * @return 上下文Application对象
     */
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
    Locale getSystemLocale();

    /**
     * 判断是否是系统使用的locale
     *
     * @return true表示系统locale
     */
    boolean isSystemLocale();

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
     * <p>
     * 用于侧边栏选择语言 可能是从Apollo读出来的
     */
    List<Locale> getSupportLocaleList();

    /**
     * 跟随系统语种（返回 true 表示需要重启 App）
     *
     * @param context 上下文
     * @return 返回 true 表示需要重启 Ap
     */
    boolean clearAppLanguage(Context context);

    /**
     * 设置当前的语种（返回 true 表示需要重启 App）
     *
     * @param context   上下文
     * @param newLocale 新的locale
     * @return 返回 true 表示需要重启 Ap
     */
    boolean setAppLanguage(Context context, Locale newLocale);

    /**
     * 添加locale变化监听
     *
     * @param listener listener监听事件
     */
    void addOnLocaleChangedListener(OnLocaleChangedListener listener);

    /**
     * 移除locale变化监听
     *
     * @param listener listener监听事件
     */
    void removeOnLocaleChangedListener(OnLocaleChangedListener listener);

    /**
     * 获取locale变化监听集合数组
     *
     * @return list数组
     */
    List<OnLocaleChangedListener> getOnLocaleChangedListeners();

    /**
     * 更新locale操作
     *
     * @param context 上下文
     */
    void refreshLocale(Context context);

}
