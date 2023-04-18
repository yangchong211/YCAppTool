package com.yc.logclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.yc.toolutils.AppLogUtils;

public final class LogClientUtils {

    private static final String TAG = LogClientUtils.class.getSimpleName();
    /**
     * 包名简称:去最后一个.分割的单词，如com.didi.food,包名简称为food
     */
    private static String mPackageShortName;
    /**
     * 计算包名,只保留最后一个字段
     */
    private static String shortPackageName = "";

    public static Intent getLogServiceIntent(Context context) {
        String packageName = getPackageName(context);
        //通过Intent指定服务端的服务名称和所在包，与远程Service进行绑定
        //参数与服务器端的action要一致,即"服务器包名.aidl接口文件名"
        Intent intent = new Intent("com.yc.logservice.action.log");
        intent.setComponent(new ComponentName(packageName, "com.yc.logservice.LogService"));
        return intent;
    }

    public static String getShortPackageName(Context context) {
        if (TextUtils.isEmpty(shortPackageName)) {
            shortPackageName = getPackageName(context).substring(getPackageName(context).lastIndexOf(".") + 1);
            Log.i(TAG, " get short package name : " + shortPackageName);
        }
        return shortPackageName;
    }

    public static String getPackageName(Context context) {
        if (mPackageShortName == null) {
            mPackageShortName = context.getPackageName();
            AppLogUtils.d(TAG, "get package name:" + mPackageShortName);
        }
        return mPackageShortName;
    }

}
