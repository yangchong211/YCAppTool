package com.yc.monitortimelib.interceptor;

/**
 * @author : yangchong
 * @email : yangchong211@163.com
 * @time : 2017/5/18
 * @desc : 拦截器
 * revise :
 */
public interface CommonCallback<T> {

    void result(T t);
}
