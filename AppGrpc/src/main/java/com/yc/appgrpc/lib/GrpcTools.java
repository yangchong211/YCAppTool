package com.yc.appgrpc.lib;

public class GrpcTools {

    private static volatile GrpcTools singleton = null;
    private IServiceApi serviceApi;


    public static GrpcTools getInstance() {
        if (singleton == null) {
            synchronized (GrpcTools.class) {
                if (singleton == null) {
                    singleton = new GrpcTools();
                }
            }
        }
        return singleton;
    }

    public GrpcTools(){
        serviceApi = new ServiceApiImpl();
    }

    public IServiceApi getServiceApi() {
        return serviceApi;
    }
}
