package com.yc.ipc.util;

import android.util.Log;

import androidx.core.util.Pair;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;


public class CallbackManager {

    private static final String TAG = "CallbackManager";

    private static final int MAX_INDEX = 10;

    private static volatile CallbackManager sInstance = null;

    private final ConcurrentHashMap<Long, CallbackWrapper> mCallbackWrappers;

    private CallbackManager() {
        mCallbackWrappers = new ConcurrentHashMap<Long, CallbackWrapper>();
    }

    public static CallbackManager getInstance() {
        if (sInstance == null) {
            synchronized (CallbackManager.class) {
                if (sInstance == null) {
                    sInstance = new CallbackManager();
                }
            }
        }
        return sInstance;
    }

    private static long getKey(long timeStamp, int index) {
        if (index >= MAX_INDEX) {
            throw new IllegalArgumentException("Index should be less than " + MAX_INDEX);
        }
        return timeStamp * MAX_INDEX + index;
    }

    public void addCallback(long timeStamp, int index, Object callback, boolean isWeakRef, boolean uiThread) {
        long key = getKey(timeStamp, index);
        mCallbackWrappers.put(key, new CallbackWrapper(isWeakRef, callback, uiThread));
    }

    public Pair<Boolean, Object> getCallback(long timeStamp, int index) {
        long key = getKey(timeStamp, index);
        CallbackWrapper callbackWrapper = mCallbackWrappers.get(key);
        if (callbackWrapper == null) {
            return null;
        }
        Pair<Boolean, Object> pair = callbackWrapper.get();
        if (pair.second == null) {
            mCallbackWrappers.remove(key);
        }
        return pair;
    }

    public void removeCallback(long timeStamp, int index) {
        long key = getKey(timeStamp, index);
        if (mCallbackWrappers.remove(key) == null) {
            Log.e(TAG, "An error occurs in the callback GC.");
        }
    }

    private static class CallbackWrapper {

        private Object mCallback;

        private boolean mUiThread;

        CallbackWrapper(boolean isWeakRef, Object callback, boolean uiThread) {
            if (isWeakRef) {
                mCallback = new WeakReference<Object>(callback);
            } else {
                mCallback = callback;
            }
            mUiThread = uiThread;
        }

        public Pair<Boolean, Object> get() {
            Object callback;
            if (mCallback instanceof WeakReference) {
                callback = ((WeakReference<Object>) mCallback).get();
            } else {
                callback = mCallback;
            }
            return new Pair<Boolean, Object>(mUiThread, callback);
        }
    }
}
