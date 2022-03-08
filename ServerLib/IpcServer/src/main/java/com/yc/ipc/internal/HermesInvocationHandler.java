

package com.yc.ipc.internal;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.yc.ipc.sender.Sender;
import com.yc.ipc.sender.SenderDesignator;
import com.yc.ipc.HermesService;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.wrapper.ObjectWrapper;

/**
 * Created by yangchong on 16/4/11.
 */
public class HermesInvocationHandler implements InvocationHandler {

    private static final String TAG = "HERMES_INVOCATION";

    private Sender mSender;

    public HermesInvocationHandler(Class<? extends HermesService> service, ObjectWrapper object) {
        mSender = SenderDesignator.getPostOffice(service, SenderDesignator.TYPE_INVOKE_METHOD, object);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) {
        try {
            Reply reply = mSender.send(method, objects);
            if (reply == null) {
                return null;
            }
            if (reply.success()) {
                return reply.getResult();
            } else {
                Log.e(TAG, "Error occurs. Error " + reply.getErrorCode() + ": " + reply.getMessage());
                return null;
            }
        } catch (HermesException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurs. Error " + e.getErrorCode() + ": " + e.getErrorMessage());
            return null;
        }
    }
}
