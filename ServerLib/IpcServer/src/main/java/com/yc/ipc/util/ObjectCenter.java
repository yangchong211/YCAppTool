

package com.yc.ipc.util;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangchong on 16/4/8.
 */
public class ObjectCenter {

    private static final String TAG = "ObjectCenter";

    private static volatile ObjectCenter sInstance = null;

    private final ConcurrentHashMap<Long, Object> mObjects;

    private ObjectCenter() {
        mObjects = new ConcurrentHashMap<Long, Object>();
    }

    public static ObjectCenter getInstance() {
        if (sInstance == null) {
            synchronized (ObjectCenter.class) {
                if (sInstance == null) {
                    sInstance = new ObjectCenter();
                }
            }
        }
        return sInstance;
    }

    public Object getObject(Long timeStamp) {
        return mObjects.get(timeStamp);
    }

    public void putObject(long timeStamp, Object object) {
        mObjects.put(timeStamp, object);
    }

    public void deleteObjects(List<Long> timeStamps) {
        for (Long timeStamp : timeStamps) {
            if (mObjects.remove(timeStamp) == null) {
                Log.e(TAG, "An error occurs in the GC.");
            }
        }
    }
}
