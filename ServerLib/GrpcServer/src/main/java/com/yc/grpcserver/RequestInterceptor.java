package com.yc.grpcserver;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;

/**
 * 拦截
 */
public class RequestInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        // 创建一个自定义的 ClientCall 实现，用于监听请求状态
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // 在请求开始时执行自定义逻辑
                // 可以在这里记录请求开始时间、添加请求头等操作
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onClose(Status status, Metadata trailers) {
                        // 在请求关闭时执行自定义逻辑
                        // 可以在这里记录请求结束时间、处理响应状态等操作
                        super.onClose(status, trailers);
                    }
                }, headers);
            }
        };
    }
}
