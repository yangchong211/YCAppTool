package com.yc.toolutils.reflect;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 反射工具类
 *     revise: 之前搜车封装库
 *
 * </pre>
 */
public final class ReflectUtils {

    static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final ConcurrentHashMap<Class<?>, Object> INSTANCE_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> INSTANCE_MAP2 = new ConcurrentHashMap<>();

    private ReflectUtils() {

    }

    static boolean isSameLength(Object[] array1, Object[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0)
                && (array2 != null || array1 == null || array1.length <= 0)
                && (array1 == null || array2 == null || array1.length == array2.length);
    }

    static Class<?>[] toClass(Object... array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_CLASS_ARRAY;
        } else {
            Class<?>[] classes = new Class[array.length];
            for(int i = 0; i < array.length; ++i) {
                classes[i] = array[i] == null ? null : array[i].getClass();
            }
            return classes;
        }
    }

    static Class<?>[] nullToEmpty(Class<?>[] array) {
        return array != null && array.length != 0 ? array : EMPTY_CLASS_ARRAY;
    }

    static Object[] nullToEmpty(Object[] array) {
        return array != null && array.length != 0 ? array : EMPTY_OBJECT_ARRAY;
    }

    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        } else {
            LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
            getAllInterfaces(cls, interfacesFound);
            return new ArrayList<>(interfacesFound);
        }
    }

    private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
        while(cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            int length = interfaces.length;
            for(int i = 0; i < length; ++i) {
                Class<?> aClass = interfaces[i];
                if (interfacesFound.add(aClass)) {
                    getAllInterfaces(aClass, interfacesFound);
                }
            }
            cls = cls.getSuperclass();
        }
    }



    /**
     * 获取反射对象
     * 这里使用泛型的好处众多，最主要的一点就是避免类型转换，防止出现ClassCastException，即类型转换异常
     *
     * @param cls class
     * @param <T> 泛型类型
     * @return 反射创建的对象
     */
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
                    // 返回使用该Class对象创建的实例
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
        if (clz == null) {
            return null;
        }
        return (T) INSTANCE_MAP.remove(clz);
    }

    /**
     * 获取反射对象
     * 这里使用泛型的好处众多，最主要的一点就是避免类型转换，防止出现ClassCastException，即类型转换异常
     *
     * @return 反射创建的对象
     */
    public static synchronized Object getInstance(String forName) {
        Object instance = INSTANCE_MAP2.get(forName);
        if (instance != null) {
            return instance;
        }
        instance = INSTANCE_MAP2.get(forName);
        if (instance == null) {
            try {
                //forName(包名.类名)
                Class<?> cls = Class.forName(forName);
                instance = cls.newInstance();
                INSTANCE_MAP.put(cls, instance);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static <T> T remove(String forName) {
        if (forName == null) {
            return null;
        }
        return (T) INSTANCE_MAP2.remove(forName);
    }

    public static synchronized void destroy() {
        for (Object instance : INSTANCE_MAP.values()) {
            if (instance instanceof Destroy) {
                ((Destroy) instance).destroy();
            }
        }
        INSTANCE_MAP.clear();


        for (Object instance : INSTANCE_MAP2.values()) {
            if (instance instanceof Destroy) {
                ((Destroy) instance).destroy();
            }
        }
        INSTANCE_MAP2.clear();
    }


    public interface Destroy {
        void destroy();
    }

}
