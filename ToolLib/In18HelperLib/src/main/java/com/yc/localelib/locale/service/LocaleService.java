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
 *     desc   : 服务
 *     revise :
 * </pre>
 */
public final class LocaleService implements ILocaleService {

    private final ILocaleService mDelegate = new LocaleServiceImpl();

    private LocaleService() {

    }

    public static synchronized LocaleService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public final void init(final Application context) {
        if (null != this.mDelegate) {
            this.mDelegate.init(context);
        }
    }

    @Override
    public Application getApplication() {
        return null != this.mDelegate ? this.mDelegate.getApplication() : null;
    }

    @Override
    public final Context attachBaseContext(final Context context) {
        return null != this.mDelegate ? this.mDelegate.attachBaseContext(context) : null;
    }

    @Override
    public final Locale getCurrentLocale() {
        return null != this.mDelegate ? this.mDelegate.getCurrentLocale() : null;
    }

    @Override
    public Locale getSystemLocale() {
        return null != this.mDelegate ? this.mDelegate.getSystemLocale() : null;
    }

    @Override
    public boolean isSystemLocale() {
        return false;
    }

    @Override
    public final String getCurrentLocaleTag() {
        return null != this.mDelegate ? this.mDelegate.getCurrentLocaleTag() : null;
    }

    @Override
    public final Locale getCurrentLang() {
        return null != this.mDelegate ? this.mDelegate.getCurrentLang() : null;
    }

    @Override
    public final String getCurrentLangTag() {
        return null != this.mDelegate ? this.mDelegate.getCurrentLangTag() : null;
    }

    @Override
    public final List<Locale> getSupportLocaleList() {
        return null != this.mDelegate ? this.mDelegate.getSupportLocaleList() : null;
    }

    @Override
    public boolean clearAppLanguage(Context context) {
        return null != this.mDelegate ? this.mDelegate.clearAppLanguage(context) : false;
    }

    @Override
    public boolean setAppLanguage(Context context, Locale newLocale) {
        return null != this.mDelegate ? this.mDelegate.setAppLanguage(context,newLocale) : false;
    }

    @Override
    public final void addOnLocaleChangedListener(final OnLocaleChangedListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.addOnLocaleChangedListener(arg0);
        }
    }

    @Override
    public final void removeOnLocaleChangedListener(final OnLocaleChangedListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.removeOnLocaleChangedListener(arg0);
        }
    }

    @Override
    public final List<OnLocaleChangedListener> getOnLocaleChangedListeners() {
        return null != this.mDelegate ? this.mDelegate.getOnLocaleChangedListeners() : null;
    }

    @Override
    public final void refreshLocale(final Context context) {
        if (null != this.mDelegate) {
            this.mDelegate.refreshLocale(context);
        }
    }

    private static final class Singleton {
        static final LocaleService INSTANCE = new LocaleService();
    }
}

