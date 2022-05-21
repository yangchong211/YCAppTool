package com.yc.reflectionlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MethodUtils {
    public MethodUtils() {
    }

    public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        parameterTypes = ReflectUtils.nullToEmpty(parameterTypes);
        args = ReflectUtils.nullToEmpty(args);
        Method method = getMatchedMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
        } else {
            method.setAccessible(true);
            return method.invoke(object, args);
        }
    }

    public static Object invokeStaticMethod(Class clazz, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        parameterTypes = ReflectUtils.nullToEmpty(parameterTypes);
        args = ReflectUtils.nullToEmpty(args);
        Method method = getMatchedMethod(clazz, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + clazz.getName());
        } else {
            method.setAccessible(true);
            return method.invoke((Object)null, args);
        }
    }

    public static Object invokeStaticMethod(Class clazz, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ReflectUtils.nullToEmpty(args);
        Class<?>[] parameterTypes = ReflectUtils.toClass(args);
        return invokeStaticMethod(clazz, methodName, args, parameterTypes);
    }

    public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ReflectUtils.nullToEmpty(args);
        Class<?>[] parameterTypes = ReflectUtils.toClass(args);
        return invokeMethod(object, methodName, args, parameterTypes);
    }

    public static Method getMatchedMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method bestMatch;
        try {
            bestMatch = cls.getDeclaredMethod(methodName, parameterTypes);
            MemberUtils.setAccessibleWorkaround(bestMatch);
            return bestMatch;
        } catch (NoSuchMethodException var10) {
            for(bestMatch = null; cls != null; cls = cls.getSuperclass()) {
                Method[] methods = cls.getDeclaredMethods();
                Method[] var5 = methods;
                int var6 = methods.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    Method method = var5[var7];
                    if (method.getName().equals(methodName) && MemberUtils.isAssignable(parameterTypes, method.getParameterTypes(), true)) {
                        bestMatch = method;
                        Method accessibleMethod = getMethodFromElse(method);
                        if (accessibleMethod != null && MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), method.getParameterTypes(), parameterTypes) < 0) {
                            bestMatch = accessibleMethod;
                            break;
                        }
                    }
                }

                if (bestMatch != null) {
                    break;
                }
            }

            if (bestMatch != null) {
                MemberUtils.setAccessibleWorkaround(bestMatch);
            }

            return bestMatch;
        }
    }

    private static Method getMethodFromElse(Method method) {
        Class<?> cls = method.getDeclaringClass();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
        if (method == null) {
            method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
        }

        return method;
    }

    private static Method getAccessibleMethodFromSuperclass(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        Class parentClass = cls.getSuperclass();

        while(parentClass != null) {
            try {
                return parentClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException var5) {
                parentClass = parentClass.getSuperclass();
            }
        }

        return null;
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        while(cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            int i = 0;

            while(i < interfaces.length) {
                try {
                    return interfaces[i].getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException var6) {
                    Method method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                    if (method != null) {
                        return method;
                    }

                    ++i;
                }
            }

            cls = cls.getSuperclass();
        }

        return null;
    }
}
