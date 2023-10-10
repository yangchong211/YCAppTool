package com.yc.heartbeatserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HeartReceiver extends BroadcastReceiver {

    private static final String TAG = HeartReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (HeartService.ACTION_HEART_BEAT.equals(action)) {
            if (true) {
                try {
                    //应用崩溃，启动应用
                    Intent toIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    context.startActivity(toIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    //应用崩溃，启动应用失败
                }
            } else {
                HeartService.start(context);
            }
        }
    }
}