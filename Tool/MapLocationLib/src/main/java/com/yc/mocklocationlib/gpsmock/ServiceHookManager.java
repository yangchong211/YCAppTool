package com.yc.mocklocationlib.gpsmock;


import android.content.Context;
import android.os.IBinder;


import com.yc.mocklocationlib.gpsmock.hook.BaseServiceHooker;
import com.yc.mocklocationlib.gpsmock.hook.BinderHookHandler;
import com.yc.mocklocationlib.gpsmock.hook.TelephonyHooker;
import com.yc.mocklocationlib.gpsmock.hook.WifiHooker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServiceHookManager {

    private static final String CLASS_SERVICE_MANAGER = "android.os.ServiceManager";
    private static final String METHOD_GET_SERVICE = "getService";
    private static final String FIELD_S_CACHE = "sCache";
    private boolean isHookSuccess;
    private final List<BaseServiceHooker> mHookers;

    public static ServiceHookManager getInstance() {
        return ServiceHookManager.Holder.INSTANCE;
    }

    private ServiceHookManager() {
        this.mHookers = new ArrayList();
        this.init();
    }

    private void init() {
        this.mHookers.add(new WifiHooker());
        this.mHookers.add(new TelephonyHooker());
    }

    /**
     * 1、 IBinder是一个接口，它代表了一种跨进程传输的能力；只要实现了这个接口，就能将这个对象进行跨进程传递；这是驱动底层支持的；
     * 在跨进程数据流经驱动的时候，驱动会识别IBinder类型的数据，从而自动完成不同进程Binder本地对象以及Binder代理对象的转换。
     * 2、 IBinder负责数据传输，那么client与server端的调用契约（这里不用接口避免混淆）呢？这里的IInterface代表的就是远程server对象具有什么能力。
     * 具体来说，就是aidl里面的接口。
     * 3、Java层的Binder类，代表的其实就是Binder本地对象。BinderProxy类是Binder类的一个内部类，它代表远程进程的Binder对象的本地代理；
     * 这两个类都继承自IBinder, 因而都具有跨进程传输的能力；实际上，在跨越进程的时候，Binder驱动会自动完成这两个对象的转换。
     * 4、在使用AIDL的时候，编译工具会给我们生成一个Stub的静态内部类；这个类继承了Binder, 说明它是一个Binder本地对象，它实现了IInterface接口，
     * 表明它具有远程Server承诺给Client的能力；Stub是一个抽象类，具体的IInterface的相关实现需要我们手动完成，这里使用了策略模式。
     * @param context
     */
    public void install(Context context) {
        try {
            Class serviceManager = Class.forName("android.os.ServiceManager");
            Method getService = serviceManager.getDeclaredMethod("getService", String.class);
            Iterator var4 = this.mHookers.iterator();

            while(var4.hasNext()) {
                BaseServiceHooker hooker = (BaseServiceHooker)var4.next();
                IBinder binder = (IBinder)getService.invoke((Object)null, hooker.getServiceName());
                if (binder == null) {
                    return;
                }

                ClassLoader classLoader = binder.getClass().getClassLoader();
                Class[] interfaces = new Class[]{IBinder.class};
                BinderHookHandler handler = new BinderHookHandler(binder, hooker);
                hooker.setBinder(binder);
                IBinder proxy = (IBinder)Proxy.newProxyInstance(classLoader, interfaces, handler);
                hooker.replaceBinder(context, proxy);
                Field sCache = serviceManager.getDeclaredField("sCache");
                sCache.setAccessible(true);
                Map<String, IBinder> cache = (Map)sCache.get((Object)null);
                cache.put(hooker.getServiceName(), proxy);
                sCache.setAccessible(false);
            }

            this.isHookSuccess = true;
        } catch (ClassNotFoundException var13) {
            var13.printStackTrace();
        } catch (NoSuchMethodException var14) {
            var14.printStackTrace();
        } catch (IllegalAccessException var15) {
            var15.printStackTrace();
        } catch (InvocationTargetException var16) {
            var16.printStackTrace();
        } catch (NoSuchFieldException var17) {
            var17.printStackTrace();
        }

    }

    public boolean isHookSuccess() {
        return this.isHookSuccess;
    }

    private static class Holder {

        private static final ServiceHookManager INSTANCE = new ServiceHookManager();

        private Holder() {
        }
    }
}

