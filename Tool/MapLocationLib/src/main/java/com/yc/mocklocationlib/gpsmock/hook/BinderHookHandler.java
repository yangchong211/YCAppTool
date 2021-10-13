package com.yc.mocklocationlib.gpsmock.hook;


import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BinderHookHandler implements InvocationHandler {
    private static final String TAG = "BinderHookHandler";
    private IBinder mOriginService;
    private BaseServiceHooker mHooker;

    public BinderHookHandler(IBinder binder, BaseServiceHooker hooker) {
        this.mOriginService = binder;
        this.mHooker = hooker;
    }

    @SuppressLint({"PrivateApi"})
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        String var4 = method.getName();
        byte var5 = -1;
        switch(var4.hashCode()) {
            case -554320650:
                if (var4.equals("queryLocalInterface")) {
                    var5 = 0;
                }
            default:
                switch(var5) {
                    case 0:
                        Class iManager;
                        try {
                            iManager = Class.forName(String.valueOf(args[0]));
                        } catch (ClassNotFoundException var9) {
                            var9.printStackTrace();
                            return method.invoke(this.mOriginService, args);
                        }

                        ClassLoader classLoader = this.mOriginService.getClass().getClassLoader();
                        Class[] interfaces = new Class[]{IInterface.class, IBinder.class, iManager};
                        return Proxy.newProxyInstance(classLoader, interfaces, this.mHooker);
                    default:
                        return method.invoke(this.mOriginService, args);
                }
        }
    }
}

