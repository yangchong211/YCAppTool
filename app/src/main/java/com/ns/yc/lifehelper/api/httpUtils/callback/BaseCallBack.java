package com.ns.yc.lifehelper.api.httpUtils.callback;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

public abstract class BaseCallBack<T> implements Callback<T> {


    /**
     * 为接收到的HTTP响应调用。
     * 注意：HTTP响应仍然可能表示应用程序级的故障，例如404或500。
     * 调用{@link Response#isSuccessful()}以确定响应是否表示成功。
     * @param call              call
     * @param response          response
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        //分发成功请求：1.200请求成功；2.其他情况
        dispatchResponse(call, response);
    }

    /**
     * 当网络异常发生与服务器对话时或当意外事件发生时调用。
     * 出现异常，创建请求或处理响应。
     * @param call              call
     * @param t                 t
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }

    /**
     * 请求成功
     * @param call              call
     * @param response          response
     */
    private void dispatchResponse(Call<T> call, Response<T> response) {
        //code >= 200 && code < 300
        boolean successful = response.isSuccessful();
        if (successful && response.code() == 200) {
            dispatchHttpSuccess(call, response);
        } else {
            HttpException httpException = new HttpException(response);
            onFailure(call, httpException);
        }
    }


    abstract void dispatchHttpSuccess(Call<T> call, Response<T> response);

}
