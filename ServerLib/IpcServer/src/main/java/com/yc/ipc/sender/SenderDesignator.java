

package com.yc.ipc.sender;

import com.yc.ipc.HermesService;
import com.yc.ipc.wrapper.ObjectWrapper;

/**
 * Created by yangchong on 16/4/8.
 */
public class SenderDesignator {

    public static final int TYPE_NEW_INSTANCE = 0;

    public static final int TYPE_GET_INSTANCE = 1;

    public static final int TYPE_GET_UTILITY_CLASS = 2;

    public static final int TYPE_INVOKE_METHOD = 3;

    public static Sender getPostOffice(Class<? extends HermesService> service, int type, ObjectWrapper object) {
        switch (type) {
            case TYPE_NEW_INSTANCE:
                return new InstanceCreatingSender(service, object);
            case TYPE_GET_INSTANCE:
                return new InstanceGettingSender(service, object);
            case TYPE_GET_UTILITY_CLASS:
                return new UtilityGettingSender(service, object);
            case TYPE_INVOKE_METHOD:
                return new ObjectSender(service, object);
            default:
                throw new IllegalArgumentException("Type " + type + " is not supported.");
        }
    }

}
