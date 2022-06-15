package com.yc.privacymonitor.handler;

import android.util.Log;

import com.yc.privacymonitor.helper.PrivacyHelper;

public class SettingsResolverHandler extends MethodHookImpl {

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Log.i(PrivacyHelper.TAG, "**检测到风险Settings查询: "+param.args[1].toString());
        super.beforeHookedMethod(param);
    }
}

