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
public class FixedBuilderImpl extends AbsThreadPoolBuilder<ExecutorService> {

    private int mSize = 1;

    public FixedBuilderImpl() {
    }

    public ExecutorService create(MyThreadFactory myThreadFactory) {
        return Executors.newFixedThreadPool(this.mSize, myThreadFactory);
    }

    public ThreadPoolType getType() {
        return ThreadPoolType.FIXED;
    }

    public FixedBuilderImpl setSize(int size) {
        this.mSize = size;
        return this;
    }
}
