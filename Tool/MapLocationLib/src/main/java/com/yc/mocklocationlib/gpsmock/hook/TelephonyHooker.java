package com.yc.mocklocationlib.gpsmock.hook;


import android.content.Context;
import android.os.IBinder;

import com.yc.mocklocationlib.gpsmock.GpsMockManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TelephonyHooker extends BaseServiceHooker {

    public TelephonyHooker() {
    }

    public String getServiceName() {
        return "phone";
    }

    public String getStubName() {
        return "com.android.internal.telephony.ITelephony$Stub";
    }

    public Map<String, MethodHandler> getMethodHandlers() {
        Map<String, MethodHandler> methodHandlers = new HashMap();
        methodHandlers.put("getAllCellInfo", new TelephonyHooker.GetAllCellInfoMethodHandler());
        methodHandlers.put("getCellLocation", new TelephonyHooker.GetCellLocationMethodHandler());
        methodHandlers.put("listen", new TelephonyHooker.ListenMethodHandler());
        return methodHandlers;
    }

    public void replaceBinder(Context context, IBinder proxy) {
    }

    private class ListenMethodHandler implements MethodHandler {
        private ListenMethodHandler() {
        }

        public Object onInvoke(Object originService, Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            return !GpsMockManager.getInstance().isMocking() ? method.invoke(originService, args) : null;
        }
    }

    public class GetCellLocationMethodHandler implements MethodHandler {
        public GetCellLocationMethodHandler() {
        }

        public Object onInvoke(Object originService, Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            return !GpsMockManager.getInstance().isMocking() ? method.invoke(originService, args) : null;
        }
    }

    public class GetAllCellInfoMethodHandler implements MethodHandler {
        public GetAllCellInfoMethodHandler() {
        }

        public Object onInvoke(Object originService, Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            return !GpsMockManager.getInstance().isMocking() ? method.invoke(originService, args) : new ArrayList();
        }
    }
}
