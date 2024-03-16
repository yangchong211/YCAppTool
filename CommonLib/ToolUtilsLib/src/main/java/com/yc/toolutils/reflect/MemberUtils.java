package com.yc.toolutils.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  :
 *     revise: 之前搜车封装库
 *
 * </pre>
 */
public final class MemberUtils {

    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES;
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP;
    private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP;

    MemberUtils() {

    }

    static {
        ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE,
                Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
        PRIMITIVE_WRAPPER_MAP = new HashMap<>();
        PRIMITIVE_WRAPPER_MAP.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_WRAPPER_MAP.put(Byte.TYPE, Byte.class);
        PRIMITIVE_WRAPPER_MAP.put(Character.TYPE, Character.class);
        PRIMITIVE_WRAPPER_MAP.put(Short.TYPE, Short.class);
        PRIMITIVE_WRAPPER_MAP.put(Integer.TYPE, Integer.class);
        PRIMITIVE_WRAPPER_MAP.put(Long.TYPE, Long.class);
        PRIMITIVE_WRAPPER_MAP.put(Double.TYPE, Double.class);
        PRIMITIVE_WRAPPER_MAP.put(Float.TYPE, Float.class);
        PRIMITIVE_WRAPPER_MAP.put(Void.TYPE, Void.TYPE);
        WRAPPER_PRIMITIVE_MAP = new HashMap<>();
        for (Class<?> primitiveClass : PRIMITIVE_WRAPPER_MAP.keySet()) {
            Class<?> wrapperClass = PRIMITIVE_WRAPPER_MAP.get(primitiveClass);
            if (!primitiveClass.equals(wrapperClass)) {
                WRAPPER_PRIMITIVE_MAP.put(wrapperClass, primitiveClass);
            }
        }
    }

    private static boolean isPackageAccess(int modifiers) {
        return (modifiers & 7) == 0;
    }

    static boolean isAccessible(Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }

    static boolean setAccessibleWorkaround(AccessibleObject o) {
        if (o != null && !o.isAccessible()) {
            Member m = (Member)o;
            if (!o.isAccessible() && Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                try {
                    o.setAccessible(true);
                    return true;
                } catch (SecurityException ignored) {
                }
            }
        }
        return false;
    }

    static boolean isAssignable(Class<?> cls, Class<?> toClass) {
        return isAssignable(cls, toClass, true);
    }

    static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing) {
        if (!ReflectUtils.isSameLength(classArray, toClassArray)) {
            return false;
        } else {
            if (classArray == null) {
                classArray = ReflectUtils.EMPTY_CLASS_ARRAY;
            }

            if (toClassArray == null) {
                toClassArray = ReflectUtils.EMPTY_CLASS_ARRAY;
            }

            for(int i = 0; i < classArray.length; ++i) {
                if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
                    return false;
                }
            }

            return true;
        }
    }

    static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
        if (toClass == null) {
            return false;
        } else if (cls == null) {
            return !toClass.isPrimitive();
        } else {
            if (autoboxing) {
                if (cls.isPrimitive() && !toClass.isPrimitive()) {
                    cls = primitiveToWrapper(cls);
                    if (cls == null) {
                        return false;
                    }
                }
                if (toClass.isPrimitive() && !cls.isPrimitive()) {
                    cls = wrapperToPrimitive(cls);
                    if (cls == null) {
                        return false;
                    }
                }
            }
            if (cls.equals(toClass)) {
                return true;
            } else if (cls.isPrimitive()) {
                if (!toClass.isPrimitive()) {
                    return false;
                } else if (Integer.TYPE.equals(cls)) {
                    return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
                } else if (Long.TYPE.equals(cls)) {
                    return Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
                } else if (Boolean.TYPE.equals(cls)) {
                    return false;
                } else if (Double.TYPE.equals(cls)) {
                    return false;
                } else if (Float.TYPE.equals(cls)) {
                    return Double.TYPE.equals(toClass);
                } else if (Character.TYPE.equals(cls)) {
                    return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
                } else if (Short.TYPE.equals(cls)) {
                    return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
                } else if (!Byte.TYPE.equals(cls)) {
                    return false;
                } else {
                    return Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
                }
            } else {
                return toClass.isAssignableFrom(cls);
            }
        }
    }

    static Class<?> primitiveToWrapper(Class<?> cls) {
        Class<?> convertedClass = cls;
        if (cls != null && cls.isPrimitive()) {
            convertedClass = PRIMITIVE_WRAPPER_MAP.get(cls);
        }

        return convertedClass;
    }

    static Class<?> wrapperToPrimitive(Class<?> cls) {
        return WRAPPER_PRIMITIVE_MAP.get(cls);
    }

    static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
        float leftCost = getTotalTransformationCost(actual, left);
        float rightCost = getTotalTransformationCost(actual, right);
        return leftCost < rightCost ? -1 : (rightCost < leftCost ? 1 : 0);
    }

    private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
        float totalCost = 0.0F;
        for(int i = 0; i < srcArgs.length; ++i) {
            Class<?> srcClass = srcArgs[i];
            Class<?> destClass = destArgs[i];
            totalCost += getObjectTransformationCost(srcClass, destClass);
        }
        return totalCost;
    }

    private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        } else {
            float cost;
            for(cost = 0.0F; srcClass != null && !destClass.equals(srcClass); srcClass = srcClass.getSuperclass()) {
                if (destClass.isInterface() && isAssignable(srcClass, destClass)) {
                    cost += 0.25F;
                    break;
                }
                ++cost;
            }
            if (srcClass == null) {
                ++cost;
            }
            return cost;
        }
    }

    private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
        float cost = 0.0F;
        Class<?> cls = srcClass;
        if (!srcClass.isPrimitive()) {
            cost += 0.1F;
            cls = wrapperToPrimitive(srcClass);
        }
        for(int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; ++i) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1F;
                if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }
        return cost;
    }

}

