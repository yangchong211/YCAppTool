package com.yc.reflectionlib;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

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


    public static <T> T getInstance(Class<T> cls) {
        try {
            // 返回使用该Class对象创建的实例
            return cls.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
