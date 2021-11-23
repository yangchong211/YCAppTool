package com.yc.location.utils;

import android.os.SystemClock;

import com.yc.location.log.LogHelper;

public final class AppToolUtils {

    private static int iSdk = 0;

    /**
     * 获取系统自启用以来的时间（单位：毫秒）
     * @return                  long时间戳
     */
    public static long getTimeBoot() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * 获取当前时间
     * @return                  当前时间戳
     */
    public static long getNowTime(){
        return System.currentTimeMillis();
    }


    /**
     * 获取手机SDK版本
     * @return                  sdk版本号
     */
    public static int getSdk() {
        if (iSdk > 0) {
            return iSdk;
        }
        int iLv = 0;
        String strVersion = "android.os.Build$VERSION";
        try {
            iLv = ReflectUtils.getStaticIntProp(strVersion, "SDK_INT");
        } catch (Exception e) {
            try {
                Object obj = ReflectUtils.getStaticProp(strVersion, "SDK");
                iLv = Integer.parseInt(obj.toString());
            } catch (Exception e1) {
                LogHelper.logFile(e1.toString());
            }
        }
        if (iLv > 0) {
            iSdk = iLv;
        }
        return iLv;
    }

}
