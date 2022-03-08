

package com.yc.ipc.receiver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.yc.ipc.util.ErrorCodes;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.TypeUtils;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ObjectWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public class InstanceCreatingReceiver extends Receiver {

    private Class<?> mObjectClass;

    private Constructor<?> mConstructor;

    public InstanceCreatingReceiver(ObjectWrapper object) throws HermesException {
        super(object);
        Class<?> clazz = TYPE_CENTER.getClassType(object);
        TypeUtils.validateAccessible(clazz);
        mObjectClass = clazz;
    }

    @Override
    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrappers)
            throws HermesException {
        Constructor<?> constructor = TypeUtils.getConstructor(mObjectClass, TYPE_CENTER.getClassTypes(parameterWrappers));
        TypeUtils.validateAccessible(constructor);
        mConstructor = constructor;
    }

    @Override
    protected Object invokeMethod() throws HermesException {
        Exception exception;
        try {
            Object object;
            Object[] parameters = getParameters();
            if (parameters == null) {
                object = mConstructor.newInstance();
            } else {
                object = mConstructor.newInstance(parameters);
            }
            OBJECT_CENTER.putObject(getObjectTimeStamp(), object);
            return null;
        } catch (InstantiationException e) {
            exception = e;
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            exception = e;
        }
        exception.printStackTrace();
        throw new HermesException(ErrorCodes.METHOD_INVOCATION_EXCEPTION,
                "Error occurs when invoking constructor to create an instance of "
                        + mObjectClass.getName(), exception);
    }
}
