package com.yc.catonhelperlib;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FloatPageManager {
    private static final String TAG = "FloatPageManager";
    private WindowManager mWindowManager;
    private Context mContext;
    private List<BaseFloatPage> mPages = new ArrayList();
    private List<FloatPageManager.FloatPageManagerListener> mListeners = new ArrayList();

    public FloatPageManager() {
    }

    public void notifyBackground() {
        Iterator<BaseFloatPage> iterator = mPages.iterator();
        while(iterator.hasNext()) {
            BaseFloatPage page = (BaseFloatPage)iterator.next();
            page.onEnterBackground();
        }

    }

    public void notifyForeground() {
        Iterator<BaseFloatPage> iterator = mPages.iterator();

        while(iterator.hasNext()) {
            BaseFloatPage page = (BaseFloatPage)iterator.next();
            page.onEnterForeground();
        }

    }

    public static FloatPageManager getInstance() {
        return FloatPageManager.Holder.INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        this.mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void add(PageIntent pageIntent) {
        try {
            if (pageIntent.targetClass == null) {
                return;
            }

            if (pageIntent.mode == 1) {
                Iterator<BaseFloatPage> iterator = mPages.iterator();

                while(iterator.hasNext()) {
                    BaseFloatPage page = (BaseFloatPage)iterator.next();
                    if (pageIntent.targetClass.isInstance(page)) {
                        return;
                    }
                }
            }

            BaseFloatPage page = (BaseFloatPage)pageIntent.targetClass.newInstance();
            page.setBundle(pageIntent.bundle);
            page.setTag(pageIntent.tag);
            this.mPages.add(page);
            page.performCreate(this.mContext);
            this.mWindowManager.addView(page.getRootView(), page.getLayoutParams());
            Iterator<FloatPageManagerListener> iterator = this.mListeners.iterator();

            while(iterator.hasNext()) {
                FloatPageManager.FloatPageManagerListener listener = (FloatPageManager.FloatPageManagerListener)iterator.next();
                listener.onPageAdd(page);
            }
        } catch (InstantiationException var5) {
            Log.e("FloatPageManager", var5.toString());
        } catch (IllegalAccessException var6) {
            Log.e("FloatPageManager", var6.toString());
        }

    }

    public void remove(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            Iterator<BaseFloatPage> iterator = this.mPages.iterator();

            BaseFloatPage page;
            do {
                if (!iterator.hasNext()) {
                    return;
                }

                page = (BaseFloatPage)iterator.next();
            } while(!tag.equals(page.getTag()));

            this.mWindowManager.removeView(page.getRootView());
            page.performDestroy();
            this.mPages.remove(page);
        }
    }

    public void remove(BaseFloatPage page) {
        this.mWindowManager.removeView(page.getRootView());
        page.performDestroy();
        this.mPages.remove(page);
    }

    public void removeAll(Class<? extends BaseFloatPage> pageClass) {
        Iterator<BaseFloatPage> it = mPages.iterator();

        while(it.hasNext()) {
            BaseFloatPage page = (BaseFloatPage)it.next();
            if (pageClass.isInstance(page)) {
                this.mWindowManager.removeView(page.getRootView());
                page.performDestroy();
                it.remove();
            }
        }

    }

    public boolean hasPage(Class<? extends BaseFloatPage> pageClass) {
        Iterator<BaseFloatPage> iterator = mPages.iterator();

        BaseFloatPage page;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            page = (BaseFloatPage)iterator.next();
        } while(!pageClass.isInstance(page));

        return true;
    }

    public void removeAll() {
        Iterator<BaseFloatPage> iterator = mPages.iterator();
        while(iterator.hasNext()) {
            BaseFloatPage page = (BaseFloatPage)iterator.next();
            this.mWindowManager.removeView(page.getRootView());
            page.performDestroy();
            iterator.remove();
        }

    }

    public BaseFloatPage getFloatPage(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return null;
        } else {
            Iterator<BaseFloatPage> iterator = mPages.iterator();

            BaseFloatPage page;
            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                page = (BaseFloatPage)iterator.next();
            } while(!tag.equals(page.getTag()));

            return page;
        }
    }

    public void addListener(FloatPageManager.FloatPageManagerListener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(FloatPageManager.FloatPageManagerListener listener) {
        this.mListeners.remove(listener);
    }

    public interface FloatPageManagerListener {
        void onPageAdd(BaseFloatPage var1);
    }

    private static class Holder {
        private static final FloatPageManager INSTANCE = new FloatPageManager();

        private Holder() {
        }
    }
}
