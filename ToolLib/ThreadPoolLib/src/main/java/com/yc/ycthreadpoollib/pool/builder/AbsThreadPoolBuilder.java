package com.yc.ycthreadpoollib.pool.builder;


import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : 抽象类
 *     revise:
 * </pre>
 */
public abstract class AbsThreadPoolBuilder<T extends ExecutorService> implements IThreadPoolBuilder<T>{

    /**
     * ConcurrentHashMap 多线程下数据安全
     */
    protected static Map<String, ExecutorService> mThreadPoolMap = new ConcurrentHashMap<>();
    protected ExecutorService mExecutorService = null;
    protected String mPoolName = "default";

    public AbsThreadPoolBuilder() {
    }


    public ExecutorService builder(MyThreadFactory myThreadFactory) {
        String globalPoolName = "YC_" + this.getType() + "_" + this.mPoolName;
        if (mThreadPoolMap.get(globalPoolName) != null) {
            this.mExecutorService = (ExecutorService)mThreadPoolMap.get(globalPoolName);
        } else {
            this.mExecutorService = this.create(myThreadFactory);
            mThreadPoolMap.put(globalPoolName, this.mExecutorService);
        }
        return this.mExecutorService;
    }

    public ExecutorService getExecutorService() {
        return this.mExecutorService;
    }

    public AbsThreadPoolBuilder<T> poolName(String poolName) {
        if (poolName != null && poolName.length() > 0) {
            this.mPoolName = poolName;
        }
        return this;
    }
}

