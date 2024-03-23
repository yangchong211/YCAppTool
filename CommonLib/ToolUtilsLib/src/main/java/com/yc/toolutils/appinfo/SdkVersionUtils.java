package com.yc.toolutils.appinfo;

import android.os.Build;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : sdk版本判断
 *     revise :
 * </pre>
 */
public final class SdkVersionUtils {


    public static final int R = 30;

    /**
     * 判断是否是低于Android LOLLIPOP版本
     *
     * @return
     */
    public static boolean isMinM() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    /**
     * 判断是否是Android O版本
     *
     * @return
     */
    public static boolean isO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }


    /**
     * 判断是否是Android N版本
     *
     * @return
     */
    public static boolean isMaxN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


    /**
     * 判断是否是Android N版本
     *
     * @return
     */
    public static boolean isN() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N;
    }

    /**
     * 判断是否是Android Q版本
     *
     * @return
     */
    public static boolean isQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * 判断是否是Android R版本
     *
     * @return
     */
    public static boolean isR() {
        return Build.VERSION.SDK_INT >= R;
    }

}
