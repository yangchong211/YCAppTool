package com.yc.logging.util;


public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}