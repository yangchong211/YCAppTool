package com.yc.store.pool;

import androidx.annotation.NonNull;

/**
 * SynchronizedPool是继承SimplePool，里面通过Synchronized同步锁实现一个安全的对象池
 *
 * @param <T>
 */
public class SynchronizedPool<T> extends SimplePool<T> {
    private final Object mLock = new Object();

    public SynchronizedPool(int maxPoolSize) {
        super(maxPoolSize);
    }

    @Override
    public T acquire() {
        //通过synchronized实现同步
        synchronized (mLock) {
            return super.acquire();
        }
    }

    @Override
    public boolean release(@NonNull T element) {
        synchronized (mLock) {
            return super.release(element);
        }
    }
}
