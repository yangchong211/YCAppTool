package com.yc.localelib.locale.service;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.yc.localelib.locale.listener.OnLocaleChangedListener;
import com.yc.localelib.locale.observer.ActivityCallback;
import com.yc.localelib.locale.observer.LanguagesObserver;
import com.yc.localelib.locale.utils.LocaleSpUtils;
import com.yc.localelib.locale.utils.LocaleToolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 服务接口实现具体类
 *     revise :
 * </pre>
 */
public class LocaleServiceImpl implements ILocaleService {

    /**
     * 应用上下文对象
     */
    private static Application sApplication;
    private final List<OnLocaleChangedListener> listeners = new ArrayList<>();
    private final AtomicBoolean isInit = new AtomicBoolean(false);

    /**
     * 获取应用上下文
     */
    @Override
    public Application getApplication() {
        return sApplication;
    }

    @Override
    public void init(Application context) {
        if (context == null) {
            throw new NullPointerException("context is null!");
        }
        if (!isInit.get()){
            init(context, false);
            isInit.set(true);
        }
    }

    private void init(Application application, boolean inject) {
        sApplication = application;
        LanguagesObserver.register(application);
        LocaleToolUtils.setDefaultLocale(application);
        if (inject) {
            ActivityCallback.inject(application);
        }
    }

    /**
     * 在上下文的子类中重写 attachBaseContext 方法（用于更新 Context 的语种）
     */
    @Override
    public Context attachBaseContext(Context context) {
        // 8.0需要使用createConfigurationContext处理
        if (LocaleToolUtils.getLocale(context).equals(LocaleSpUtils.getAppLanguage(context))) {
            return context;
        }
        return LocaleToolUtils.attachLanguages(context, LocaleSpUtils.getAppLanguage(context));
    }

    @Override
    public Locale getCurrentLocale() {
        return LocaleSpUtils.getAppLanguage(sApplication);
    }

    /**
     * 获取系统的语种
     */
    @Override
    public Locale getSystemLocale() {
        return LanguagesObserver.getSystemLanguage();
    }

    /**
     * 是否跟随系统的语种
     */
    @Override
    public boolean isSystemLocale() {
        return LocaleSpUtils.isSystemLanguage(sApplication);
    }

    @Override
    public String getCurrentLocaleTag() {
        return "";
    }

    @Override
    public Locale getCurrentLang() {
        return null;
    }

    @Override
    public String getCurrentLangTag() {
        return "";
    }

    @Override
    public List<Locale> getSupportLocaleList() {
        List<Locale> resultList = new ArrayList<>();
        return resultList;
    }

    /**
     * 跟随系统语种
     *
     * @return              语种是否发生改变了
     */
    @Override
    public boolean clearAppLanguage(Context context) {
        LocaleSpUtils.clearLanguage(context);
        if (LocaleToolUtils.getLocale(context).equals(getSystemLocale())) {
            return false;
        }

        LocaleToolUtils.updateLanguages(context.getResources(), getSystemLocale());
        LocaleToolUtils.setDefaultLocale(context);
        if (context != sApplication) {
            // 更新 Application 的语种
            LocaleToolUtils.updateLanguages(sApplication.getResources(), getSystemLocale());
        }
        return true;
    }

    /**
     * 设置 App 的语种
     *
     * @return              语种是否发生改变了
     */
    @Override
    public boolean setAppLanguage(Context context, Locale newLocale) {
        LocaleSpUtils.setAppLanguage(context, newLocale);
        if (LocaleToolUtils.getLocale(context).equals(newLocale)) {
            return false;
        }

        Locale oldLocale = LocaleToolUtils.getLocale(context);
        // 更新 Context 的语种
        LocaleToolUtils.updateLanguages(context.getResources(), newLocale);
        if (context != sApplication) {
            // 更新 Application 的语种
            LocaleToolUtils.updateLanguages(sApplication.getResources(), newLocale);
        }

        LocaleToolUtils.setDefaultLocale(context);
        List<OnLocaleChangedListener> onLocaleChangedListeners = getOnLocaleChangedListeners();
        if (onLocaleChangedListeners!=null && onLocaleChangedListeners.size()>0){
            for (int i=0 ; i<onLocaleChangedListeners.size() ; i++){
                OnLocaleChangedListener onLocaleChangedListener = onLocaleChangedListeners.get(i);
                if (onLocaleChangedListener != null) {
                    onLocaleChangedListener.onAppLocaleChange(oldLocale, newLocale);
                }
            }
        }
        return true;
    }

    @Override
    public void addOnLocaleChangedListener(OnLocaleChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeOnLocaleChangedListener(OnLocaleChangedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public List<OnLocaleChangedListener> getOnLocaleChangedListeners() {
        return listeners;
    }

    @Override
    public void refreshLocale(Context context) {
        updateAppLanguage(context.getResources());
    }

    /**
     * 更新 Resources 的语种
     */
    public void updateAppLanguage(Resources resources) {
        if (resources == null) {
            return;
        }
        if (LocaleToolUtils.getLocale(resources.getConfiguration()).equals(getCurrentLocale())) {
            return;
        }
        LocaleToolUtils.updateLanguages(resources, getCurrentLocale());
    }


}
