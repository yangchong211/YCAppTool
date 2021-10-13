package com.yc.localelib.service;

import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.Locale;

public final class LocaleService implements LocaleServiceProvider {

    private final LocaleServiceProvider mDelegate = new LocaleServiceImpl();

    private LocaleService() {

    }

    public static final LocaleService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public final void init(final Context arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.init(arg0);
        }
    }

    @Override
    public final Context attachBaseContext(final Context arg0) {
        return null != this.mDelegate ? this.mDelegate.attachBaseContext(arg0) : null;
    }

    @Override
    public final Locale getCurrentLocale() {
        return null != this.mDelegate ? this.mDelegate.getCurrentLocale() : null;
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
    public final Locale switchLocale(final Intent arg0, final Locale arg1) {
        return null != this.mDelegate ? this.mDelegate.switchLocale(arg0, arg1) : null;
    }

    @Override
    public final Locale switchLocale(final Intent arg0, final String arg1) {
        return null != this.mDelegate ? this.mDelegate.switchLocale(arg0, arg1) : null;
    }

    @Override
    public final void addOnLocaleChangedListener(final LocaleServiceProvider.OnLocaleChangedListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.addOnLocaleChangedListener(arg0);
        }
    }

    @Override
    public final void removeOnLocaleChangedListener(final LocaleServiceProvider.OnLocaleChangedListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.removeOnLocaleChangedListener(arg0);
        }
    }

    @Override
    public final List<LocaleServiceProvider.OnLocaleChangedListener> getOnLocaleChangedListeners() {
        return null != this.mDelegate ? this.mDelegate.getOnLocaleChangedListeners() : null;
    }

    @Override
    public final void refreshLocale(final Context arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.refreshLocale(arg0);
        }
    }

    private static final class Singleton {
        static final LocaleService INSTANCE = new LocaleService();
    }
}

