package com.yc.localelib.locale.observer;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.yc.localelib.locale.listener.OnLocaleChangedListener;
import com.yc.localelib.locale.service.LocaleService;
import com.yc.localelib.locale.utils.LocaleSpUtils;
import com.yc.localelib.locale.utils.LocaleToolUtils;

import java.util.List;
import java.util.Locale;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 语种变化监听
 *     revise :
 * </pre>
 */
public final class LanguagesObserver implements ComponentCallbacks {

    /** 系统语种 */
    private static volatile Locale mSystemLanguage;

    static {
        // 获取当前系统的语种
        Configuration configuration = Resources.getSystem().getConfiguration();
        if (configuration!=null){
            mSystemLanguage = LocaleToolUtils.getLocale(configuration);
        }
        if (mSystemLanguage == null){
            mSystemLanguage = new Locale("en-US");
        }
    }

    /**
     * 获取系统的语种
     */
    public static Locale getSystemLanguage() {
        return mSystemLanguage;
    }

    /**
     * 注册系统语种变化监听
     */
    public static void register(Application application) {
        application.registerComponentCallbacks(new LanguagesObserver());
    }

    /**
     * 手机的配置发生了变化
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Locale newLocale = LocaleToolUtils.getLocale(newConfig);

        Locale oldLocale = mSystemLanguage;

        // 更新 Application 的配置，否则会出现横竖屏切换之后 Application 的 orientation 没有随之变化的问题
        LocaleToolUtils.updateConfigurationChanged(LocaleService.getInstance().getApplication(), newConfig);

        if (newLocale.equals(oldLocale)) {
            return;
        }
        mSystemLanguage = newLocale;

        // 如果当前的语种是跟随系统变化的，那么就需要重置一下当前 App 的语种
        if (LocaleSpUtils.isSystemLanguage(LocaleService.getInstance().getApplication())) {
            LocaleSpUtils.clearLanguage(LocaleService.getInstance().getApplication());
        }

        List<OnLocaleChangedListener> listeners = LocaleService.getInstance().getOnLocaleChangedListeners();
        if (listeners != null && listeners.size()>0) {
            for (int i=0 ; i<listeners.size() ; i++){
                OnLocaleChangedListener listener = listeners.get(i);
                if (listener!=null){
                    listener.onSystemLocaleChange(oldLocale, newLocale);
                }
            }
        }
    }

    @Override
    public void onLowMemory() {}
}