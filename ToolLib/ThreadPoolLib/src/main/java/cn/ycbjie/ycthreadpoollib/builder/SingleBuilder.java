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
public class SingleBuilder extends ThreadPoolBuilder<ExecutorService> {

    public SingleBuilder() {
    }

    protected ExecutorService create(MyThreadFactory myThreadFactory) {
        return Executors.newSingleThreadExecutor(myThreadFactory);
    }

    protected ThreadPoolType getType() {
        return ThreadPoolType.SINGLE;
    }
}
