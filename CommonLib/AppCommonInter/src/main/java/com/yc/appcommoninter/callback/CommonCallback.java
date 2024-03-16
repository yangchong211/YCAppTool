package com.yc.appcommoninter.callback;

/**
 * 通用回调
 * @param <T>
 */
public interface CommonCallback<T> {
    void result(T data);
}
