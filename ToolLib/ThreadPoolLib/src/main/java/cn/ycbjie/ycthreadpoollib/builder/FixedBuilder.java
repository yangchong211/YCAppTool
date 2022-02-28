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
public class FixedBuilder extends ThreadPoolBuilder<ExecutorService> {

    private int mSize = 1;

    public FixedBuilder() {
    }

    protected ExecutorService create(MyThreadFactory myThreadFactory) {
        return Executors.newFixedThreadPool(this.mSize, myThreadFactory);
    }

    protected ThreadPoolType getType() {
        return ThreadPoolType.FIXED;
    }

    public FixedBuilder setSize(int size) {
        this.mSize = size;
        return this;
    }
}
