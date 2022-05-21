package com.yc.spi.loader;

import java.util.Set;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2021/04/15
 * @desc : 自定义ServiceRegistry，替代原生的
 * @revise :
 */
public abstract class ServiceRegistry {

    private ServiceRegistry() {
        
    }

    public static synchronized <S, P extends S> void register(
            final Class<S> serviceClass, final Class<P> providerClass) {
        throw new RuntimeException("Stub!");
    }

    public static synchronized <S> Set<Class<? extends S>> get(final Class<S> clazz) {
        throw new RuntimeException("Stub!");
    }

    public static synchronized <S> S newProvider(final Class<? extends S> clazz) {
        throw new RuntimeException("Stub!");
    }

}
