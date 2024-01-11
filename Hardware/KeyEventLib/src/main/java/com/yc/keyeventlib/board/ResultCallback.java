package com.yc.keyeventlib.board;

/**
 * 带返回值的的回调
 */
public interface ResultCallback<T> {

    boolean result(T t);

}
