package com.yc.grpcserver;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import io.grpc.stub.AbstractStub;

public class GrpcStubManager {

    public static final String G_ZIP = "gzip";
    public static final String NO_ZIP = "none";
    //设置是否全局开启请求压缩，默认关闭的
    private static boolean useZip;

    public static boolean isUseZip() {
        return useZip;
    }

    public static void setUseZip(boolean _useZip) {
        useZip = _useZip;
    }


    /**
     * 存根修饰
     *
     * @param stub    存根
     * @param request 请求
     * @return 新的存根
     */
    @SuppressLint("CheckResult")
    public static <T extends AbstractStub<T>> T createStub(T stub, BaseRequest request) {
        //开启压缩，如果单个方法设置过，单个方法优先
        if (!TextUtils.isEmpty(request.getOnlyZip())) {
            //单个方法设置了压缩
            if (request.getOnlyZip().equals(G_ZIP)) {
                return stub.withDeadlineAfter(request.getTimeoutMil(), TimeUnit.MILLISECONDS)
                        .withCompression(G_ZIP);
            }
        } else {
            //如果单个方法没设置过，全局方法优先
            if (useZip) {
                return stub.withDeadlineAfter(request.getTimeoutMil(), TimeUnit.MILLISECONDS)
                        .withCompression(G_ZIP);
            }
        }
        return stub.withDeadlineAfter(request.getTimeoutMil(), TimeUnit.MILLISECONDS);
    }

}
