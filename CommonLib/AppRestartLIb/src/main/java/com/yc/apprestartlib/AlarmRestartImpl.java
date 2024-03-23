package com.yc.apprestartlib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yc.activitymanager.activity.ActivityManager;
import com.yc.toolutils.system.AppLogUtils;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 重启APP接口，闹钟方式重启实现
 *     revise:
 * </pre>
 */
public class AlarmRestartImpl implements IRestartProduct {

    public AlarmRestartImpl(){

    }

    @Override
    public void restartApp(Context context) {
        String packageName = context.getPackageName();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        //这种方式会黑屏一下
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        //        | Intent.FLAG_ACTIVITY_CLEAR_TASK |
        //        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.getComponent() != null) {
            //如果类名已经设置，我们强制它模拟启动器启动。
            //如果我们不这样做，如果你从错误活动重启，然后按home，
            //然后从启动器启动活动，主活动将在backstack上出现两次。
            //这很可能不会有任何有害的影响，因为如果你设置了Intent组件，
            //if将始终启动，而不考虑此处指定的操作。
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        //为何用PendingIntent，不能Intent，因为PendingIntent其实是一种延迟intent
        PendingIntent restartIntent = PendingIntent.getActivity(
                context.getApplicationContext(),
                0, intent,PendingIntent.FLAG_ONE_SHOT);
        //退出程序
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,restartIntent);
        AppLogUtils.w("IRestartApp:", "restart app use alarm");
        ActivityManager.getInstance().killCurrentProcess(false);
    }
}
