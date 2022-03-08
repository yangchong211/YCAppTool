

package com.yc.ipc.receiver;

import android.content.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.yc.ipc.Hermes;
import com.yc.ipc.internal.IHermesServiceCallback;
import com.yc.ipc.internal.HermesCallbackInvocationHandler;
import com.yc.ipc.internal.Reply;
import com.yc.ipc.util.CodeUtils;
import com.yc.ipc.util.HermesCallbackGc;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.ObjectCenter;
import com.yc.ipc.util.TypeCenter;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ObjectWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public abstract class Receiver {

    protected static final ObjectCenter OBJECT_CENTER = ObjectCenter.getInstance();

    protected static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();

    protected static final HermesCallbackGc HERMES_CALLBACK_GC = HermesCallbackGc.getInstance();

    private long mObjectTimeStamp;

    private Object[] mParameters;

    private IHermesServiceCallback mCallback;

    public Receiver(ObjectWrapper objectWrapper) {
        mObjectTimeStamp = objectWrapper.getTimeStamp();
    }

    protected long getObjectTimeStamp() {
        return mObjectTimeStamp;
    }

    protected Object[] getParameters() {
        return mParameters;
    }

    public void setHermesServiceCallback(IHermesServiceCallback callback) {
        mCallback = callback;
    }

    private Object getProxy(Class<?> clazz, int index, long methodInvocationTimeStamp) {
        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new HermesCallbackInvocationHandler(methodInvocationTimeStamp, index, mCallback));
    }

    private static void registerCallbackReturnTypes(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            TYPE_CENTER.register(method.getReturnType());
        }
    }

    private void setParameters(long methodInvocationTimeStamp, ParameterWrapper[] parameterWrappers) throws HermesException {
        if (parameterWrappers == null) {
            mParameters = null;
        } else {
            int length = parameterWrappers.length;
            mParameters = new Object[length];
            for (int i = 0; i < length; ++i) {
                ParameterWrapper parameterWrapper = parameterWrappers[i];
                if (parameterWrapper == null) {
                    mParameters[i] = null;
                } else {
                    Class<?> clazz = TYPE_CENTER.getClassType(parameterWrapper);
                    if (clazz != null && clazz.isInterface()) {
                        registerCallbackReturnTypes(clazz); //****
                        mParameters[i] = getProxy(clazz, i, methodInvocationTimeStamp);
                        HERMES_CALLBACK_GC.register(mCallback, mParameters[i], methodInvocationTimeStamp, i);
                    } else if (clazz != null && Context.class.isAssignableFrom(clazz)) {
                        mParameters[i] = Hermes.getContext();
                    } else {
                        String data = parameterWrapper.getData();
                        if (data == null) {
                            mParameters[i] = null;
                        } else {
                            mParameters[i] = CodeUtils.decode(data, clazz);
                        }
                    }
                }
            }
        }
    }

    protected abstract void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrappers)
            throws HermesException;

    protected abstract Object invokeMethod() throws HermesException;

    public final Reply action(long methodInvocationTimeStamp, MethodWrapper methodWrapper, ParameterWrapper[] parameterWrappers) throws HermesException{
        setMethod(methodWrapper, parameterWrappers);
        setParameters(methodInvocationTimeStamp, parameterWrappers);
        Object result = invokeMethod();
        if (result == null) {
            return null;
        } else {
            return new Reply(new ParameterWrapper(result));
        }
    }

}
