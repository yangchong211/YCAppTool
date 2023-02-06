package com.yc.videoview.tool;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class FloatScreenType {
    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;

    @IntDef({WIDTH, HEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface screenType {}
}
