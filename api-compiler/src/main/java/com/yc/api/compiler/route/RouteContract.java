package com.yc.api.compiler.route;

public class RouteContract<T> {

    private T api;
    private T apiImpl;

    public RouteContract(T api, T apiImpl) {
        this.api = api;
        this.apiImpl = apiImpl;
    }

    public T getApi() {
        return api;
    }

    public T getApiImpl() {
        return apiImpl;
    }
}
