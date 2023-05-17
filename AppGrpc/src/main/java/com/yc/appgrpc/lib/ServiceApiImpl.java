package com.yc.appgrpc.lib;

import com.yc.grpcserver.GrpcCallback;

public class ServiceApiImpl implements IServiceApi{


    @Override
    public HelloResponse sayHello(HelloRequest request) {
        return null;
    }

    @Override
    public void sayHello(HelloRequest request, GrpcCallback<HelloResponse> callback) {

    }


}
