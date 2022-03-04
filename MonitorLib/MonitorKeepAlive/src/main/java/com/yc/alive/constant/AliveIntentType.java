package com.yc.alive.constant;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AliveIntentType {

    // 系统 Action
    public static final int ACTION = 1;

    // className
    public static final int CLASS_NAME = 2;

    @IntDef({ ACTION, CLASS_NAME })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }
}
