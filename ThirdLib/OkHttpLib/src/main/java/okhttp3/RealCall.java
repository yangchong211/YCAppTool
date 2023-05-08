/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okhttp3;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.Transmitter;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;
import okio.Timeout;

import static okhttp3.internal.Util.closeQuietly;
import static okhttp3.internal.platform.Platform.INFO;

/**
 * RealCall类构造创建对象，它是Call接口的具体实现类。
 */
final class RealCall implements Call {
    final OkHttpClient client;

    /**
     * There is a cycle between the {@link Call} and {@link Transmitter} that makes this awkward.
     * This is set after immediately after creating the call instance.
     */
    private Transmitter transmitter;

    /**
     * The application's original request unadulterated by redirects or auth headers.
     */
    final Request originalRequest;
    final boolean forWebSocket;

    // Guarded by this.
    private boolean executed;

    private RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        this.client = client;
        this.originalRequest = originalRequest;
        this.forWebSocket = forWebSocket;
    }

    /**
     * RealCall的构造函数需要传入一个OKHttpClient对象和Request对象(PS：第三个参数false表示不是webSokcet)。
     * 因此RealCall包装了Request对象。所以RealCall可以很方便地使用这两个对象。
     *
     * @param client
     * @param originalRequest
     * @param forWebSocket
     * @return
     */
    static RealCall newRealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        // Safely publish the Call instance to the EventListener.
        RealCall call = new RealCall(client, originalRequest, forWebSocket);
        call.transmitter = new Transmitter(client, call);
        return call;
    }

    @Override
    public Request request() {
        return originalRequest;
    }

    /**
     * 执行同步请求操作
     *
     * @return
     * @throws IOException
     */
    @Override
    public Response execute() throws IOException {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        transmitter.timeoutEnter();
        transmitter.callStart();
        try {
            client.dispatcher().executed(this);
            return getResponseWithInterceptorChain();
        } finally {
            client.dispatcher().finished(this);
        }
    }

    /**
     * 执行异步请求操作
     *
     * @param responseCallback
     */
    @Override
    public void enqueue(Callback responseCallback) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        transmitter.callStart();
        client.dispatcher().enqueue(new AsyncCall(responseCallback));
    }

    @Override
    public void cancel() {
        transmitter.cancel();
    }

    @Override
    public Timeout timeout() {
        return transmitter.timeout();
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

    @Override
    public boolean isCanceled() {
        return transmitter.isCanceled();
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    // We are a final type & this saves clearing state.
    @Override
    public RealCall clone() {
        return RealCall.newRealCall(client, originalRequest, forWebSocket);
    }

    final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;
        private volatile AtomicInteger callsPerHost = new AtomicInteger(0);

        AsyncCall(Callback responseCallback) {
            super("OkHttp %s", redactedUrl());
            this.responseCallback = responseCallback;
        }

        AtomicInteger callsPerHost() {
            return callsPerHost;
        }

        void reuseCallsPerHostFrom(AsyncCall other) {
            this.callsPerHost = other.callsPerHost;
        }

        String host() {
            return originalRequest.url().host();
        }

        Request request() {
            return originalRequest;
        }

        RealCall get() {
            return RealCall.this;
        }

        /**
         * Attempt to enqueue this async call on {@code executorService}. This will attempt to clean up
         * if the executor has been shut down by reporting the call as failed.
         */
        void executeOn(ExecutorService executorService) {
            assert (!Thread.holdsLock(client.dispatcher()));
            boolean success = false;
            try {
                executorService.execute(this);
                success = true;
            } catch (RejectedExecutionException e) {
                InterruptedIOException ioException = new InterruptedIOException("executor rejected");
                ioException.initCause(e);
                transmitter.noMoreExchanges(ioException);
                responseCallback.onFailure(RealCall.this, ioException);
            } finally {
                if (!success) {
                    client.dispatcher().finished(this); // This call is no longer running!
                }
            }
        }

        @Override
        protected void execute() {
            boolean signalledCallback = false;
            transmitter.timeoutEnter();
            try {
                Response response = getResponseWithInterceptorChain();
                signalledCallback = true;
                responseCallback.onResponse(RealCall.this, response);
            } catch (IOException e) {
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    Platform.get().log(INFO, "Callback failure for " + toLoggableString(), e);
                } else {
                    responseCallback.onFailure(RealCall.this, e);
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }
    }

    /**
     * Returns a string that describes this call. Doesn't include a full URL as that might contain
     * sensitive information.
     */
    String toLoggableString() {
        return (isCanceled() ? "canceled " : "")
                + (forWebSocket ? "web socket" : "call")
                + " to " + redactedUrl();
    }

    String redactedUrl() {
        return originalRequest.url().redact();
    }

    Response getResponseWithInterceptorChain() throws IOException {
        // Build a full stack of interceptors.
        List<Interceptor> interceptors = new ArrayList<>();
        //添加开发者应用层自定义的Interceptor
        interceptors.addAll(client.interceptors());
        //这个Interceptor是处理请求失败的重试，重定向
        interceptors.add(new RetryAndFollowUpInterceptor(client));
        //这个Interceptor工作是添加一些请求的头部或其他信息
        //并对返回的Response做一些友好的处理（有一些信息你可能并不需要）
        interceptors.add(new BridgeInterceptor(client.cookieJar()));
        //这个Interceptor的职责是判断缓存是否存在，读取缓存，更新缓存等等
        interceptors.add(new CacheInterceptor(client.internalCache()));
        //这个Interceptor的职责是建立客户端和服务器的连接
        interceptors.add(new ConnectInterceptor(client));
        if (!forWebSocket) {
            //添加开发者自定义的网络层拦截器
            interceptors.addAll(client.networkInterceptors());
        }
        //最后 添加 负责向服务器发送请求数据、从服务器读取响应数据的 CallServerInterceptor。
        interceptors.add(new CallServerInterceptor(forWebSocket));

        Interceptor.Chain chain = new RealInterceptorChain(interceptors, transmitter, null, 0,
                originalRequest, this, client.connectTimeoutMillis(),
                client.readTimeoutMillis(), client.writeTimeoutMillis());

        boolean calledNoMoreExchanges = false;
        try {
            //把chain传递到第一个Interceptor手中
            Response response = chain.proceed(originalRequest);
            if (transmitter.isCanceled()) {
                closeQuietly(response);
                throw new IOException("Canceled");
            }
            return response;
        } catch (IOException e) {
            calledNoMoreExchanges = true;
            throw transmitter.noMoreExchanges(e);
        } finally {
            if (!calledNoMoreExchanges) {
                transmitter.noMoreExchanges(null);
            }
        }
    }
}
