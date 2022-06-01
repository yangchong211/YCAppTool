package com.yc.apprestartlib;

import android.content.Context;
import android.content.Intent;

import com.yc.activitymanager.ActivityManager;
import com.yc.toolutils.AppLogUtils;

public class ServiceRestartImpl implements IRestartApp {

    @Override
    public void reStartApp(Context context) {
        Intent intent = new Intent(context, KillSelfService.class);
        intent.putExtra("PackageName",context.getPackageName());
        intent.putExtra("Delayed",2000);
        context.startService(intent);
        AppLogUtils.w("ServiceRestartImpl", "restart app");
        ActivityManager.getInstance().killCurrentProcess(true);
    }
}
