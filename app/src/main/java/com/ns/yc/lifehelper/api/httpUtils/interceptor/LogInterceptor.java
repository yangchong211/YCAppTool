package com.ns.yc.lifehelper.api.httpUtils.interceptor;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by yc on 2017/4/26
 * 日志拦截器，打印请求和响应日志
 * 测试时打开，上线到市场则关闭
 */

public class LogInterceptor implements Interceptor {

    public static final String TAG = "yc";
    private String tag;
    private boolean showResponse;

    public LogInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LogInterceptor(String tag) {
        this(tag, false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    /**打印响应日志*/
    private Response logForResponse(Response response) {
        try {
            //===>response log
            Log.e(tag, "====响应====response'log响应=======");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(tag, "1111url : " + clone.request().url());
            Log.e(tag, "1111code : " + clone.code());
            Log.e(tag, "1111protocol : " + clone.protocol());
            Log.e(tag, "1111code : " + response.body().toString());
            Log.e(tag, "1111cookie : " + response.header("Set-Cookie"));
            if (!TextUtils.isEmpty(clone.message())){
                Log.e(tag, "1111message : " + clone.message());
            }
            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        Log.e(tag, "1111responseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            Log.e(tag, "1111responseBody's content : " + resp);
                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            Log.e(tag, "1111responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }
            Log.e(tag, "========response'log=======end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**打印请求日志*/
    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            Log.e(tag, "2222====请求====request'log======查看请求信息及返回信息，如链接地址、头信息、参数信息等=");
            Log.e(tag, "2222method : " + request.method());
            Log.e(tag, "2222url : " + url);
            Log.e(tag, "2222cookie : " + request.header("Cookie"));
            if (headers != null && headers.size() > 0) {
                Log.e(tag, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    Log.e(tag, "2222requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        Log.e(tag, "2222requestBody's content : " + bodyToString(request));
                    } else {
                        Log.e(tag, "2222requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            Log.e(tag, "========request'log=======end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml"))
                return true;
        }
        return false;
    }

    /**将内容转化为字符串*/
    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }

}
