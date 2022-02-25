package com.yc.animation.animator.fake;

import android.view.View;

import com.yc.animation.utils.LoggerUtil;

import java.lang.reflect.Method;

/**
 * @author 杨充
 * @date 2017/6/13 18:07
 */
public final class FakeAnimatorHelper {
    private static final String TAG = "FakeAnimatorHelper";

    private FakeAnimatorHelper(){}

    private static String getMethodName(String prefix, String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            // shouldn't get here
            return prefix;
        }
        char firstLetter = Character.toUpperCase(propertyName.charAt(0));
        String theRest = propertyName.substring(1);
        return prefix + firstLetter + theRest;
    }

    private static Method getPropertyFunction(Class<? extends View> targetClass, String methodName,  Class valueType) {
        Method method = null;
        Class args[] = {valueType};
        try {
            method = targetClass.getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            LoggerUtil.d(TAG, "getPropertyFunction e: " + e);
        }
        if (method == null) {
            LoggerUtil.d(TAG, "Method " + methodName + "() with type " + valueType +
                    " not found on target class " + targetClass);
        }
        return method;
    }


    static void invokeCustomSetter(View target, String propertyName, float value) {
        Class<? extends View> aClass = target.getClass();
        String methodName = getMethodName("set", propertyName);
        String logMethodSignature = "[" + aClass.getName() + "#" + methodName + "(" + float.class.getName() + ")]";
        LoggerUtil.d(TAG, "invokeCustomSetter " + logMethodSignature);

        Method setter = getPropertyFunction(aClass, methodName, float.class);
        if (setter != null) {
            try {
                setter.invoke(target, value);
            } catch (Exception e) {
                LoggerUtil.d(TAG, "invokeCustomSetter e: " + e);
            }
        } else {
            LoggerUtil.d(TAG, "invokeCustomSetter " + logMethodSignature + " failed because of null setter");
        }
    }
}
