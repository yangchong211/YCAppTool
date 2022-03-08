

package com.yc.ipc;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Proxy;

import com.yc.ipc.internal.Channel;
import com.yc.ipc.internal.HermesInvocationHandler;
import com.yc.ipc.internal.Reply;
import com.yc.ipc.sender.Sender;
import com.yc.ipc.sender.SenderDesignator;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.HermesGc;
import com.yc.ipc.util.TypeCenter;
import com.yc.ipc.util.TypeUtils;
import com.yc.ipc.wrapper.ObjectWrapper;


public class Hermes {

    private static final String TAG = "HERMES";

    private static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();

    private static final Channel CHANNEL = Channel.getInstance();

    private static final HermesGc HERMES_GC = HermesGc.getInstance();

    private static Context sContext = null;

    public static void register(Object object) {
        register(object.getClass());
    }

    private static void checkInit() {
        if (sContext == null) {
            throw new IllegalStateException("Hermes has not been initialized.");
        }
    }

    /**
     * There is no need to register class in local process!
     *
     * But if the returned type of a method is not exactly the same with the return type of the method, it should be registered.
     * @param clazz
     */
    public static void register(Class<?> clazz) {
        checkInit();
        TYPE_CENTER.register(clazz);
    }

    public static Context getContext() {
        return sContext;
    }

    public static void init(Context context) {
        if (sContext != null) {
            return;
        }
        sContext = context.getApplicationContext();
    }

    private static void checkBound(Class<? extends HermesService> service) {
        if (!CHANNEL.getBound(service)) {
            throw new IllegalStateException("Service Unavailable: You have not connected the service "
                    + "or the connection is not completed. You can set HermesListener to receive a callback "
                    + "when the connection is completed.");
        }
    }

    private static <T> T getProxy(Class<? extends HermesService> service, ObjectWrapper object) {
        Class<?> clazz = object.getObjectClass();
        T proxy =  (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz},
                    new HermesInvocationHandler(service, object));
        HERMES_GC.register(service, proxy, object.getTimeStamp());
        return proxy;
    }

    public static <T> T newInstance(Class<T> clazz, Object... parameters) {
        return newInstanceInService(HermesService.HermesService0.class, clazz, parameters);
    }

    public static <T> T newInstanceInService(Class<? extends HermesService> service, Class<T> clazz, Object... parameters) {
        TypeUtils.validateServiceInterface(clazz);
        checkBound(service);
        ObjectWrapper object = new ObjectWrapper(clazz, ObjectWrapper.TYPE_OBJECT_TO_NEW);
        Sender sender = SenderDesignator.getPostOffice(service, SenderDesignator.TYPE_NEW_INSTANCE, object);
        try {
            Reply reply = sender.send(null, parameters);
            if (reply != null && !reply.success()) {
                Log.e(TAG, "Error occurs during creating instance. Error code: " + reply.getErrorCode());
                Log.e(TAG, "Error message: " + reply.getMessage());
                return null;
            }
        } catch (HermesException e) {
            e.printStackTrace();
            return null;
        }
        object.setType(ObjectWrapper.TYPE_OBJECT);
        return getProxy(service, object);
    }

    public static <T> T getInstanceInService(Class<? extends HermesService> service, Class<T> clazz, Object... parameters) {
        return getInstanceWithMethodNameInService(service, clazz, "", parameters);
    }

    public static <T> T getInstance(Class<T> clazz, Object... parameters) {
        return getInstanceInService(HermesService.HermesService0.class, clazz, parameters);
    }

    public static <T> T getInstanceWithMethodName(Class<T> clazz, String methodName, Object... parameters) {
        return getInstanceWithMethodNameInService(HermesService.HermesService0.class, clazz, methodName, parameters);
    }

    public static <T> T getInstanceWithMethodNameInService(Class<? extends HermesService> service, Class<T> clazz, String methodName, Object... parameters) {
        TypeUtils.validateServiceInterface(clazz);
        checkBound(service);
        ObjectWrapper object = new ObjectWrapper(clazz, ObjectWrapper.TYPE_OBJECT_TO_GET);
        Sender sender = SenderDesignator.getPostOffice(service, SenderDesignator.TYPE_GET_INSTANCE, object);
        if (parameters == null) {
            parameters = new Object[0];
        }
        int length = parameters.length;
        Object[] tmp = new Object[length + 1];
        tmp[0] = methodName;
        for (int i = 0; i < length; ++i) {
            tmp[i + 1] = parameters[i];
        }
        try {
            Reply reply = sender.send(null, tmp);
            if (reply != null && !reply.success()) {
                Log.e(TAG, "Error occurs during getting instance. Error code: " + reply.getErrorCode());
                Log.e(TAG, "Error message: " + reply.getMessage());
                return null;
            }
        } catch (HermesException e) {
            e.printStackTrace();
            return null;
        }
        object.setType(ObjectWrapper.TYPE_OBJECT);
        return getProxy(service, object);
    }

    public static <T> T getUtilityClass(Class<T> clazz) {
        return getUtilityClassInService(HermesService.HermesService0.class, clazz);
    }

    public static <T> T getUtilityClassInService(Class<? extends HermesService> service, Class<T> clazz) {
        TypeUtils.validateServiceInterface(clazz);
        checkBound(service);
        ObjectWrapper object = new ObjectWrapper(clazz, ObjectWrapper.TYPE_CLASS_TO_GET);
        Sender sender = SenderDesignator.getPostOffice(service, SenderDesignator.TYPE_GET_UTILITY_CLASS, object);
        try {
            Reply reply = sender.send(null, null);
            if (reply != null && !reply.success()) {
                Log.e(TAG, "Error occurs during getting utility class. Error code: " + reply.getErrorCode());
                Log.e(TAG, "Error message: " + reply.getMessage());
                return null;
            }
        } catch (HermesException e) {
            e.printStackTrace();
            return null;
        }
        object.setType(ObjectWrapper.TYPE_CLASS);
        return getProxy(service, object);
    }

    public static void connect(Context context) {
        connectApp(context, null, HermesService.HermesService0.class);
    }

    public static void connect(Context context, Class<? extends HermesService> service) {
        // TODO callbacks should be handled as an exception.
        // It seems that callbacks may not be registered.
        connectApp(context, null, service);
    }

    public static void connectApp(Context context, String packageName) {
        connectApp(context, packageName, HermesService.HermesService0.class);
    }

    public static void connectApp(Context context, String packageName, Class<? extends HermesService> service) {
        init(context);
        CHANNEL.bind(context.getApplicationContext(), packageName, service);
    }

    public static void disconnect(Context context) {
        disconnect(context, HermesService.HermesService0.class);
    }

    public static void disconnect(Context context, Class<? extends HermesService> service) {
        CHANNEL.unbind(context.getApplicationContext(), service);
    }

    public static boolean isConnected() {
        return isConnected(HermesService.HermesService0.class);
    }

    public static boolean isConnected(Class<? extends HermesService> service) {
        return CHANNEL.isConnected(service);
    }

    public static void setHermesListener(HermesListener listener) {
        CHANNEL.setHermesListener(listener);
    }

}
