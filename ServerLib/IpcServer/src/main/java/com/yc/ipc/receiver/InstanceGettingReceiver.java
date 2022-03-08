

package com.yc.ipc.receiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.yc.ipc.util.ErrorCodes;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.TypeUtils;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ObjectWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public class InstanceGettingReceiver extends Receiver {

    private Method mMethod;

    private Class<?> mObjectClass;

    public InstanceGettingReceiver(ObjectWrapper objectWrapper) throws HermesException {
        super(objectWrapper);
        Class<?> clazz = TYPE_CENTER.getClassType(objectWrapper);
        TypeUtils.validateAccessible(clazz);
        mObjectClass = clazz;
    }

    @Override
    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrappers)
            throws HermesException {
        int length = parameterWrappers.length;
        Class<?>[] parameterTypes = new Class<?>[length];
        for (int i = 0; i < length; ++i) {
            parameterTypes[i] = TYPE_CENTER.getClassType(parameterWrappers[i]);
        }
        String methodName = methodWrapper.getName();
        Method method = TypeUtils.getMethodForGettingInstance(mObjectClass, methodName, parameterTypes);
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new HermesException(ErrorCodes.METHOD_GET_INSTANCE_NOT_STATIC,
                    "Method " + method.getName() + " of class " + mObjectClass.getName() + " is not static. "
                            + "Only the static method can be invoked to get an instance.");
        }
        TypeUtils.validateAccessible(method);
        mMethod = method;
    }

    @Override
    protected Object invokeMethod() throws HermesException {
        Exception exception;
        try {
            Object object = mMethod.invoke(null, getParameters());
            OBJECT_CENTER.putObject(getObjectTimeStamp(), object);
            return null;
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            exception = e;
        }
        exception.printStackTrace();
        throw new HermesException(ErrorCodes.METHOD_INVOCATION_EXCEPTION,
                "Error occurs when invoking method " + mMethod + " to get an instance of "
                        + mObjectClass.getName(), exception);
    }
}
