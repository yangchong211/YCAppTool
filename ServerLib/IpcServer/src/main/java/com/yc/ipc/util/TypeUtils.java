

package com.yc.ipc.util;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import com.yc.ipc.annotation.ClassId;
import com.yc.ipc.annotation.GetInstance;
import com.yc.ipc.annotation.MethodId;
import com.yc.ipc.annotation.WithinProcess;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/7.
 */
public class TypeUtils {

    private static final HashSet<Class<?>> CONTEXT_CLASSES = new HashSet<Class<?>>() {
        {
            add(Context.class);
            add(Activity.class);
            add(AppCompatActivity.class);
            add(Application.class);
            add(FragmentActivity.class);
            add(IntentService.class);
            add(Service.class);
        }
    };

    public static String getClassId(Class<?> clazz) {
        ClassId classId = clazz.getAnnotation(ClassId.class);
        if (classId != null) {
            return classId.value();
        } else {
            return clazz.getName();
        }
    }

    public static String getMethodId(Method method) {
        MethodId methodId = method.getAnnotation(MethodId.class);
        if (methodId != null) {
            return methodId.value();
        } else {
            StringBuilder result = new StringBuilder(method.getName());
            result.append('(').append(getMethodParameters(method.getParameterTypes())).append(')');
            return result.toString();
        }
    }

    //boolean, byte, char, short, int, long, float, and double void
    private static String getClassName(Class<?> clazz) {
        if (clazz == Boolean.class) {
            return "boolean";
        } else if (clazz == Byte.class) {
            return "byte";
        } else if (clazz == Character.class) {
            return "char";
        } else if (clazz == Short.class) {
            return "short";
        } else if (clazz == Integer.class) {
            return "int";
        } else if (clazz == Long.class) {
            return "long";
        } else if (clazz == Float.class) {
            return "float";
        } else if (clazz == Double.class) {
            return "double";
        } else if (clazz == Void.class) {
            return "void";
        } else {
            return clazz.getName();
        }
    }

    public static String getMethodParameters(Class<?>[] classes) {
        StringBuilder result = new StringBuilder();
        int length = classes.length;
        if (length == 0) {
            return result.toString();
        }
        result.append(getClassName(classes[0]));
        for (int i = 1; i < length; ++i) {
            result.append(",").append(getClassName(classes[i]));
        }
        return result.toString();
    }

    public static boolean primitiveMatch(Class<?> class1, Class<?> class2) {
        if (!class1.isPrimitive() && !class2.isPrimitive()) {
            return false;
        } else if (class1 == class2) {
            return true;
        } else if (class1.isPrimitive()) {
            return primitiveMatch(class2, class1);
            //class2 is primitive
            //boolean, byte, char, short, int, long, float, and double void
        } else if (class1 == Boolean.class && class2 == boolean.class) {
            return true;
        } else if (class1 == Byte.class && class2 == byte.class) {
            return true;
        } else if (class1 == Character.class && class2 == char.class) {
            return true;
        } else if (class1 == Short.class && class2 == short.class) {
            return true;
        } else if (class1 == Integer.class && class2 == int.class) {
            return true;
        } else if (class1 == Long.class && class2 == long.class) {
            return true;
        } else if (class1 == Float.class && class2 == float.class) {
            return true;
        } else if (class1 == Double.class && class2 == double.class) {
            return true;
        } else if (class1 == Void.class && class2 == void.class) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean classAssignable(Class<?>[] classes1, Class<?>[] classes2) {
        if (classes1.length != classes2.length) {
            return false;
        }
        int length = classes2.length;
        for (int i = 0; i < length; ++i) {
            if (classes2[i] == null) {
                continue;
            }
            if (primitiveMatch(classes1[i], classes2[i])) {
                continue;
            }
            if (!classes1[i].isAssignableFrom(classes2[i])) {
                return false;
            }
        }
        return true;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Class<?> returnType)
                                throws HermesException {
        Method result = null;
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && classAssignable(method.getParameterTypes(), parameterTypes)) {
                if (result == null) {
                    result = method;
                } else {
                    throw new HermesException(ErrorCodes.TOO_MANY_MATCHING_METHODS,
                            "There are more than one method named "
                                    + methodName + " of the class "+ clazz.getName()
                                    + " matching the parameters!");
                }
            }
        }
        if (result == null) {
            return result;
        }
        if (result.getReturnType() != returnType) {
            throw new HermesException(ErrorCodes.METHOD_RETURN_TYPE_NOT_MATCHING,
                    "The method named " + methodName + " of the class " + clazz.getName()
                            + " matches the parameter types but not the return type. The return type is "
                            + result.getReturnType().getName() + " but the required type is "
                            + returnType.getName() + ". The method in the local interface must exactly "
                            + "match the method in the remote class.");
        }
        return result;
    }

    public static Method getMethodForGettingInstance(Class<?> clazz, String methodName, Class<?>[] parameterTypes)
            throws HermesException {
        Method[] methods = clazz.getMethods();
        Method result = null;
        for (Method method : methods) {
            String tmpName = method.getName();
            if (methodName.equals("") && (tmpName.equals("getInstance") || method.isAnnotationPresent(GetInstance.class))
                    || !methodName.equals("") && tmpName.equals(methodName)) {
                if (classAssignable(method.getParameterTypes(), parameterTypes)) {
                    if (result == null) {
                        result = method;
                    } else {
                        throw new HermesException(ErrorCodes.TOO_MANY_MATCHING_METHODS_FOR_GETTING_INSTANCE,
                                "When getting instance, there are more than one method named "
                                        + methodName + " of the class "+ clazz.getName()
                                        + " matching the parameters!");
                    }
                }
            }
        }
        if (result != null) {
            if (result.getReturnType() != clazz) {
                throw new HermesException(ErrorCodes.GETTING_INSTANCE_RETURN_TYPE_ERROR,
                        "When getting instance, the method named " + methodName + " of the class " + clazz.getName()
                                + " matches the parameter types but not the return type. The return type is "
                                + result.getReturnType().getName() + " but the required type is "
                                + clazz.getName() + ".");
            }
            return result;
        }
        throw new HermesException(ErrorCodes.GETTING_INSTANCE_METHOD_NOT_FOUND,
                "When getting instance, the method named " + methodName + " of the class "
                        + clazz.getName() + " is not found. The class must have a method for getting instance.");
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] parameterTypes) throws HermesException {
        Constructor<?> result = null;
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (classAssignable(constructor.getParameterTypes(), parameterTypes)) {
                if (result != null) {
                    throw new HermesException(ErrorCodes.TOO_MANY_MATCHING_CONSTRUCTORS_FOR_CREATING_INSTANCE,
                            "The class " + clazz.getName() + " has too many constructors whose "
                                    + " parameter types match the required types.");
                } else {
                    result = constructor;
                }
            }
        }
        if (result == null) {
            throw new HermesException(ErrorCodes.CONSTRUCTOR_NOT_FOUND,
                    "The class " + clazz.getName() + " do not have a constructor whose "
                            + " parameter types match the required types.");
        }
        return result;
    }

    public static ParameterWrapper[] objectToWrapper(Object[] objects) throws HermesException {
        if (objects == null) {
            objects = new Object[0];
        }
        int length = objects.length;
        ParameterWrapper[] parameterWrappers = new ParameterWrapper[length];
        for (int i = 0; i < length; ++i) {
            try {
                parameterWrappers[i] = new ParameterWrapper(objects[i]);
            } catch (HermesException e) {
                e.printStackTrace();
                throw new HermesException(e.getErrorCode(),
                        "Error happens at parameter encoding, and parameter index is "
                                + i + ". See the stack trace for more information.",
                        e);
            }
        }
        return parameterWrappers;
    }

    public static void validateClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class object is null.");
        }
        if (clazz.isPrimitive() || clazz.isInterface()) {
            return;
        }
        if (clazz.isAnnotationPresent(WithinProcess.class)) {
            throw new IllegalArgumentException(
                    "Error occurs when registering class " + clazz.getName()
                            + ". Class with a WithinProcess annotation presented on it cannot be accessed"
                            + " from outside the process.");
        }

        if (clazz.isAnonymousClass()) {
            throw new IllegalArgumentException(
                    "Error occurs when registering class " + clazz.getName()
                            +". Anonymous class cannot be accessed from outside the process.");
        }
        if (clazz.isLocalClass()) {
            throw new IllegalArgumentException(
                    "Error occurs when registering class " + clazz.getName()
                            + ". Local class cannot be accessed from outside the process.");
        }
        if (Context.class.isAssignableFrom(clazz)) {
            return;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException(
                    "Error occurs when registering class " + clazz.getName()
                            + ". Abstract class cannot be accessed from outside the process.");
        }
    }

    public static void validateServiceInterface(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class object is null.");
        }
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Only interfaces can be passed as the parameters.");
        }
    }

    public static boolean arrayContainsAnnotation(Annotation[] annotations, Class<? extends Annotation> annotationClass) {
        if (annotations == null || annotationClass == null) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> getContextClass(Class<?> clazz) {
        for (Class<?> tmp = clazz; tmp != Object.class; tmp = tmp.getSuperclass()) {
            if (CONTEXT_CLASSES.contains(tmp)) {
                return tmp;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void validateAccessible(Class<?> clazz) throws HermesException {
        if (clazz.isAnnotationPresent(WithinProcess.class)) {
            throw new HermesException(ErrorCodes.CLASS_WITH_PROCESS,
                    "Class " + clazz.getName() + " has a WithProcess annotation on it, "
                            + "so it cannot be accessed from outside the process.");
        }
    }

    public static void validateAccessible(Method method) throws HermesException {
        if (method.isAnnotationPresent(WithinProcess.class)) {
            throw new HermesException(ErrorCodes.METHOD_WITH_PROCESS,
                    "Method " + method.getName() + " of class " + method.getDeclaringClass().getName()
                            + " has a WithProcess annotation on it, so it cannot be accessed from "
                            + "outside the process.");
        }
    }

    public static void validateAccessible(Constructor<?> constructor) throws HermesException {
        if (constructor.isAnnotationPresent(WithinProcess.class)) {
            throw new HermesException(ErrorCodes.METHOD_WITH_PROCESS,
                    "Constructor " + constructor.getName() + " of class " + constructor.getDeclaringClass().getName()
                            + " has a WithProcess annotation on it, so it cannot be accessed from "
                            + "outside the process.");
        }
    }

    public static void methodParameterTypeMatch(Method method, MethodWrapper methodWrapper) throws HermesException {
        Class<?>[] requiredParameterTypes = TypeCenter.getInstance().getClassTypes(methodWrapper.getParameterTypes());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (requiredParameterTypes.length != parameterTypes.length) {
            throw new HermesException(ErrorCodes.METHOD_PARAMETER_NOT_MATCHING,
                    "The number of method parameters do not match. "
                            + "Method " + method + " has " + parameterTypes.length + " parameters. "
                            + "The required method has " + requiredParameterTypes.length + " parameters.");
        }
        int length = requiredParameterTypes.length;
        for (int i = 0; i < length; ++i) {
            if (requiredParameterTypes[i].isPrimitive() || parameterTypes[i].isPrimitive()) {
                if (!primitiveMatch(requiredParameterTypes[i], parameterTypes[i])) {
                    throw new HermesException(ErrorCodes.METHOD_PARAMETER_NOT_MATCHING,
                            "The parameter type of method " + method + " do not match at index " + i + ".");
                }
            } else if (requiredParameterTypes[i] != parameterTypes[i]) {
                if (!primitiveMatch(requiredParameterTypes[i], parameterTypes[i])) {
                    throw new HermesException(ErrorCodes.METHOD_PARAMETER_NOT_MATCHING,
                            "The parameter type of method " + method + " do not match at index " + i + ".");
                }
            }
        }
    }

    public static void methodReturnTypeMatch(Method method, MethodWrapper methodWrapper) throws HermesException {
        Class<?> returnType = method.getReturnType();
        Class<?> requiredReturnType = TypeCenter.getInstance().getClassType(methodWrapper.getReturnType());
        if (returnType.isPrimitive() || requiredReturnType.isPrimitive()) {
            if (!TypeUtils.primitiveMatch(returnType, requiredReturnType)) {
                throw new HermesException(ErrorCodes.METHOD_RETURN_TYPE_NOT_MATCHING,
                        "The return type of methods do not match. "
                                + "Method " + method + " return type: " + returnType.getName()
                                + ". The required is " + requiredReturnType.getName());
            }
        } else if (requiredReturnType != returnType) {
            if (!TypeUtils.primitiveMatch(returnType, requiredReturnType)) {
                throw new HermesException(ErrorCodes.METHOD_RETURN_TYPE_NOT_MATCHING,
                        "The return type of methods do not match. "
                                + "Method " + method + " return type: " + returnType.getName()
                                + ". The required is " + requiredReturnType.getName());
            }
        }
    }

    public static void methodMatch(Method method, MethodWrapper methodWrapper) throws HermesException {
        methodParameterTypeMatch(method, methodWrapper);
        methodReturnTypeMatch(method, methodWrapper);
    }

}
