

package com.yc.ipc.sender;

import java.lang.reflect.Method;

import com.yc.ipc.HermesService;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ObjectWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public class InstanceCreatingSender extends Sender {

    private Class<?>[] mConstructorParameterTypes;

    public InstanceCreatingSender(Class<? extends HermesService> service, ObjectWrapper object) {
        super(service, object);
    }

    @Override
    protected MethodWrapper getMethodWrapper(Method method, ParameterWrapper[] parameterWrappers) {
        int length = parameterWrappers == null ? 0 : parameterWrappers.length;
        mConstructorParameterTypes = new Class<?>[length];
        for (int i = 0; i < length; ++i) {
            try {
                ParameterWrapper parameterWrapper = parameterWrappers[i];
                mConstructorParameterTypes[i] = parameterWrapper == null ? null : parameterWrapper.getClassType();
            } catch (Exception e) {

            }
        }
        return new MethodWrapper(mConstructorParameterTypes);
    }


}
