

package com.yc.ipc.internal;

import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.TypeUtils;
import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/11.
 */
public class HermesCallbackInvocationHandler implements InvocationHandler {

    private static final String TAG = "HERMES_CALLBACK";

    private long mTimeStamp;

    private int mIndex;

    private IHermesServiceCallback mCallback;

    public HermesCallbackInvocationHandler(long timeStamp, int index, IHermesServiceCallback callback) {
        mTimeStamp = timeStamp;
        mIndex = index;
        mCallback = callback;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) {
        try {
            MethodWrapper methodWrapper = new MethodWrapper(method);
            ParameterWrapper[] parameterWrappers = TypeUtils.objectToWrapper(objects);
            CallbackMail callbackMail = new CallbackMail(mTimeStamp, mIndex, methodWrapper, parameterWrappers);
            Reply reply = mCallback.callback(callbackMail);
            if (reply == null) {
                return null;
            }
            if (reply.success()) {
                /**
                 * Note that the returned type should be registered in the remote process.
                 */
                return reply.getResult();
            } else {
                Log.e(TAG, "Error occurs: " + reply.getMessage());
                return null;
            }
        } catch (HermesException e) {
            Log.e(TAG, "Error occurs but does not crash the app.", e);
        } catch (RemoteException e) {
            Log.e(TAG, "Error occurs but does not crash the app.", e);
        }
        return null;
    }
}
