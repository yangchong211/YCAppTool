package com.yc.toolutils.reflection;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public final class ReflectUtils {
    static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    public ReflectUtils() {
    }

    static boolean isSameLength(Object[] array1, Object[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
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
            LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet();
            getAllInterfaces(cls, interfacesFound);
            return new ArrayList(interfacesFound);
        }
    }

    private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
        while(cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            Class[] var3 = interfaces;
            int var4 = interfaces.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Class<?> i = var3[var5];
                if (interfacesFound.add(i)) {
                    getAllInterfaces(i, interfacesFound);
                }
            }

            cls = cls.getSuperclass();
        }

    }
}
