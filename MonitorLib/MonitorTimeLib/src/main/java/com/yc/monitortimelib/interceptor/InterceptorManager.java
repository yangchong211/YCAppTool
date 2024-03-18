package com.yc.monitortimelib.interceptor;

/**
 * @author : yangchong
 * @email : yangchong211@163.com
 * @time : 2017/5/18
 * @desc : 拦截器管理者
 * revise :
 */
public class InterceptorManager extends BaseInterceptor {

    private static volatile InterceptorManager singleton = null;

    public static InterceptorManager getInstance() {
        if (singleton == null) {
            synchronized (InterceptorManager.class) {
                if (singleton == null) {
                    singleton = new InterceptorManager();
                }
            }
        }
        return singleton;
    }

    private InterceptorManager() {

    }

}
