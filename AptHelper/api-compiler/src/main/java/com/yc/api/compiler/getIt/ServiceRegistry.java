package com.yc.api.compiler.getIt;

import java.util.Set;


public abstract class ServiceRegistry {

    private ServiceRegistry() {

    }

    public static synchronized <S, P extends S> void register(final Class<S> serviceClass,
                                                              final Class<P> providerClass) {
        throw new RuntimeException("Stub!");
    }

    public static synchronized <S> Set<Class<? extends S>> get(final Class<S> clazz) {
        throw new RuntimeException("Stub!");
    }

    static synchronized <S> S newProvider(final Class<? extends S> clazz) {
        throw new RuntimeException("Stub!");
    }

}
