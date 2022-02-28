package cn.ycbjie.ycthreadpoollib.builder;

import java.util.concurrent.ExecutorService;

import cn.ycbjie.ycthreadpoollib.config.ThreadPoolType;
import cn.ycbjie.ycthreadpoollib.factory.MyThreadFactory;

public interface IThreadPoolBuilder<T extends ExecutorService> {

     T create(MyThreadFactory myThreadFactory);

     ThreadPoolType getType();

}
