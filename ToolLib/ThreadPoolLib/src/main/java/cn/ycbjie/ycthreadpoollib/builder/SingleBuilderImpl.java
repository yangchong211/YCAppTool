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
public class SingleBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    public SingleBuilderImpl() {
    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        return Executors.newSingleThreadExecutor(myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.SINGLE;
    }
}
