package com.yc.expandlib;

import android.util.Log;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2019/04/24
 *     desc  : 日志
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCWidgetLib
 * </pre>
 */
public final class ExpandLogUtils {

    private static boolean isLog = false;

    public static boolean isIsLog() {
        return isLog;
    }

    public static void setIsLog(boolean isLog) {
        ExpandLogUtils.isLog = isLog;
    }

    public static void d(String log){
        if (isLog){
            Log.d("LogUtils",log);
        }
    }

}
