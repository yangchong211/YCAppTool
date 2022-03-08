

package com.yc.ipc.util;

import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import com.yc.ipc.annotation.ClassId;
import com.yc.ipc.annotation.MethodId;
import com.yc.ipc.wrapper.BaseWrapper;
import com.yc.ipc.wrapper.MethodWrapper;

/**
 * Created by yangchong on 16/4/7.
 */
public class TypeCenter {
    
    private static volatile TypeCenter sInstance = null;

    private final ConcurrentHashMap<String, Class<?>> mAnnotatedClasses;

    private final ConcurrentHashMap<String, Class<?>> mRawClasses;

    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mAnnotatedMethods;

    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mRawMethods;

    private TypeCenter() {
        mAnnotatedClasses = new ConcurrentHashMap<String, Class<?>>();
        mRawClasses = new ConcurrentHashMap<String, Class<?>>();
        mAnnotatedMethods = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>>();
        mRawMethods = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>>();
    }

    public static TypeCenter getInstance() {
        if (sInstance == null) {
            synchronized (TypeCenter.class) {
                if (sInstance == null) {
                    sInstance = new TypeCenter();
                }
            }
        }
        return sInstance;
    }

    private void registerClass(Class<?> clazz) {
        ClassId classId = clazz.getAnnotation(ClassId.class);
        if (classId == null) {
            String className = clazz.getName();
            mRawClasses.putIfAbsent(className, clazz);
        } else {
            String className = classId.value();
            mAnnotatedClasses.putIfAbsent(className, clazz);
        }
    }

    private void registerMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            MethodId methodId = method.getAnnotation(MethodId.class);
            if (methodId == null) {
                mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
                ConcurrentHashMap<String, Method> map = mRawMethods.get(clazz);
                String key = TypeUtils.getMethodId(method);
                map.putIfAbsent(key, method);
            } else {
                mAnnotatedMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
                ConcurrentHashMap<String, Method> map = mAnnotatedMethods.get(clazz);
                String key = TypeUtils.getMethodId(method);
                map.putIfAbsent(key, method);
            }
        }
    }

    public void register(Class<?> clazz) {
        TypeUtils.validateClass(clazz);
        registerClass(clazz);
        registerMethod(clazz);
    }

    public Class<?> getClassType(BaseWrapper wrapper) throws HermesException {
        String name = wrapper.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        if (wrapper.isName()) {
            Class<?> clazz = mRawClasses.get(name);
            if (clazz != null) {
                return clazz;
            }
            //boolean, byte, char, short, int, long, float, and double void
            if (name.equals("boolean")) {
                clazz = boolean.class;
            } else if (name.equals("byte")) {
                clazz = byte.class;
            } else if (name.equals("char")) {
                clazz = char.class;
            } else if (name.equals("short")) {
                clazz = short.class;
            } else if (name.equals("int")) {
                clazz = int.class;
            } else if (name.equals("long")) {
                clazz = long.class;
            } else if (name.equals("float")) {
                clazz = float.class;
            } else if (name.equals("double")) {
                clazz = double.class;
            } else if (name.equals("void")) {
                clazz = void.class;
            } else {
                try {
                    clazz = Class.forName(name);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new HermesException(ErrorCodes.CLASS_NOT_FOUND,
                            "Cannot find class " + name + ". Classes without ClassId annotation on it "
                                    + "should be located at the same package and have the same name, "
                                    + "EVEN IF the source code has been obfuscated by Proguard.");
                }

            }
            mRawClasses.putIfAbsent(name, clazz);
            return clazz;
        } else {
            Class<?> clazz = mAnnotatedClasses.get(name);
            if (clazz == null) {
                throw new HermesException(ErrorCodes.CLASS_NOT_FOUND,
                        "Cannot find class with ClassId annotation on it. ClassId = " + name
                                + ". Please add the same annotation on the corresponding class in the remote process"
                                + " and register it. Have you forgotten to register the class?");
            }
            return clazz;
        }
    }

    public Class<?>[] getClassTypes(BaseWrapper[] wrappers) throws HermesException {
        Class<?>[] classes = new Class<?>[wrappers.length];
        for (int i = 0; i < wrappers.length; ++i) {
            classes[i] = getClassType(wrappers[i]);
        }
        return classes;
    }

    public Method getMethod(Class<?> clazz, MethodWrapper methodWrapper) throws HermesException {
        String name = methodWrapper.getName();
        if (methodWrapper.isName()) {
            mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> methods = mRawMethods.get(clazz);
            Method method = methods.get(name);
            if (method != null) {
                TypeUtils.methodReturnTypeMatch(method, methodWrapper);
                return method;
            }
            int pos = name.indexOf('(');
            method = TypeUtils.getMethod(clazz, name.substring(0, pos), getClassTypes(methodWrapper.getParameterTypes()), getClassType(methodWrapper.getReturnType()));
            if (method == null) {
                throw new HermesException(ErrorCodes.METHOD_NOT_FOUND,
                        "Method not found: " + name + " in class " + clazz.getName());
            }
            methods.put(name, method);
            return method;
        } else {
            ConcurrentHashMap<String, Method> methods = mAnnotatedMethods.get(clazz);
            Method method = methods.get(name);
            if (method != null) {
                TypeUtils.methodMatch(method, methodWrapper);
                return method;
            }
            throw new HermesException(ErrorCodes.METHOD_NOT_FOUND,
                    "Method not found in class " + clazz.getName() + ". Method id = " + name + ". "
                            + "Please add the same annotation on the corresponding method in the remote process.");
        }
    }
}
