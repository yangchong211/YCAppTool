package com.yc.looperthread.heart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HeartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        HeartManager.getInstance().log("服务广播被调用");
        String action = intent.getAction();
        if (HeartService.ACTION_HEART_BEAT.equals(action)) {
            try {
                HeartManager.getInstance().log("应用崩溃，启动应用");
                String packageName = context.getPackageName();
                Intent toIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(toIntent);
            } catch (Exception e) {
                e.printStackTrace();
                HeartManager.getInstance().log("应用崩溃，启动应用失败：" + e);
            }
        }
    }
}