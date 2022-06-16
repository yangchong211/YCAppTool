package com.yc.privacymonitor.handler;

import android.util.Log;

import com.yc.privacymonitor.helper.PrivacyHelper;

import java.util.Arrays;


/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAndroidTool
 *     email : yangchong211@163.com
 *     time  : 2021/05/09
 *     desc  : 方法包装类
 *     revise:
 * </pre>
 */
public class SettingsResolverImpl extends MethodHookImpl {

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        try {
            Log.i(PrivacyHelper.TAG, "检测到风险Settings调用: "+param.args[1].toString());
        } catch (Exception e){
            Log.i(PrivacyHelper.TAG, "检测到风险Settings调用: "+ Arrays.toString(param.args));
        }
        super.beforeHookedMethod(param);
    }
}

