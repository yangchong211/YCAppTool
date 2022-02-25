package com.yc.timerlib.timer;

import android.util.Log;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2020/5/26
 *     desc  :  工具类
 *     revise:
 * </pre>
 */
public final class CountTimeTools {

    private static final String TAG = "CountDownTimer";
    private static boolean isLog = true;

    public static void i(String message) {
        if(isLog){
            Log.i(TAG, message);
        }
    }


    /**
     * 将毫秒换成00:00:00
     * @param time                          毫秒
     * @return                              时间字符串
     */
    public static String getCountTimeByLong(long time) {
        //秒
        long totalTime = time / 1000;
        //时，分，秒
        long hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();
        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(minute).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();
    }


    /**
     * 将毫秒换成 00:00 或者 00，这个根据具体时间来计算
     * @param time                          毫秒
     * @return                              时间字符串
     */
    public static String getCountTime(long time) {
        //秒
        long totalTime = time / 1000;
        //时，分，秒
        long hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();
        if (hour>0){
            if (hour < 10) {
                sb.append("0").append(hour).append(":");
            } else {
                sb.append(hour).append(":");
            }
        }
        if (minute>0){
            if (minute < 10) {
                sb.append("0").append(minute).append(":");
            } else {
                sb.append(minute).append(":");
            }
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();
    }

}
