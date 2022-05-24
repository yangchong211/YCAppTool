package com.yc.circleprogresslib;


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
public class CircleProgressUtils {

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

}
