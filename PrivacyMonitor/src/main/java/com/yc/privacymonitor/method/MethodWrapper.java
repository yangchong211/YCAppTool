package com.yc.privacymonitor.method;

import com.yc.privacymonitor.PrivacyMonitor;
import com.yc.privacymonitor.handler.MethodHandler;

public class MethodWrapper {
    private Class<?> targetClass;
    private String targetMethod;
    private Class<?>[] params;
    private MethodHandler methodHandler;


    public MethodWrapper(Class<?> targetClass, String targetMethod, Class<?>... params) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.params = params;
    }

    public void setMethodHandler(MethodHandler methodHandler) {
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
            temp[params.length] = PrivacyMonitor.defaultMethodHandler;
        }else {
            temp[params.length] = methodHandler;
        }
        return temp;
    }
}

