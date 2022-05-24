package com.yc.apprestartlib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yc.baseclasslib.activity.ActivityManager;
import com.yc.toolutils.AppLogUtils;

public class AlarmRestartImpl implements IRestartApp {


    @Override
    public void reStartApp(Context context) {
        //finishActivity();
        //Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Intent intent = new Intent(context.getApplicationContext(), null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);*/
        if (intent.getComponent() != null) {
            //如果类名已经设置，我们强制它模拟启动器启动。
            //如果我们不这样做，如果你从错误活动重启，然后按home，
            //然后从启动器启动活动，主活动将在backstack上出现两次。
            //这很可能不会有任何有害的影响，因为如果你设置了Intent组件，
            //if将始终启动，而不考虑此处指定的操作。
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        //为何用PendingIntent，不能Intent
        PendingIntent restartIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, intent,PendingIntent.FLAG_ONE_SHOT);
        //退出程序
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,restartIntent);
        AppLogUtils.w("AlarmRestartImpl", "reStartApp--- 用来重启本APP--2---");
        //exitApp();
        ActivityManager.getInstance().killCurrentProcess(true);
    }
}
