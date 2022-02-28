package cn.ycbjie.ycthreadpoollib.builder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class CachedBuilder extends ThreadPoolBuilder<ExecutorService> {

    public CachedBuilder() {
    }

    protected ExecutorService create(MyThreadFactory myThreadFactory) {
        return Executors.newCachedThreadPool(myThreadFactory);
    }

    protected ThreadPoolType getType() {
        return ThreadPoolType.CACHED;
    }
}
