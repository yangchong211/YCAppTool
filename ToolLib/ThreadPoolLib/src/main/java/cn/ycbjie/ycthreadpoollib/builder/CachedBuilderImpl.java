package cn.ycbjie.ycthreadpoollib.builder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ycbjie.ycthreadpoollib.config.ThreadPoolType;
import cn.ycbjie.ycthreadpoollib.factory.MyThreadFactory;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : SingleBuilder
 *     revise:
 * </pre>
 */
public class CachedBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    public CachedBuilderImpl() {
    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        return Executors.newCachedThreadPool(myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.CACHED;
    }
}
