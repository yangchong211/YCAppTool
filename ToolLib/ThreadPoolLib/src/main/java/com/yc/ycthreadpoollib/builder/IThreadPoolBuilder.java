package com.yc.ycthreadpoollib.builder;

import com.yc.ycthreadpoollib.config.ThreadPoolType;
import com.yc.ycthreadpoollib.factory.MyThreadFactory;
import com.yc.ycthreadpoollib.config.ThreadPoolType;
import com.yc.ycthreadpoollib.factory.MyThreadFactory;

import java.util.concurrent.ExecutorService;

public interface IThreadPoolBuilder<T extends ExecutorService> {

     T create(MyThreadFactory myThreadFactory);

     ThreadPoolType getType();

}
