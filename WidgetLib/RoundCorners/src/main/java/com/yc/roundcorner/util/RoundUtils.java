package com.yc.roundcorner.util;

import android.content.Context;

import androidx.annotation.NonNull;


public class RoundUtils {

    /**
     * 根据手机的分辨率将dp的单位转成px(像素)
     */
    public static float dip2px(@NonNull Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }

    /**
     * 根据手机的分辨率将px(像素)的单位转成dp
     */
    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale;
    }
}
