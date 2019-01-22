package com.ycbjie.library.utils.okHttpUtils;


import com.ycbjie.library.BuildConfig;
import com.ycbjie.library.http.InterceptorUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2018/1/9
 * 描    述：http简单请求工具类[没有封装]，只是作为分析源码基础，不建议使用
 * 修订历史：
 * ================================================
 */
public class OkHttpUtils {

    private static OkHttpClient client = null;

    private OkHttpUtils() {}

    /**
     * 恶汉式单例
     * @return                  返回OkHttpClient实例对象
     */
    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (OkHttpUtils.class) {
                if (client == null){
                    client = new OkHttpClient.Builder()
                            .addInterceptor(InterceptorUtils.getHttpLoggingInterceptor(BuildConfig.DEBUG))
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return client;
    }

    /**
     * 普通的get请求
     * @param url               请求地址，比如：http://api.tianapi.com/social/?key=APIKEY&num=10
     * @param callback          callback
     *
     *                          使用场景：比如投资界，新芽的新闻，数据等详情页面用get请求
     */
    public static void getWebRequest(String url , Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }


    /**
     * 普通的post请求
     * @param url               请求地址，比如：http://api.tianapi.com/social/
     * @param mapParams         请求参数，比如key=APIKEY；num=10；……
     * @param callback          callback
     *
     *                          使用场景：比如投资界，新芽list页面用post请求
     */
    public static void postWebRequest(String url, Map<String, Object> mapParams, Callback callback){
        StringBuilder sb = new StringBuilder();
        for (String key :mapParams.keySet()) {
            sb.append(key)
                    .append("=")
                    .append("")
                    .append(mapParams.get(key))
                    .append("&");
        }
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded"),sb.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }


    /**
     * 普通post的json请求
     * @param url               请求地址
     * @param json              请求参数，json
     * @param callback          callback
     *
     *                          使用场景，还没遇到过
     */
    public static void postJsonRequest(String url , String json , Callback callback){
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }


    /**
     * 上传文件，post请求
     * @param url               请求地址
     * @param pathName          文件路径名称
     * @param fileName          文件名
     * @param callback          callback
     */
    public static void postUploadFile(String url, String pathName, String fileName, Callback callback) {
        //判断文件类型
        MediaType mediaType = MediaType.parse(judgeType(pathName));
        //创建文件参数
        MultipartBody.Builder builder ;
        if (mediaType != null) {
            builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(mediaType.type(), fileName,
                                    RequestBody.create(mediaType, new File(pathName)));
            //发出请求参数
            Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
            Call call = getInstance().newCall(request);
            call.enqueue(callback);
        }
    }

    /**
     * 下载文件
     * @param url               请求地址
     * @param fileDir           父路径名字符串
     * @param fileName          子路径名的字符串
     */
    public static void downFile(String url, final String fileDir, final String fileName) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len ;
                FileOutputStream fos = null;
                try {
                    //noinspection ConstantConditions
                    is = response.body().byteStream();
                    File file = new File(fileDir, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null){
                        is.close();
                    }
                    if (fos != null){
                        fos.close();
                    }
                }
            }
        });

    }


    /**
     * 根据文件路径判断MediaType
     * @param path              文件路径
     * @return                  返回字符串类型的文件类型
     */
    private static String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


}
