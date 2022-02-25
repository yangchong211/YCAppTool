package com.yc.logging.util;

import com.yc.logging.annotation.KeepSource;

@KeepSource
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}