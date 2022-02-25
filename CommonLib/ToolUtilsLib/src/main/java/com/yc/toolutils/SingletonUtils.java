package com.yc.toolutils;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

public final class SingletonUtils {

    private static final ConcurrentHashMap<Class<?>, Object>
            INSTANCE_MAP = new ConcurrentHashMap<>();

    private SingletonUtils() {

    }

    public static <T> T getInstance(Class<T> cls) {
        Object instance = INSTANCE_MAP.get(cls);
        if (instance != null) {
            return (T) instance;
        }
        synchronized (cls) {
            instance = INSTANCE_MAP.get(cls);
            if (instance == null) {
                try {
                    Constructor e = cls.getDeclaredConstructor(new Class[0]);
                    e.setAccessible(true);
                    instance = e.newInstance(new Object[0]);
                    INSTANCE_MAP.put(cls, instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (T) instance;
        }
    }

    public static <T> T remove(Class<T> clz) {
        if (clz == null){
            return null;
        }
        return (T) INSTANCE_MAP.remove(clz);
    }

    public static synchronized void destroy() {
        for (Object instance : INSTANCE_MAP.values()) {
            if (instance instanceof Destroy) {
                ((Destroy) instance).destroy();
            }
        }
        INSTANCE_MAP.clear();
    }


    public interface Destroy {
        void destroy();
    }
}
