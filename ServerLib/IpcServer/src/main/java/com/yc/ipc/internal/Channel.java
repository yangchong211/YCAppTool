
package com.yc.ipc.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.yc.ipc.HermesListener;
import com.yc.ipc.HermesService;
import com.yc.ipc.util.CallbackManager;
import com.yc.ipc.util.CodeUtils;
import com.yc.ipc.util.ErrorCodes;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.TypeCenter;
import com.yc.ipc.wrapper.ParameterWrapper;


public class Channel {

    private static final String TAG = "CHANNEL";

    private static volatile Channel sInstance = null;

    private final ConcurrentHashMap<Class<? extends HermesService>, IHermesService> mHermesServices = new ConcurrentHashMap<Class<? extends HermesService>, IHermesService>();

    private final ConcurrentHashMap<Class<? extends HermesService>, HermesServiceConnection> mHermesServiceConnections = new ConcurrentHashMap<Class<? extends HermesService>, HermesServiceConnection>();

    private final ConcurrentHashMap<Class<? extends HermesService>, Boolean> mBindings = new ConcurrentHashMap<Class<? extends HermesService>, Boolean>();

    private final ConcurrentHashMap<Class<? extends HermesService>, Boolean> mBounds = new ConcurrentHashMap<Class<? extends HermesService>, Boolean>();

    private HermesListener mListener = null;

    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    private static final CallbackManager CALLBACK_MANAGER = CallbackManager.getInstance();

    private static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();

    private final IHermesServiceCallback mHermesServiceCallback = new IHermesServiceCallback.Stub() {

        private Object[] getParameters(ParameterWrapper[] parameterWrappers) throws HermesException {
            if (parameterWrappers == null) {
                parameterWrappers = new ParameterWrapper[0];
            }
            int length = parameterWrappers.length;
            Object[] result = new Object[length];
            for (int i = 0; i < length; ++i) {
                ParameterWrapper parameterWrapper = parameterWrappers[i];
                if (parameterWrapper == null) {
                    result[i] = null;
                } else {
                    Class<?> clazz = TYPE_CENTER.getClassType(parameterWrapper);

                    String data = parameterWrapper.getData();
                    if (data == null) {
                        result[i] = null;
                    } else {
                        result[i] = CodeUtils.decode(data, clazz);
                    }
                }
            }
            return result;
        }

        public Reply callback(CallbackMail mail) {
            final Pair<Boolean, Object> pair = CALLBACK_MANAGER.getCallback(mail.getTimeStamp(), mail.getIndex());
            if (pair == null) {
                return null;
            }
            final Object callback = pair.second;
            if (callback == null) {
                return new Reply(ErrorCodes.CALLBACK_NOT_ALIVE, "");
            }
            boolean uiThread = pair.first;
            try {
                // TODO Currently, the callback should not be annotated!
                final Method method = TYPE_CENTER.getMethod(callback.getClass(), mail.getMethod());
                final Object[] parameters = getParameters(mail.getParameters());
                Object result = null;
                Exception exception = null;
                if (uiThread) {
                    boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
                    if (isMainThread) {
                        try {
                            result = method.invoke(callback, parameters);
                        } catch (IllegalAccessException e) {
                            exception = e;
                        } catch (InvocationTargetException e) {
                            exception = e;
                        }
                    } else {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    method.invoke(callback, parameters);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return null;
                    }
                } else {
                    try {
                        result = method.invoke(callback, parameters);
                    } catch (IllegalAccessException e) {
                        exception = e;
                    } catch (InvocationTargetException e) {
                        exception = e;
                    }
                }
                if (exception != null) {
                    exception.printStackTrace();
                    throw new HermesException(ErrorCodes.METHOD_INVOCATION_EXCEPTION,
                            "Error occurs when invoking method " + method + " on " + callback, exception);
                }
                if (result == null) {
                    return null;
                }
                return new Reply(new ParameterWrapper(result));
            } catch (HermesException e) {
                e.printStackTrace();
                return new Reply(e.getErrorCode(), e.getErrorMessage());
            }
        }

        @Override
        public void gc(List<Long> timeStamps, List<Integer> indexes) throws RemoteException {
            int size = timeStamps.size();
            for (int i = 0; i < size; ++i) {
                CALLBACK_MANAGER.removeCallback(timeStamps.get(i), indexes.get(i));
            }
        }
    };

    private Channel() {

    }

    public static Channel getInstance() {
        if (sInstance == null) {
            synchronized (Channel.class) {
                if (sInstance == null) {
                    sInstance = new Channel();
                }
            }
        }
        return sInstance;
    }

    public void bind(Context context, String packageName, Class<? extends HermesService> service) {
        HermesServiceConnection connection;
        synchronized (this) {
            if (getBound(service)) {
                return;
            }
            Boolean binding = mBindings.get(service);
            if (binding != null && binding) {
                return;
            }
            mBindings.put(service, true);
            connection = new HermesServiceConnection(service);
            mHermesServiceConnections.put(service, connection);
        }
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        }
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbind(Context context, Class<? extends HermesService> service) {
        synchronized (this) {
            Boolean bound = mBounds.get(service);
            if (bound != null && bound) {
                HermesServiceConnection connection = mHermesServiceConnections.get(service);
                if (connection != null) {
                    context.unbindService(connection);
                }
                mBounds.put(service, false);
            }
        }
    }

    public Reply send(Class<? extends HermesService> service, Mail mail) {
        IHermesService hermesService = mHermesServices.get(service);
        try {
            if (hermesService == null) {
                return new Reply(ErrorCodes.SERVICE_UNAVAILABLE,
                        "Service Unavailable: Check whether you have connected Hermes.");
            }
            return hermesService.send(mail);
        } catch (RemoteException e) {
            return new Reply(ErrorCodes.REMOTE_EXCEPTION, "Remote Exception: Check whether "
                    + "the process you are communicating with is still alive.");
        }
    }

    public void gc(Class<? extends HermesService> service, List<Long> timeStamps) {
        IHermesService hermesService = mHermesServices.get(service);
        if (hermesService == null) {
            Log.e(TAG, "Service Unavailable: Check whether you have disconnected the service before a process dies.");
        } else {
            try {
                hermesService.gc(timeStamps);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getBound(Class<? extends HermesService> service) {
        Boolean bound = mBounds.get(service);
        return bound != null && bound;
    }

    public void setHermesListener(HermesListener listener) {
        mListener = listener;
    }

    public boolean isConnected(Class<? extends HermesService> service) {
        IHermesService hermesService = mHermesServices.get(service);
        return hermesService != null && hermesService.asBinder().pingBinder();
    }

    private class HermesServiceConnection implements ServiceConnection {

        private Class<? extends HermesService> mClass;

        HermesServiceConnection(Class<? extends HermesService> service) {
            mClass = service;
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            synchronized (Channel.this) {
                mBounds.put(mClass, true);
                mBindings.put(mClass, false);
                IHermesService hermesService = IHermesService.Stub.asInterface(service);
                mHermesServices.put(mClass, hermesService);
                try {
                    hermesService.register(mHermesServiceCallback, Process.myPid());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Remote Exception: Check whether "
                            + "the process you are communicating with is still alive.");
                    return;
                }
            }
            if (mListener != null) {
                mListener.onHermesConnected(mClass);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            synchronized (Channel.this) {
                mHermesServices.remove(mClass);
                mBounds.put(mClass, false);
                mBindings.put(mClass, false);
            }
            if (mListener != null) {
                mListener.onHermesDisconnected(mClass);
            }
        }
    }
}
