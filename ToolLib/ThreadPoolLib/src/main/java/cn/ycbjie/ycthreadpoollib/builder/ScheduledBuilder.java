package cn.ycbjie.ycthreadpoollib.builder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
public class ScheduledBuilder extends ThreadPoolBuilder<ExecutorService> {

    private int mSize = 1;

    public ScheduledBuilder() {
    }

    protected ScheduledExecutorService create(MyThreadFactory myThreadFactory) {
        return Executors.newScheduledThreadPool(this.mSize, myThreadFactory);
    }

    protected ThreadPoolType getType() {
        return ThreadPoolType.SCHEDULED;
    }

    public ScheduledBuilder setSize(int size) {
        this.mSize = size;
        return this;
    }
}

