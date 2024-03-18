package com.yc.appserver.http.http.model;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonSyntaxException;
import com.yc.appserver.R;
import com.yc.appserver.http.http.exception.ResultException;
import com.yc.appserver.http.http.exception.TokenException;
import com.yc.gson.factory.GsonFactory;
import com.yc.http.synthesis.EasyLog;
import com.yc.http.synthesis.config.IRequestHandler;
import com.yc.http.synthesis.exception.CancelException;
import com.yc.http.synthesis.exception.DataException;
import com.yc.http.synthesis.exception.NetHttpException;
import com.yc.http.synthesis.exception.NetworkException;
import com.yc.http.synthesis.exception.NullBodyException;
import com.yc.http.synthesis.exception.ResponseException;
import com.yc.http.synthesis.exception.ServerException;
import com.yc.http.synthesis.exception.TimeoutException;
import com.yc.http.synthesis.request.HttpRequest;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;


public final class RequestHandler implements IRequestHandler {

    private final Application mApplication;

    public RequestHandler(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public Object requestSucceed(@NonNull HttpRequest<?> httpRequest, @NonNull Response response,
                                 @NonNull Type type) throws Exception {
        if (Response.class.equals(type)) {
            return response;
        }

        if (!response.isSuccessful()) {
            // 返回响应异常
            throw new ResponseException(mApplication.getString(R.string.http_response_error) + ", responseCode: " +
                    response.code() + ", message: " + response.message(), response);
        }

        if (Headers.class.equals(type)) {
            return response.headers();
        }

        ResponseBody body = response.body();
        if (body == null) {
            throw new NullBodyException(mApplication.getString(R.string.http_response_null_body));
        }

        if (ResponseBody.class.equals(type)) {
            return body;
        }

        // 如果是用数组接收，判断一下是不是用 byte[] 类型进行接收的
        if(type instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            if (byte.class.equals(genericComponentType)) {
                return body.bytes();
            }
        }

        if (InputStream.class.equals(type)) {
            return body.byteStream();
        }

        if (Bitmap.class.equals(type)) {
            return BitmapFactory.decodeStream(body.byteStream());
        }

        String text;
        try {
            text = body.string();
        } catch (IOException e) {
            // 返回结果读取异常
            throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
        }

        // 打印这个 Json 或者文本
        EasyLog.printJson(httpRequest, text);

        if (String.class.equals(type)) {
            return text;
        }

        final Object result;

        try {
            result = GsonFactory.getSingletonGson().fromJson(text, type);
        } catch (JsonSyntaxException e) {
            // 返回结果读取异常
            throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
        }

        if (result instanceof HttpData) {
            HttpData<?> model = (HttpData<?>) result;

            if (model.isRequestSucceed()) {
                // 代表执行成功
                return result;
            }

            if (model.isTokenFailure()) {
                // 代表登录失效，需要重新登录
                throw new TokenException(mApplication.getString(R.string.http_token_error));
            }

            // 代表执行失败
            throw new ResultException(model.getMessage(), model);
        }
        return result;
    }

    @NonNull
    @Override
    public Exception requestFail(@NonNull HttpRequest<?> httpRequest, @NonNull Exception e) {
        if (e instanceof NetHttpException) {
            if (e instanceof TokenException) {
                // 登录信息失效，跳转到登录页

            }
            return e;
        }

        if (e instanceof SocketTimeoutException) {
            return new TimeoutException(mApplication.getString(R.string.http_server_out_time), e);
        }

        if (e instanceof UnknownHostException) {
            NetworkInfo info = ((ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            // 判断网络是否连接
            if (info != null && info.isConnected()) {
                // 有连接就是服务器的问题
                return new ServerException(mApplication.getString(R.string.http_server_error), e);
            }
            // 没有连接就是网络异常
            return new NetworkException(mApplication.getString(R.string.http_network_error), e);
        }

        if (e instanceof IOException) {
            return new CancelException(mApplication.getString(R.string.http_request_cancel), e);
        }

        return new NetworkException(e.getMessage(), e);
    }

    @Nullable
    @Override
    public Object readCache(@NonNull @NotNull HttpRequest<?> httpRequest, @NonNull @NotNull Type type, long cacheTime) {
        return null;
    }

    @Override
    public boolean writeCache(@NonNull @NotNull HttpRequest<?> httpRequest, @NonNull @NotNull Response response, @NonNull @NotNull Object result) {
        return false;
    }

    @Override
    public void clearCache() {

    }
}