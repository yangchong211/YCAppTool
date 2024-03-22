package com.yc.localelib.locale.listener;

import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 变化监听器
 *     revise :
 * </pre>
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