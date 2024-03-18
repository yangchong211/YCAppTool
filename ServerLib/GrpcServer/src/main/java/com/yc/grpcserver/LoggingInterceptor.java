package com.yc.grpcserver;

import android.util.Log;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class LoggingInterceptor implements ServerInterceptor, ClientInterceptor {

    private static final String TAG = "gRPC-yc";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        // 在请求发送前记录日志
        Log.d(TAG,"Sending request: " + method.getFullMethodName());
        // 调用下一个拦截器或服务实现
        return next.newCall(method,callOptions);
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        // 在请求接收前记录日志
        Log.d(TAG,"Received request: " + call.getMethodDescriptor().getFullMethodName());
        // 调用下一个拦截器或服务实现
        return next.startCall(call,headers);
    }
}
