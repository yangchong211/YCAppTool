package com.yc.ycprogresslib;


import android.content.Context;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/2/10
 *     desc  :
 *     revise: 参考案例：夏安明博客http://blog.csdn.net/xiaanming/article/details/10298163
 *             案例地址：https://github.com/yangchong211
 * </pre>
 */
public class ProgressBarUtils {

    /**
     * 进度条类型。
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProgressType {
        /**
         * 顺数进度条，从0-100；
         */
        int COUNT = 1;
        /**
         * 倒数进度条，从100-0；
         */
        int COUNT_BACK = 2;
    }
    @IntDef({ProgressType.COUNT, ProgressType.COUNT_BACK})
    @interface CircleProgressType {}


    /**
     * 进度条text是否显示
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumberTextVisibility {
        /**
         * 显示
         */
        int Visible = 0;
        /**
         * 不显示
         */
        int Invisible = 1;
    }
    @IntDef({NumberTextVisibility.Visible, NumberTextVisibility.Invisible})
    @interface NumberVisibility {}


    /**
     * 进度条text是否显示
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface RingShowMode {
        /**
         * 空显示模式
         */
        int SHOW_MODE_NULL = 0;
        /**
         * 百分比+单位
         */
        int SHOW_MODE_PERCENT = 1;
    }
    @IntDef({RingShowMode.SHOW_MODE_NULL, RingShowMode.SHOW_MODE_PERCENT})
    @interface RingShowModeType {}


    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


}
