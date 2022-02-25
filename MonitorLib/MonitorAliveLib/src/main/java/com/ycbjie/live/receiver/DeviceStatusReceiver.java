package com.ycbjie.live.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ycbjie.live.activity.OnePixelActivity;
import com.ycbjie.live.alive.YcKeepAlive;
import com.ycbjie.live.constant.YcConstant;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 监听设备状态，包括屏幕亮、屏幕关闭、点击home键、最近活动列表等
 *     revise:
 * </pre>
 */
public final class DeviceStatusReceiver extends BroadcastReceiver {

    private Handler mHandler;
    private boolean mScreenOn = true;

    public DeviceStatusReceiver() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            //屏幕关闭的时候接受到广播
            mScreenOn = false;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mScreenOn) {
                        Intent intent2 = new Intent(context, OnePixelActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
                        try {
                            pendingIntent.send();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 1000);
            //通知屏幕已关闭，开始播放无声音乐
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(YcConstant.KEEP_ACTION_OPEN_MUSIC));
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            //屏幕打开的时候发送广播  结束一像素
            mScreenOn = true;
            //通知屏幕已点亮，停止播放无声音乐
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(YcConstant.KEEP_ACTION_CLOSE_MUSIC));
        } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
            String reason = intent.getStringExtra("reason");
            if (YcKeepAlive.sRunMode == YcKeepAlive.RunMode.ROGUE) {
                if ("recentapps".equals(reason) || "homekey".equals(reason)) {
                    //流氓模式，点击了最近任务列表和home键也播放音乐
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(YcConstant.KEEP_ACTION_OPEN_MUSIC_ONCE));
                }
            }
        }
    }
}
