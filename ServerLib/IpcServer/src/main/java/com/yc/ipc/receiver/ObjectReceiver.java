

package com.yc.ipc.receiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.yc.ipc.util.ErrorCodes;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.TypeUtils;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ObjectWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public class ObjectReceiver extends Receiver {

    private Method mMethod;

    private Object mObject;

    public ObjectReceiver(ObjectWrapper objectWrapper) {
        super(objectWrapper);
        mObject = OBJECT_CENTER.getObject(getObjectTimeStamp());
    }

    @Override
    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrappers)
            throws HermesException {
        Method method = TYPE_CENTER.getMethod(mObject.getClass(), methodWrapper);
        TypeUtils.validateAccessible(method);
        mMethod = method;
    }

    @Override
    protected Object invokeMethod() throws HermesException {
        Exception exception;
        try {
            return mMethod.invoke(mObject, getParameters());
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            exception = e;
        }
        exception.printStackTrace();
        throw new HermesException(ErrorCodes.METHOD_INVOCATION_EXCEPTION,
                "Error occurs when invoking method " + mMethod + " on " + mObject, exception);
    }
}
