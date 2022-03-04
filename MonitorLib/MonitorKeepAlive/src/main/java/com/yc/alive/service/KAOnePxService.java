package com.yc.alive.service;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.yc.alive.ui.KAOnePxActivity;
import com.yc.alive.util.AliveLogUtils;
import com.yc.alive.util.KAStringUtils;

import java.lang.ref.WeakReference;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 1 像素 服务
 */
@RestrictTo(LIBRARY)
public class KAOnePxService extends Service {

    private static final String TAG = "KAOnePxService";

    private final IBinder mBinder = new MyBinder();
    private ScreenBroadcastReceiver mReceiver;
    private WeakReference<Activity> mOnePxActRef;

    public static void launch(Context context) {
        try {
            // OPPO 4.x 手机不支持 startService
            Intent intent = new Intent(context, KAOnePxService.class);
            context.startService(intent);
        } catch (Exception e) {
            AliveLogUtils.d(TAG, "KAOnePxService startService fail " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, KAOnePxService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AliveLogUtils.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AliveLogUtils.d(TAG, "onStartCommand");

        unregister();

        try {
            mReceiver = new ScreenBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            registerReceiver(mReceiver, filter);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AliveLogUtils.d(TAG, "onDestroy");
        unregister();
    }

    private void unregister() {
        AliveLogUtils.d(TAG, "unregister");

        try {
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void bindActivity(Activity activity) {
        mOnePxActRef = new WeakReference<>(activity);
    }

    public boolean needFinish() {
        if (mReceiver == null) {
            return true;
        }
        return mReceiver.needFinish();
    }

    private void startActivity() {
        KAOnePxActivity.launch(getApplicationContext());
    }

    private void finishActivity() {
        if (mOnePxActRef == null) {
            return;
        }
        Activity activity = mOnePxActRef.get();
        if (activity == null) {
            return;
        }
        activity.finish();
    }

    /**
     * 锁屏状态
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        private static final String TAG = "ScreenBroadcastReceiver";

        private int lastScreenType = -1;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }

            AliveLogUtils.d(TAG, "onReceive " + intent.toString() + ", lastScreenType " + lastScreenType);

            String action = intent.getAction();

            if (KAStringUtils.isEmpty(action)) {
                return;
            }

            if (Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_USER_PRESENT.equals(action)) {
                lastScreenType = 1;
                // 开屏
                AliveLogUtils.d(TAG, "onReceive finishActivity");
                finishActivity();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                if (lastScreenType == 2) {
                    // 防止有的手机出现重复的问题
                    return;
                }
                lastScreenType = 2;
                // 锁屏
                AliveLogUtils.d(TAG, "onReceive startActivity");
                startActivity();
            }
        }

        public boolean needFinish() {
            return lastScreenType == 1;
        }
    }

    public class MyBinder extends Binder {
        public KAOnePxService getService() {
            return KAOnePxService.this;
        }
    }

}
