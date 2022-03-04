package com.yc.webviewlib.wv;

public interface WvJsHandler<T,R> {
    void handler(T data, ResponseCallback<R> callback);
}
