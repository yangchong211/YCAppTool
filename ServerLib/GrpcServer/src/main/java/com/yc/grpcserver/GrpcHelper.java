package com.yc.grpcserver;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yc.toolutils.AppLogUtils;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.grpc.stub.AbstractStub;

public final class GrpcHelper {

    /**
     * 存根修饰
     *
     * @param stub    存根
     * @param request 请求
     * @return 新的存根
     */
    @SuppressLint("CheckResult")
    public static <T extends AbstractStub> T createStub(T stub, BaseRequest request) {
        //开启压缩，如果单个方法设置过，单个方法优先
        if (!TextUtils.isEmpty(request.getOnlyZip())) {
            //单个方法设置了压缩
            if (request.getOnlyZip().equals(BaseRequest.G_ZIP)) {
                long timeoutMil = request.getTimeoutMil();
                AbstractStub abstractStub = stub
                        .withDeadlineAfter(timeoutMil, TimeUnit.MILLISECONDS)
                        .withCompression(BaseRequest.G_ZIP);
                return (T) abstractStub;
            }
        }
        long timeoutMil = request.getTimeoutMil();
        AbstractStub abstractStub = stub.withDeadlineAfter(timeoutMil, TimeUnit.MILLISECONDS);
        return (T) abstractStub;
    }

    /**
     * 响应对象转换
     *
     * @param response 响应体
     * @param type     类型
     * @param <T>      类型
     * @return 返回对象
     */
    public static <T> T translateResponse(Object response, Type type) {
        //创建gson对象
        Gson gson = new Gson();
        //先将GRPC对象转为Json字符串
        String jsonString = gson.toJson(response);
        //将Json字符串转为Bean通用对象,回调出去
        T t = toBean(jsonString, type);
        AppLogUtils.d(t.getClass().getSimpleName() + "-转换前的响应体" + jsonString);
        return t;
    }

    /**
     * 将字符串转为对象,如果对象是String类型,则会原路返回
     *
     * @param result 源字符串
     * @param type   类型
     * @param <T>    类型
     * @return 转换后的对象
     */
    private static <T> T toBean(String result, Type type) {
        if (type.toString().contains(String.class.getSimpleName())) {
            return (T) result;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(result, type);
        }
    }

    /**
     * 处理响应体信息
     *
     * @param baseResponse 基类响应
     * @param netCallback  回调
     */
    public static <T> BaseResponse handleResponse(BaseResponse baseResponse, GrpcCallback<T> netCallback) {
        try {
            if (baseResponse != null) {
                if (baseResponse.getCode() == 0) {
                    netCallback.onSuccess((T) baseResponse);
                    AppLogUtils.d("Response:" + baseResponse.getClass().getName() + "is success");
                } else {
                    netCallback.onFailed(baseResponse.getCode(), baseResponse.getMessage(), null);
                    AppLogUtils.d("Response:" + baseResponse.getClass().getName() + "is failed errorInfo:"
                            + baseResponse);
                }
            } else {
                String errMsg = "通道为空或响应为空，请检查网络";
                netCallback.onFailed(100, errMsg, null);
                AppLogUtils.d("Response is failed errorInfo:" + errMsg);
            }
        } finally {
            netCallback.onFinish();
        }
        return baseResponse;
    }

}
