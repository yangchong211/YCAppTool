package com.yc.appwifilib;

import static com.yc.appwifilib.WifiHelper.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

public final class WifiToolUtils {

    /**
     * 请求修改系统设置Code
     */
    public static final int REQUEST_WRITE_SETTING_CODE = 1001;

    /**
     * 查看授权情况, 开启热点需要申请系统设置修改权限，如有必要，可提前申请
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestWriteSettings(Activity activity) {
        if (isGrantedWriteSettings(activity)) {
            Log.d(TAG, "已授权修改系统设置权限");
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_WRITE_SETTING_CODE);
    }


    /**
     * 返回应用程序是否可以修改系统设置
     *
     * @return {@code true}: yes
     * {@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isGrantedWriteSettings(Context context) {
        return Settings.System.canWrite(context);
    }


}
