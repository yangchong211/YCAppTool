

package com.yc.ipc.sender;

import java.lang.reflect.Method;

import com.yc.ipc.HermesService;
import com.yc.ipc.util.ErrorCodes;
import com.yc.ipc.util.CodeUtils;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ObjectWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public class InstanceGettingSender extends Sender {

    public InstanceGettingSender(Class<? extends HermesService> service, ObjectWrapper object) {
        super(service, object);
    }

    @Override
    protected void setParameterWrappers(ParameterWrapper[] parameterWrappers) {
        int length = parameterWrappers.length;
        ParameterWrapper[] tmp = new ParameterWrapper[length - 1];
        for (int i = 1; i < length; ++i) {
            tmp[i - 1] = parameterWrappers[i];
        }
        super.setParameterWrappers(tmp);
    }

    @Override
    protected MethodWrapper getMethodWrapper(Method method, ParameterWrapper[] parameterWrappers) throws HermesException {
        ParameterWrapper parameterWrapper = parameterWrappers[0];
        String methodName;
        try {
            methodName = CodeUtils.decode(parameterWrapper.getData(), String.class);
        } catch (HermesException e) {
            e.printStackTrace();
            throw new HermesException(ErrorCodes.GSON_DECODE_EXCEPTION,
                    "Error occurs when decoding the method name.");
        }
        int length = parameterWrappers.length;
        Class<?>[] parameterTypes = new Class[length - 1];
        for (int i = 1; i < length; ++i) {
            parameterWrapper = parameterWrappers[i];
            parameterTypes[i - 1] = parameterWrapper == null ? null : parameterWrapper.getClassType();
        }
        return new MethodWrapper(methodName, parameterTypes);
    }
}
