package com.yc.privacymonitor.bean;

import com.yc.privacymonitor.handler.MethodHookImpl;
import com.yc.privacymonitor.helper.PrivacyHelper;

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
public final class MethodBean {

    private final Class<?> targetClass;
    private final String targetMethod;
    private final Class<?>[] params;
    private MethodHookImpl methodHandler;


    public MethodBean(Class<?> targetClass, String targetMethod, Class<?>... params) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.params = params;
    }

    public void setMethodHandler(MethodHookImpl methodHandler) {
        this.methodHandler = methodHandler;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public Class<?>[] getParams() {
        return params;
    }

    public Object[] getParamsWithDefaultHandler(){
        Object[] temp = new Object[params.length+1];
        System.arraycopy(params, 0, temp, 0, params.length);
        if (methodHandler == null){
            temp[params.length] = PrivacyHelper.DEFAULT_METHOD_HANDLER;
        }else {
            temp[params.length] = methodHandler;
        }
        return temp;
    }
}

