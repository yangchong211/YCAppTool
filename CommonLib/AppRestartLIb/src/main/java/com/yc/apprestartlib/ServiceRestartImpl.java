package com.yc.apprestartlib;

import android.content.Context;
import android.content.Intent;

import com.yc.activitymanager.ActivityManager;
import com.yc.toolutils.AppLogUtils;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 重启APP接口，使用service方式重启实现
 *     revise:
 * </pre>
 */
public class ServiceRestartImpl implements IRestartApp {

    @Override
    public void restartApp(Context context) {
        Intent intent = new Intent(context, KillSelfService.class);
        intent.putExtra("PackageName",context.getPackageName());
        intent.putExtra("Delayed",2000);
        context.startService(intent);
        AppLogUtils.d("KillSelfService: ", "restart app");
        ActivityManager.getInstance().killCurrentProcess(false);
    }
}
