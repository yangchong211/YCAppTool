package com.yc.http.easy;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.easyexecutor.DelegateTaskExecutor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求工具
 */
public final class RequestManager {

    private static final MediaType MEDIA_TYPE_FORM =
            MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType MEDIA_TYPE_MARKDOWN =
            MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_JSON =
            MediaType.parse("application/json; charset=utf-8");
    /**
     * 单利引用
     */
    private static volatile RequestManager mInstance;
    /**
     * okHttpClient 实例
     */
    private final OkHttpClient mOkHttpClient;
    /**
     * 配置属性
     */
    private RequestConfig requestConfig;

    /**
     * 初始化RequestManager
     */
    public RequestManager() {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(new LoggerInterceptor())//添加日志拦截器
                .build();
    }


    /**
     * 获取单例引用
     *
     * @return 引用
     */
    public static RequestManager getInstance() {
        RequestManager inst = mInstance;
        if (inst == null) {
            synchronized (RequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new RequestManager();
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    private String getBaseUrl() {
        if (requestConfig == null || TextUtils.isEmpty(requestConfig.getBaseUrl())) {
            throw new NullPointerException("请先初始化设置接口根地址");
        }
        return requestConfig.getBaseUrl();
    }

    /**
     * okHttp同步请求统一入口
     *
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     */
    public void requestExecute(String actionUrl, HttpMethod requestType, HashMap<String,
            String> paramsMap, ReqCallBack<String> callBack) {
        switch (requestType) {
            case GET:
                DelegateTaskExecutor.getInstance().executeOnCpu(() ->
                        requestGet(actionUrl, paramsMap, callBack));
                break;
            case POST_JSON:
                DelegateTaskExecutor.getInstance().executeOnCpu(() ->
                        requestPost(actionUrl, paramsMap, callBack));
                break;
            case POST_FORM:
                DelegateTaskExecutor.getInstance().executeOnCpu(() ->
                        requestPostWithForm(actionUrl, paramsMap, callBack));
                break;
            default:
                break;
        }
    }


    /**
     * okHttp异步请求统一入口
     *
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack    请求返回数据回调
     **/
    public void requestEnqueue(String actionUrl, HttpMethod requestType,
                               HashMap<String, String> paramsMap, ReqCallBack<String> callBack) {
        switch (requestType) {
            case GET:
                requestGetEnqueue(actionUrl, paramsMap, callBack);
                break;
            case POST_JSON:
                requestPostEnqueue(actionUrl, paramsMap, callBack);
                break;
            case POST_FORM:
                requestPostEnqueueWithForm(actionUrl, paramsMap, callBack);
                break;
            case POST_FORM2:
                requestPostEnqueueWithFormData(actionUrl, paramsMap, callBack);
                break;
            default:
                break;
        }
    }


    /**
     * okHttp get同步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  callback
     */
    private void requestGet(String actionUrl,
                            HashMap<String, String> paramsMap, ReqCallBack<String> callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            if (paramsMap != null) {
                int pos = 0;
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    //对参数进行URLEncoder
                    tempParams.append(String.format("%s=%s", key,
                            URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
            }
            //补全请求地址
            String requestUrl = String.format("%s/%s?%s", getBaseUrl(), actionUrl, tempParams);
            d("requestGet requestUrl " + requestUrl);
            //创建一个请求
            Request request = addHeaders().url(requestUrl).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            final Response response = call.execute();
            if (response.code() == 200) {
                ResponseBody body = response.body();
                if (body != null) {
                    String string = body.string();
                    successCallBack(string, callBack);
                } else {
                    failedCallBack(response.code(), response.message(), callBack);
                }
            } else {
                failedCallBack(response.code(), "服务器错误: " + response.message(), callBack);
            }
        } catch (Exception e) {
            e("requestGet Exception " + e);
        }
    }


    /**
     * okHttp post同步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  callback
     */
    private void requestPost(String actionUrl,
                             HashMap<String, String> paramsMap, ReqCallBack<String> callBack) {
        try {
            //处理参数
            StringBuilder tempParams = new StringBuilder();
            if (paramsMap != null) {
                int pos = 0;
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key,
                            URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
            }

            //补全请求地址
            String requestUrl = String.format("%s/%s", getBaseUrl(), actionUrl);
            d("requestPost requestUrl " + requestUrl);
            //生成参数
            String params = tempParams.toString();
            //创建一个请求实体对象 RequestBody
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, params);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(requestBody).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (response.code() == 200) {
                ResponseBody body = response.body();
                if (body != null) {
                    String string = body.string();
                    successCallBack(string, callBack);
                } else {
                    failedCallBack(response.code(), response.message(), callBack);
                }
            } else {
                failedCallBack(response.code(), "服务器错误: " + response.message(), callBack);
            }
        } catch (Exception e) {
            e("requestPost Exception " + e);
        }
    }

    /**
     * okHttp post同步请求表单提交
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  callback
     */
    private void requestPostWithForm(String actionUrl, HashMap<String, String> paramsMap,
                                     ReqCallBack<String> callBack) {
        try {
            //创建一个FormBody.Builder
            FormBody.Builder builder = new FormBody.Builder();
            if (paramsMap != null) {
                for (String key : paramsMap.keySet()) {
                    String s = paramsMap.get(key);
                    //追加表单信息
                    builder.add(key, s + "");
                }
            }
            //生成表单实体对象
            RequestBody formBody = builder.build();
            //补全请求地址
            String requestUrl = String.format("%s/%s", getBaseUrl(), actionUrl);
            d("requestPostWithForm requestUrl " + requestUrl);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            if (response.code() == 200) {
                ResponseBody body = response.body();
                if (body != null) {
                    String string = body.string();
                    successCallBack(string, callBack);
                } else {
                    failedCallBack(response.code(), response.message(), callBack);
                }
            } else {
                failedCallBack(response.code(), response.message(), callBack);
            }
        } catch (Exception e) {
            e("requestPostWithForm Exception " + e);
        }
    }


    /**
     * okHttp get异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     */
    private <T> void requestGetEnqueue(String actionUrl, HashMap<String, String> paramsMap,
                                       final ReqCallBack<T> callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            if (paramsMap != null) {
                int pos = 0;
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key,
                            URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
            }
            String requestUrl = String.format("%s/%s?%s", getBaseUrl(), actionUrl, tempParams);
            d("requestGetEnqueue requestUrl " + requestUrl);
            final Request request = addHeaders().url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    failedCallBack(-1, "访问失败:" + e.getMessage(), callBack);
                }

                @Override
                public void onResponse(@NonNull Call call,
                                       @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String string = body.string();
                            successCallBack((T) string, callBack);
                        } else {
                            failedCallBack(response.code(), response.message(), callBack);
                        }
                    } else {
                        failedCallBack(response.code(), "服务器错误: " + response.message(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            e("requestGetEnqueue Exception " + e);
        }
    }

    /**
     * okHttp post异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     */
    private <T> void requestPostEnqueue(String actionUrl, HashMap<String,
            String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            StringBuilder tempParams = new StringBuilder();
            if (paramsMap != null) {
                int pos = 0;
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key,
                            URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
            }
            String params = tempParams.toString();
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            String requestUrl = String.format("%s/%s", getBaseUrl(), actionUrl);
            d("requestPostEnqueue requestUrl " + requestUrl);
            final Request request = addHeaders().url(requestUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    failedCallBack(-1, "访问失败:" + e.getMessage(), callBack);
                }

                @Override
                public void onResponse(@NonNull Call call,
                                       @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String string = body.string();
                            successCallBack((T) string, callBack);
                        } else {
                            failedCallBack(response.code(), response.message(), callBack);
                        }
                    } else {
                        failedCallBack(response.code(), "服务器错误: " + response.message(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            e("requestPostEnqueue Exception " + e);
        }
    }


    /**
     * okHttp post异步请求表单提交
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     */
    private <T> void requestPostEnqueueWithForm(String actionUrl, HashMap<String,
            String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if (paramsMap != null) {
                for (String key : paramsMap.keySet()) {
                    String s = paramsMap.get(key);
                    builder.add(key, s + "");
                }
            }
            RequestBody formBody = builder.build();
            String requestUrl = String.format("%s/%s", getBaseUrl(), actionUrl);
            d("requestPostEnqueueWithForm requestUrl " + requestUrl);
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    failedCallBack(-1, "访问失败:" + e.getMessage(), callBack);
                }

                @Override
                public void onResponse(@NonNull Call call,
                                       @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String string = body.string();
                            successCallBack((T) string, callBack);
                        } else {
                            failedCallBack(response.code(), response.message(), callBack);
                        }
                    } else {
                        failedCallBack(response.code(), "服务器错误: " + response.message(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            e("requestPostEnqueueWithForm Exception " + e);
        }
    }

    /**
     * okHttp post异步请求表单提交
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     */
    private void requestPostEnqueueWithFormData(String actionUrl, HashMap<String,
            String> paramsMap, final ReqCallBack<String> callBack) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            // 设置传参为form-data格式
            builder.setType(MultipartBody.FORM);
            if (paramsMap != null) {
                for (String key : paramsMap.keySet()) {
                    String s = paramsMap.get(key);
                    builder.addFormDataPart(key, s + "");
                }
            }

            RequestBody formBody = builder.build();
            String requestUrl = String.format("%s/%s", getBaseUrl(), actionUrl);
            d("requestPostEnqueueWithForm requestUrl " + requestUrl);
            //开始请求
            final Request request = addHeaders()
                    .url(requestUrl)
                    .post(formBody)
                    .addHeader("Content-Type", "application/json")
                    .build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    failedCallBack(-1, "访问失败:" + e.getMessage(), callBack);
                }

                @Override
                public void onResponse(@NonNull Call call,
                                       @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String string = body.string();
                            successCallBack(string, callBack);
                        } else {
                            failedCallBack(response.code(), response.message(), callBack);
                        }
                    } else {
                        failedCallBack(response.code(), "服务器错误: " + response.message(), callBack);
                    }
                }
            });
        } catch (Exception e) {
            ExceptionUtils.handleException(e);
        }
    }



    /**
     * 统一为请求添加头信息
     *
     * @return 添加请求公共参数
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE);

        //添加外部设置的请求头
        if (requestConfig != null && requestConfig.getHeaders() != null
                && requestConfig.getHeaders().size() > 0) {
            HashMap<String, String> headers = requestConfig.getHeaders();
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result   响应数据
     * @param callBack 回调
     * @param <T>      泛型
     */
    private <T> void successCallBack(final T result, final ReqCallBack<T> callBack) {
        DelegateTaskExecutor.getInstance().postToMainThread(() -> {
            d("successCallBack response ----->" + result);
            if (callBack != null) {
                callBack.onReqSuccess(result);
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg 异常消息
     * @param callBack 回调
     * @param <T>      泛型
     */
    private <T> void failedCallBack(int code, final String errorMsg, final ReqCallBack<T> callBack) {
        DelegateTaskExecutor.getInstance().postToMainThread(() -> {
            d("failedCallBack response ----->" + code + " , " + errorMsg);
            if (callBack != null) {
                callBack.onReqFailed(code, errorMsg);
            }
        });
    }

    public static void d(String msg) {
        Log.d("PalmSdk |: Net ", msg);
    }

    public static void e(String msg) {
        Log.e("PalmSdk |: Net ", msg);
    }

}
