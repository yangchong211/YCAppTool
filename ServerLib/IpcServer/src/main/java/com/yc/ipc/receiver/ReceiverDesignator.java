

package com.yc.ipc.receiver;

import com.yc.ipc.util.ErrorCodes;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.wrapper.ObjectWrapper;

/**
 * Created by yangchong on 16/4/10.
 */
public class ReceiverDesignator {
    public static Receiver getReceiver(ObjectWrapper objectWrapper) throws HermesException {
        int type = objectWrapper.getType();
        switch (type) {
            case ObjectWrapper.TYPE_OBJECT_TO_NEW:
                return new InstanceCreatingReceiver(objectWrapper);
            case ObjectWrapper.TYPE_OBJECT_TO_GET:
                return new InstanceGettingReceiver(objectWrapper);
            case ObjectWrapper.TYPE_CLASS:
                return new UtilityReceiver(objectWrapper);
            case ObjectWrapper.TYPE_OBJECT:
                return new ObjectReceiver(objectWrapper);
            case ObjectWrapper.TYPE_CLASS_TO_GET:
                return new UtilityGettingReceiver(objectWrapper);
            default:
                throw new HermesException(ErrorCodes.ILLEGAL_PARAMETER_EXCEPTION,
                        "Type " + type + " is not supported.");
        }
    }
}
