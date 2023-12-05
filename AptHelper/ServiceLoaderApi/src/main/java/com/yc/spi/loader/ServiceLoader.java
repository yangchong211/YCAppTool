package com.yc.spi.loader;

import com.yc.spi.annotation.ServiceProvider;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ServiceConfigurationError;
import java.util.Set;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2021/04/15
 * @desc : 自定义ServiceLoader，替代原生的
 * @revise :
 */
public final class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(final Class<S> serviceClass) {
        return new ServiceLoader<S>(serviceClass);
    }

    public static <S> ServiceLoader<S> load(final Class<S> serviceClass, final String alias) {
        return new ServiceLoader<S>(serviceClass, alias);
    }

    private final Class<S> mService;
    private final String mAlias;
    private final Set<S> mProviders = new LinkedHashSet<S>();

    private ServiceLoader(final Class<S> service) {
        this(service, null);
    }

    public ServiceLoader(Class<S> service, String alias) {
        this.mService = service;
        this.mAlias = alias;
        this.load();
    }

    public S get() {
        final Iterator<S> i = this.mProviders.iterator();
        if (i.hasNext()) {
            return i.next();
        }

        return null;
    }

    @Override
    public Iterator<S> iterator() {
        return Collections.unmodifiableSet(this.mProviders).iterator();
    }

    private void load() {
        Set<Class<? extends S>> serviceProviders = getServiceProviders();
        for (final Class<? extends S> provider : serviceProviders) {
            try {
                final S p = ServiceRegistry.newProvider(provider);
                this.mProviders.add(p);
            } catch (final Throwable t) {
                throw new ServiceConfigurationError("Provider " + provider.getName() + " could not be initialized", t);
            }
        }
    }

    private Set<Class<? extends S>> getServiceProviders() {
        final Set<Class<? extends  S>> all = ServiceRegistry.get(this.mService);
        if (null == this.mAlias) {
            return all;
        }

        final Set<Class<? extends  S>> classes = new LinkedHashSet<>();
        for (final Class<? extends S> clazz : all) {
            final ServiceProvider sp = clazz.getAnnotation(ServiceProvider.class);
            if (null != sp && this.mAlias.equals(sp.alias())) {
                classes.add(clazz);
            }
        }
        return classes;
    }

}
