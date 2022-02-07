package com.yc.catonhelperlib.fps;


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
        Iterator var1 = this.mPages.iterator();

        while(var1.hasNext()) {
            BaseFloatPage page = (BaseFloatPage)var1.next();
            page.onEnterBackground();
        }

    }

    public void notifyForeground() {
        Iterator var1 = this.mPages.iterator();

        while(var1.hasNext()) {
            BaseFloatPage page = (BaseFloatPage)var1.next();
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
                Iterator var2 = this.mPages.iterator();

                while(var2.hasNext()) {
                    BaseFloatPage page = (BaseFloatPage)var2.next();
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
            Iterator var8 = this.mListeners.iterator();

            while(var8.hasNext()) {
                FloatPageManager.FloatPageManagerListener listener = (FloatPageManager.FloatPageManagerListener)var8.next();
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
            Iterator var2 = this.mPages.iterator();

            BaseFloatPage page;
            do {
                if (!var2.hasNext()) {
                    return;
                }

                page = (BaseFloatPage)var2.next();
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
        Iterator it = this.mPages.iterator();

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
        Iterator it = this.mPages.iterator();

        BaseFloatPage page;
        do {
            if (!it.hasNext()) {
                return false;
            }

            page = (BaseFloatPage)it.next();
        } while(!pageClass.isInstance(page));

        return true;
    }

    public void removeAll() {
        Iterator it = this.mPages.iterator();

        while(it.hasNext()) {
            BaseFloatPage page = (BaseFloatPage)it.next();
            this.mWindowManager.removeView(page.getRootView());
            page.performDestroy();
            it.remove();
        }

    }

    public BaseFloatPage getFloatPage(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return null;
        } else {
            Iterator var2 = this.mPages.iterator();

            BaseFloatPage page;
            do {
                if (!var2.hasNext()) {
                    return null;
                }

                page = (BaseFloatPage)var2.next();
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
        private static FloatPageManager INSTANCE = new FloatPageManager();

        private Holder() {
        }
    }
}
