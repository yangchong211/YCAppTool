package com.yc.privacymonitor.handler;

import android.util.Log;

import com.yc.privacymonitor.PrivacyMonitor;

public class SettingsResolverHandler extends MethodHandler{

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Log.i(PrivacyMonitor.TAG, "**检测到风险Settings查询: "+param.args[1].toString());
        super.beforeHookedMethod(param);
    }
}

