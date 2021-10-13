package com.yc.mocklocationlib.gpsmock.hook;


import android.content.Context;
import android.os.IBinder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class BaseServiceHooker implements InvocationHandler {

    protected static final String METHOD_ASINTERFACE = "asInterface";
    private Object mOriginService;

    public BaseServiceHooker() {
    }

    public abstract String getServiceName();

    public abstract String getStubName();

    public abstract Map<String, BaseServiceHooker.MethodHandler> getMethodHandlers();

    public Object invoke(Object proxy, Method method, Object[] args)
            throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        return this.getMethodHandlers().containsKey(method.getName())
                ? ((BaseServiceHooker.MethodHandler)this.getMethodHandlers()
                    .get(method.getName())).onInvoke(this.mOriginService, proxy, method, args)
                : method.invoke(this.mOriginService, args);
    }

    public void setBinder(IBinder binder) {
        try {
            Class stub = Class.forName(this.getStubName());
            Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
            this.mOriginService = asInterface.invoke((Object)null, binder);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public abstract void replaceBinder(Context var1, IBinder var2)
            throws NoSuchFieldException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException;

    public interface MethodHandler {
        Object onInvoke(Object var1, Object var2, Method var3, Object[] var4)
                throws InvocationTargetException, IllegalAccessException, NoSuchFieldException;
    }
}

