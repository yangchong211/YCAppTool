package com.ycbjie.love.util;

import android.os.SystemClock;
import android.util.Log;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 快速点击判断
 *     revise:
 * </pre>
 */
public class ClickUtils {

    private static final String TAG = ClickUtils.class.getSimpleName();
    private static long lastClickTime = 0L;
    private static final boolean isDebug = false;
    private static final String BLANK_LOG = "\t";
    /**
     * 用于处理频繁点击问题, 如果两次点击小于500毫秒则不予以响应
     * @return true:是连续的快速点击
     */
    public static boolean isFastDoubleClick() {
        //从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
        long nowTime = SystemClock.elapsedRealtime();

        if (isDebug){
            Log.d(TAG,"nowTime:" + nowTime);
            Log.d(TAG,"lastClickTime:" + lastClickTime);
            Log.d(TAG,"时间间隔:"+(nowTime - lastClickTime));
        }
        if ((nowTime - lastClickTime) < 5000) {

            if (isDebug){
                Log.d(TAG,"快速点击");
                Log.d(TAG, BLANK_LOG);
            }
            return true;
        } else {
            lastClickTime = nowTime;

            if (isDebug){
                Log.d(TAG,"lastClickTime:" + lastClickTime);
                Log.d(TAG,"不是快速点击");
                Log.d(TAG,BLANK_LOG);
            }
            return false;
        }
    }
}
