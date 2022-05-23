package com.yc.toolutils;

import android.content.Context;

public final class AppSizeUtils {


    /**
     * sp 转 px
     * @param spValue               sp 值
     * @return                      px 值
     */
    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
