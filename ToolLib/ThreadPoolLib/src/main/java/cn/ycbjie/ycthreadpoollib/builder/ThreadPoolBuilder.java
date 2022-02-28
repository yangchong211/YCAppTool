package cn.ycbjie.ycthreadpoollib.builder;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import cn.ycbjie.ycthreadpoollib.factory.MyThreadFactory;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : 抽象类
 *     revise:
 * </pre>
 */
public abstract class ThreadPoolBuilder<T extends ExecutorService> {

    protected static Map<String, ExecutorService> mThreadPoolMap = new ConcurrentHashMap<>();
    protected ExecutorService mExecutorService = null;
    protected String mPoolName = "default";

    public ThreadPoolBuilder() {
    }

    protected abstract T create(MyThreadFactory myThreadFactory);

    protected abstract ThreadPoolType getType();

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

    public ThreadPoolBuilder<T> poolName(String poolName) {
        if (poolName != null && poolName.length() > 0) {
            this.mPoolName = poolName;
        }
        return this;
    }
}

