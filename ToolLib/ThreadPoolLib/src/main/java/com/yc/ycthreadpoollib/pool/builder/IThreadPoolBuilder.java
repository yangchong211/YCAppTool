package com.yc.ycthreadpoollib.pool.builder;

import com.yc.ycthreadpoollib.pool.config.ThreadPoolType;
import com.yc.ycthreadpoollib.pool.factory.MyThreadFactory;

import java.util.concurrent.ExecutorService;

public interface IThreadPoolBuilder<T extends ExecutorService> {

     T create(MyThreadFactory myThreadFactory);

     ThreadPoolType getType();

}
