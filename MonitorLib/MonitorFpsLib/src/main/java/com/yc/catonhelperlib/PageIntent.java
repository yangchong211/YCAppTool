package com.yc.catonhelperlib;

import android.os.Bundle;

public class PageIntent {
    public Class<? extends BaseFloatPage> targetClass;
    public Bundle bundle;
    public String tag;
    public int mode = 0;

    public PageIntent() {

    }

    public PageIntent(Class<? extends BaseFloatPage> targetClass) {
        this.targetClass = targetClass;
    }
}
