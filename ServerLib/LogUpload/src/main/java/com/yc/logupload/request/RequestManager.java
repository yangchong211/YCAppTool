package com.yc.logupload.request;

import com.yc.logupload.config.UploadInitHelper;
import com.yc.logupload.inter.OnUploadListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestManager {

    private static final OkHttpClient sHttpClient;
    private static volatile RequestManager sInstance;

    static {
        sHttpClient = new OkHttpClient.Builder()
                .connectTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .build();
    }

    private RequestManager() {

    }

    public static RequestManager getInstance() {
        if (sInstance == null) {
            synchronized (RequestManager.class) {
                if (sInstance == null) {
                    sInstance = new RequestManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 上传文件到服务器
     * @param file                  file文件
     * @param onUploadListener      上传回调
     */
    public void uploadFile(File file, final OnUploadListener onUploadListener) {
        String serverHost = UploadInitHelper.getConfig().getServerUrl();
        if (serverHost!=null){
            if (!serverHost.startsWith("http") || !serverHost.startsWith("https")) {
                onUploadListener.onError("url must be contain http or https");
                return;
            }
            postUploadFile(serverHost, file.getPath(), file.getName(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    onUploadListener.onError("post net error :" + e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    onUploadListener.onSuccess();
                }
            });
        }
    }


    /**
     * 上传文件，post请求
     * @param url               请求地址
     * @param pathName          文件路径名称
     * @param fileName          文件名
     * @param callback          callback
     */
    public static void postUploadFile(String url, String pathName,
                                      String fileName, Callback callback) {
        //判断文件类型
        MediaType mediaType = MediaType.parse(judgeType(pathName));
        //创建文件参数
        MultipartBody.Builder builder ;
        if (mediaType != null) {
            RequestBody requestBody = RequestBody.create(mediaType, new File(pathName));
            builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(mediaType.type(), fileName, requestBody);
            //发出请求参数
            Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
            Call call = sHttpClient.newCall(request);
            call.enqueue(callback);
        }
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
