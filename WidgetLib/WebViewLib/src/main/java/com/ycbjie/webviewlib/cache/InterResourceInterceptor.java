package com.ycbjie.webviewlib.cache;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/17
 *     desc  : 自定义资源拦截起接口
 *     revise:
 * </pre>
 */
public interface InterResourceInterceptor {
    boolean interceptor(String url);
}
