package com.yc.mocklocationlib.gpsmock.hook;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.yc.mocklocationlib.gpsmock.GpsMockManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WifiHooker extends BaseServiceHooker {

    public WifiHooker() {
    }

    public String getServiceName() {
        return "wifi";
    }

    public String getStubName() {
        return "android.net.wifi.IWifiManager$Stub";
    }

    public Map<String, MethodHandler> getMethodHandlers() {
        Map<String, MethodHandler> methodHandlers = new HashMap();
        methodHandlers.put("getScanResults", new WifiHooker.GetScanResultsMethodHandler());
        methodHandlers.put("getConnectionInfo", new WifiHooker.GetConnectionInfoMethodHandler());
        return methodHandlers;
    }

    public void replaceBinder(Context context, IBinder proxy) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService("wifi");
        if (wifiManager != null) {
            Class<?> wifiManagerClass = wifiManager.getClass();
            Field mServiceField = wifiManagerClass.getDeclaredField("mService");
            mServiceField.setAccessible(true);
            Class stub = Class.forName(this.getStubName());
            Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
            mServiceField.set(wifiManager, asInterface.invoke((Object)null, proxy));
            mServiceField.setAccessible(false);
        }
    }

    public class GetConnectionInfoMethodHandler implements MethodHandler {
        public GetConnectionInfoMethodHandler() {
        }

        public Object onInvoke(Object originService, Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            return !GpsMockManager.getInstance().isMocking() ? method.invoke(originService, args) : null;
        }
    }

    public class GetScanResultsMethodHandler implements MethodHandler {
        public GetScanResultsMethodHandler() {
        }

        public Object onInvoke(Object originService, Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            return !GpsMockManager.getInstance().isMocking() ? method.invoke(originService, args) : new ArrayList();
        }
    }
}

