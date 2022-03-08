package com.yc.ipc.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.yc.ipc.HermesService;
import com.yc.ipc.internal.Channel;

public class HermesGc {

    private static volatile HermesGc sInstance = null;

    private final ReferenceQueue<Object> mReferenceQueue;

    private static final Channel CHANNEL = Channel.getInstance();

    private final ConcurrentHashMap<PhantomReference<Object>, Long> mTimeStamps;

    private final ConcurrentHashMap<Long, Class<? extends HermesService>> mServices;

    private HermesGc() {
        mReferenceQueue = new ReferenceQueue<Object>();
        mTimeStamps = new ConcurrentHashMap<PhantomReference<Object>, Long>();
        mServices = new ConcurrentHashMap<Long, Class<? extends HermesService>>();
    }

    public static HermesGc getInstance() {
        if (sInstance == null) {
            synchronized (HermesGc.class) {
                if (sInstance == null) {
                    sInstance = new HermesGc();
                }
            }
        }
        return sInstance;
    }

    private void gc() {
        synchronized (mReferenceQueue) {
            PhantomReference<Object> reference;
            Long timeStamp;
            HashMap<Class<? extends HermesService>, ArrayList<Long>> timeStamps
                    = new HashMap<Class<? extends HermesService>, ArrayList<Long>>();
            while ((reference = (PhantomReference<Object>) mReferenceQueue.poll()) != null) {
                //After a long time, the program can reach here.
                timeStamp = mTimeStamps.remove(reference);
                if (timeStamp != null) {
                    Class<? extends HermesService> clazz = mServices.remove(timeStamp);
                    if (clazz != null) {
                        ArrayList<Long> tmp = timeStamps.get(clazz);
                        if (tmp == null) {
                            tmp = new ArrayList<Long>();
                            timeStamps.put(clazz, tmp);
                        }
                        tmp.add(timeStamp);
                    }
                }
            }
            Set<Map.Entry<Class<? extends HermesService>, ArrayList<Long>>> set = timeStamps.entrySet();
            for (Map.Entry<Class<? extends HermesService>, ArrayList<Long>> entry : set) {
                ArrayList<Long> values = entry.getValue();
                if (!values.isEmpty()) {
                    CHANNEL.gc(entry.getKey(), values);
                }
            }
        }
    }

    public void register(Class<? extends HermesService> service, Object object, Long timeStamp) {
        gc();
        mTimeStamps.put(new PhantomReference<Object>(object, mReferenceQueue), timeStamp);
        mServices.put(timeStamp, service);
    }
}
