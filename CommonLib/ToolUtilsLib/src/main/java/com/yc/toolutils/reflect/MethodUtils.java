package com.yc.toolutils.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 获取class对象的方法
 *     revise: 之前搜车封装库
 *
 * </pre>
 */
public final class MethodUtils {

    /**
     * Method[] methods = class1.getDeclaredMethods();//获取class对象的所有声明方法
     * Method[] allMethods = class1.getMethods();//获取class对象的所有public方法 包括父类的方法
     * Method method = class1.getMethod("info", String.class);//返回次Class对象对应类的、带指定形参列表的public方法
     * Method declaredMethod = class1.getDeclaredMethod("info", String.class);//返回次Class对象对应类的、带指定形参列表的方法
     */
    private MethodUtils() {

    }

    /**
     * 获取class对象的所有public方法 包括父类的方法
     *
     * @param cls cls
     * @return
     */
    public static Method[] getMethods(Class<?> cls) {
        return cls.getMethods();
    }

    /**
     * 获取class对象的所有声明方法
     *
     * @param cls cls
     * @return
     */
    public static Method[] getDeclaredMethods(Class<?> cls) {
        return cls.getDeclaredMethods();
    }

    /**
     * 返回次Class对象对应类的、带指定形参列表的public方法
     *
     * @param cls        cls
     * @param methodName 方法名
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethod(Class<?> cls, String methodName) throws NoSuchMethodException {
        return getMatchedMethod(cls, methodName);
    }

    /**
     * 返回次Class对象对应类的、带指定形参列表的方法
     *
     * @param cls        cls
     * @param methodName 方法名
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getDeclaredMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        return getMatchedMethod(cls, methodName, parameterTypes);
    }

    /**
     * 反射调用类的方法
     *
     * @param object     对象
     * @param methodName 方法名
     * @param args       参数(可以是多个)
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ReflectUtils.nullToEmpty(args);
        Class<?>[] parameterTypes = ReflectUtils.toClass(args);
        return invokeMethod(object, methodName, args, parameterTypes);
    }

    /**
     * 反射调用类的方法
     *
     * @param object         对象
     * @param methodName     方法名
     * @param args           参数集合
     * @param parameterTypes 参数对应的类型集合
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        parameterTypes = ReflectUtils.nullToEmpty(parameterTypes);
        args = ReflectUtils.nullToEmpty(args);
        Method method = getMatchedMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
        } else {
            //如果是 private 或者 package 权限的，一定要赋予其访问权限
            method.setAccessible(true);
            // invoke() 方法中第一个参数 Object 实质上是 Method 所依附的 Class 对应的类的实例
            // 如果这个方法是一个静态方法，那么 ojb 为 null，后面的可变参数 Object 对应的自然就是参数。
            return method.invoke(object, args);
        }
    }

    /**
     * 反射调用类的静态方法
     *
     * @param clazz      clazz
     * @param methodName 方法名
     * @param args       参数(可以是多个)
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ReflectUtils.nullToEmpty(args);
        Class<?>[] parameterTypes = ReflectUtils.toClass(args);
        return invokeStaticMethod(clazz, methodName, args, parameterTypes);
    }

    /**
     * 反射调用类的静态方法
     *
     * @param clazz          clazz
     * @param methodName     方法名
     * @param args           参数集合
     * @param parameterTypes 参数对应的类型集合
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        parameterTypes = ReflectUtils.nullToEmpty(parameterTypes);
        args = ReflectUtils.nullToEmpty(args);
        Method method = getMatchedMethod(clazz, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + clazz.getName());
        } else {
            method.setAccessible(true);
            // invoke() 方法中第一个参数 Object 实质上是 Method 所依附的 Class 对应的类的实例
            // 如果这个方法是一个静态方法，那么 ojb 为 null，后面的可变参数 Object 对应的自然就是参数。
            return method.invoke(null, args);
        }
    }

    private static Method getMatchedMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method bestMatch;
        try {
            bestMatch = cls.getDeclaredMethod(methodName, parameterTypes);
            MemberUtils.setAccessibleWorkaround(bestMatch);
            return bestMatch;
        } catch (NoSuchMethodException var10) {
            for (bestMatch = null; cls != null; cls = cls.getSuperclass()) {
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods) {
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
        Class<?> parentClass = cls.getSuperclass();
        while (parentClass != null) {
            try {
                return parentClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException var5) {
                parentClass = parentClass.getSuperclass();
            }
        }
        return null;
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            int i = 0;
            while (i < interfaces.length) {
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
