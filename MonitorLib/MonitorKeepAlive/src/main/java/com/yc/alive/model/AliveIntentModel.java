package com.yc.alive.model;

import androidx.annotation.StringRes;

import com.yc.alive.constant.AliveIntentType.TYPE;

/**
 * IntentModel
 */
public class AliveIntentModel {

    @TYPE
    public int type;

    public String action;

    public boolean needPackage;

    public String packageName;
    public String className;

    @StringRes
    public int routeTip;
}
