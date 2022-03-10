

package com.yc.ipc.util;

import android.os.RemoteException;

import androidx.core.util.Pair;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.yc.ipc.internal.IHermesServiceCallback;

/**
 * Created by yangchong on 16/7/1.
 *
 * This works in the main process.
 */
public class HermesCallbackGc {

    private static volatile HermesCallbackGc sInstance = null;

    private final ReferenceQueue<Object> mReferenceQueue;

    private final ConcurrentHashMap<PhantomReference<Object>, Triple<IHermesServiceCallback, Long, Integer>> mTimeStamps;

    private HermesCallbackGc() {
        mReferenceQueue = new ReferenceQueue<Object>();
        mTimeStamps = new ConcurrentHashMap<PhantomReference<Object>, Triple<IHermesServiceCallback, Long, Integer>>();
    }

    public static HermesCallbackGc getInstance() {
        if (sInstance == null) {
            synchronized (HermesCallbackGc.class) {
                if (sInstance == null) {
                    sInstance = new HermesCallbackGc();
                }
            }
        }
        return sInstance;
    }

    private void gc() {
        synchronized (mReferenceQueue) {
            PhantomReference<Object> reference;
            Triple<IHermesServiceCallback, Long, Integer> triple;
            HashMap<IHermesServiceCallback, Pair<ArrayList<Long>, ArrayList<Integer>>> timeStamps
                    = new HashMap<IHermesServiceCallback, Pair<ArrayList<Long>, ArrayList<Integer>>>();
            while ((reference = (PhantomReference<Object>) mReferenceQueue.poll()) != null) {
                triple = mTimeStamps.remove(reference);
                if (triple != null) {
                    Pair<ArrayList<Long>, ArrayList<Integer>> tmp = timeStamps.get(triple.first);
                    if (tmp == null) {
                        tmp = new Pair<ArrayList<Long>, ArrayList<Integer>>(new ArrayList<Long>(), new ArrayList<Integer>());
                        timeStamps.put(triple.first, tmp);
                    }
                    tmp.first.add(triple.second);
                    tmp.second.add(triple.third);
                }
            }
            Set<Map.Entry<IHermesServiceCallback, Pair<ArrayList<Long>, ArrayList<Integer>>>> set = timeStamps.entrySet();
            for (Map.Entry<IHermesServiceCallback, Pair<ArrayList<Long>, ArrayList<Integer>>> entry : set) {
                Pair<ArrayList<Long>, ArrayList<Integer>> values = entry.getValue();
                if (!values.first.isEmpty()) {
                    try {
                        entry.getKey().gc(values.first, values.second);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void register(IHermesServiceCallback callback, Object object, long timeStamp, int index) {
        gc();
        mTimeStamps.put(new PhantomReference<Object>(object, mReferenceQueue), Triple.create(callback, timeStamp, index));
    }
}
