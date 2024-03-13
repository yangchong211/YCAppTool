package com.yc.store.pool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Pool有个很好的特点：它并不需要预先创建对象到对象池，它需要在release方法中将回收对象添加到复用的对象池中
 * @param <T>
 */
public interface Pool<T> {

    /**
     * 获取对象
     */
    @Nullable
    T acquire();

    /**
     * 释放对象，存入池中
     */
    boolean release(@NonNull T instance);

}
