

package com.yc.ipc.sender;

import java.lang.reflect.Method;

import com.yc.ipc.HermesService;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ObjectWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public class UtilityGettingSender extends Sender {

    public UtilityGettingSender(Class<? extends HermesService> service, ObjectWrapper object) {
        super(service, object);
    }

    @Override
    protected MethodWrapper getMethodWrapper(Method method, ParameterWrapper[] parameterWrappers) {
        return null;
    }
}
