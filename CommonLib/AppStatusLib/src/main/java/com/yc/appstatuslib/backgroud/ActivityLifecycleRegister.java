package com.yc.appstatuslib.backgroud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ActivityLifecycleRegister {

    private static final Queue<ActivityLifecycleListener> mListeners = new ConcurrentLinkedQueue<>();

    public ActivityLifecycleRegister() {
    }

    public static void addActivityLifecycleListener(ActivityLifecycleRegister.ActivityLifecycleListener listener) {
        mListeners.add(listener);
    }

    @TargetApi(14)
    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Iterator var3 = ActivityLifecycleRegister.mListeners.iterator();

                while(var3.hasNext()) {
                    ActivityLifecycleRegister.ActivityLifecycleListener listener = (ActivityLifecycleRegister.ActivityLifecycleListener)var3.next();
                    listener.onActivityCreated(activity, bundle);
                }

            }

            public void onActivityDestroyed(Activity activity) {
                Iterator var2 = ActivityLifecycleRegister.mListeners.iterator();

                while(var2.hasNext()) {
                    ActivityLifecycleRegister.ActivityLifecycleListener listener = (ActivityLifecycleRegister.ActivityLifecycleListener)var2.next();
                    listener.onActivityDestroyed(activity);
                }

            }

            public void onActivityStarted(Activity activity) {
                Iterator var2 = ActivityLifecycleRegister.mListeners.iterator();

                while(var2.hasNext()) {
                    ActivityLifecycleRegister.ActivityLifecycleListener listener = (ActivityLifecycleRegister.ActivityLifecycleListener)var2.next();
                    listener.onActivityStarted(activity);
                }

            }

            public void onActivityStopped(Activity activity) {
                Iterator var2 = ActivityLifecycleRegister.mListeners.iterator();

                while(var2.hasNext()) {
                    ActivityLifecycleRegister.ActivityLifecycleListener listener = (ActivityLifecycleRegister.ActivityLifecycleListener)var2.next();
                    listener.onActivityStopped(activity);
                }

            }

            public void onActivityResumed(Activity activity) {
                Iterator var2 = ActivityLifecycleRegister.mListeners.iterator();

                while(var2.hasNext()) {
                    ActivityLifecycleRegister.ActivityLifecycleListener listener = (ActivityLifecycleRegister.ActivityLifecycleListener)var2.next();
                    listener.onActivityResumed(activity);
                }

            }

            public void onActivityPaused(Activity activity) {
                Iterator var2 = ActivityLifecycleRegister.mListeners.iterator();

                while(var2.hasNext()) {
                    ActivityLifecycleRegister.ActivityLifecycleListener listener = (ActivityLifecycleRegister.ActivityLifecycleListener)var2.next();
                    listener.onActivityPaused(activity);
                }

            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
        addActivityLifecycleListener(AppStateMonitor.getInstance());
    }

    public interface ActivityLifecycleListener {
        void onActivityCreated(Activity var1, Bundle var2);

        void onActivityDestroyed(Activity var1);

        void onActivityStarted(Activity var1);

        void onActivityStopped(Activity var1);

        void onActivityResumed(Activity var1);

        void onActivityPaused(Activity var1);
    }
}

