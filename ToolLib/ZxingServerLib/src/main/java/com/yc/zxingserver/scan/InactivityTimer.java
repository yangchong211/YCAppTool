package com.yc.zxingserver.scan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;

import com.yc.toolutils.system.AppLogUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.RejectedExecutionException;

/**
 * Finishes an activity after a period of inactivity if the device is on battery power.
 */
final class InactivityTimer {

    private static final long INACTIVITY_DELAY_MS = 5 * 60 * 1000L;

    private final Activity activity;
    private final BroadcastReceiver powerStatusReceiver;
    private boolean registered;
    private AsyncTask<Object,Object,Object> inactivityTask;

    InactivityTimer(Activity activity) {
        this.activity = activity;
        powerStatusReceiver = new PowerStatusReceiver(this);
        registered = false;
        onActivity();
    }

    void onActivity() {
        cancel();
        inactivityTask = new InactivityAsyncTask(activity);
        try {
            inactivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (RejectedExecutionException ree) {
            AppLogUtils.w( "Couldn't schedule inactivity task; ignoring");
        }
    }

    void onPause() {
        cancel();
        if (registered) {
            activity.unregisterReceiver(powerStatusReceiver);
            registered = false;
        } else {
            AppLogUtils.w( "PowerStatusReceiver was never registered?");
        }
    }

     void onResume() {
        if (registered) {
            AppLogUtils.w( "PowerStatusReceiver was already registered?");
        } else {
            activity.registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            registered = true;
        }
        onActivity();
    }

    private void cancel() {
        AsyncTask<?,?,?> task = inactivityTask;
        if (task != null) {
            task.cancel(true);
            inactivityTask = null;
        }
    }

    void shutdown() {
        cancel();
    }

    private static class PowerStatusReceiver extends BroadcastReceiver {

        private WeakReference<InactivityTimer> weakReference;

        public PowerStatusReceiver(InactivityTimer inactivityTimer){
            weakReference = new WeakReference<>(inactivityTimer);
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                // 0 indicates that we're on battery

                InactivityTimer inactivityTimer = weakReference.get();
                if(inactivityTimer!=null){
                    boolean onBatteryNow = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) <= 0;
                    if (onBatteryNow) {
                        inactivityTimer.onActivity();
                    } else {
                        inactivityTimer.cancel();
                    }
                }

            }
        }
    }

    private static class InactivityAsyncTask extends AsyncTask<Object,Object,Object> {

        private WeakReference<Activity> weakReference;

        public InactivityAsyncTask(Activity activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                Thread.sleep(INACTIVITY_DELAY_MS);
                AppLogUtils.i( "Finishing activity due to inactivity");
                Activity activity = weakReference.get();
                if(activity!=null){
                    activity.finish();
                }
            } catch (InterruptedException e) {
                // continue without killing
            }
            return null;
        }
    }

}