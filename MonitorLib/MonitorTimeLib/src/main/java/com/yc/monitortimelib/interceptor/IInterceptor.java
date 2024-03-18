package com.yc.monitortimelib.interceptor;


/**
 * @author : yangchong
 * @email : yangchong211@163.com
 * @time : 2017/5/18
 * @desc : 拦截器，每条请求都会调用拦截器
 * revise :
 */
public interface IInterceptor {

    /**
     * 添加拦截器
     *
     * @param interceptorCallback 拦截器
     */
    void addInterceptor(CommonCallback<InterceptorBean> interceptorCallback);

    /**
     * 删除拦截器
     *
     * @param interceptorCallback 拦截器
     */
    void removeInterceptor(CommonCallback<InterceptorBean> interceptorCallback);
}
