package com.yc.alive.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.yc.alive.alive.YcKeepAlive;
import com.yc.alive.constant.YcConstant;
import com.yc.alive.receiver.NotificationClickReceiver;
import com.yc.alive.utils.NotificationUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 通过相同的notification_id来隐藏前台服务通知
 *     revise:
 * </pre>
 */
public class HideForegroundService extends Service {

    private Handler mHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        if (mHandler == null){
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopForeground(true);
                stopSelf();
            }
        }, 2000);
        return START_NOT_STICKY;
    }


    private void startForeground() {
        if (YcKeepAlive.sForegroundNotification != null) {
            Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            intent.setAction(YcConstant.ACTION_CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(
                    this, YcKeepAlive.sForegroundNotification.getTitle(),
                    YcKeepAlive.sForegroundNotification.getDescription(),
                    YcKeepAlive.sForegroundNotification.getIconRes(),
                    intent);
            startForeground(YcConstant.KEY_NOTIFICATION_ID, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
