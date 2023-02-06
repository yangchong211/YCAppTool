package com.yc.videoview.tool;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class FloatMoveType {

    public static final int FIXED = 0;
    public static final int FREE = 1;
    public static final int ACTIVE = 2;
    public static final int SLIDE = 3;
    public static final int BACK = 4;

    @IntDef({FIXED, FREE, ACTIVE, SLIDE, BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MOVE_TYPE {

    }

}
