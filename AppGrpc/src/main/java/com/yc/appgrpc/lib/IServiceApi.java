package com.yc.appgrpc.lib;

import com.yc.grpcserver.GrpcCallback;

public interface IServiceApi {

    HelloResponse sayHello(HelloRequest request);

    void sayHello(HelloRequest request , GrpcCallback<HelloResponse> callback);

}
