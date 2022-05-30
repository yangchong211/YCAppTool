package com.yc.easy.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.yc.easy.demo.http.api.SearchAuthorApi;
import com.yc.easy.demo.http.api.SearchBlogsApi;
import com.yc.easy.demo.http.api.UpdateImageApi;
import com.yc.easy.demo.http.model.HttpData;
import com.yc.easy.demo.http.model.RequestHandler;
import com.yc.easy.demo.http.server.ReleaseServer;
import com.yc.http.EasyConfig;
import com.yc.http.EasyHttp;
import com.yc.http.EasyUtils;
import com.yc.http.config.IRequestInterceptor;
import com.yc.http.config.IRequestServer;
import com.yc.http.listener.OnDownloadListener;
import com.yc.http.listener.OnHttpListener;
import com.yc.http.listener.OnUpdateListener;
import com.yc.http.model.HttpHeaders;
import com.yc.http.model.HttpMethod;
import com.yc.http.model.HttpParams;
import com.yc.http.model.ResponseClass;
import com.yc.http.request.HttpRequest;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppToolUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class NetMainActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressBar mProgressBar;
    private TextView mResponseTextView;

    static {
        // 网络请求框架初始化
        IRequestServer server = new ReleaseServer();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                //.setLogEnabled(BuildConfig.DEBUG)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler(AppToolUtils.getApp()))
                // 设置请求参数拦截器
                .setInterceptor(new IRequestInterceptor() {
                    @Override
                    public void interceptArguments(@NonNull HttpRequest<?> httpRequest,
                                                   @NonNull HttpParams params,
                                                   @NonNull HttpHeaders headers) {
                        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    }
                })
                // 设置请求重试次数
                .setRetryCount(1)
                // 设置请求重试时间
                .setRetryTime(2000)
                // 添加全局请求参数
                .addParam("token", "6666666")
                // 添加全局请求头
                //.addHeader("date", "20191030")
                .into();
    }

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, NetMainActivity.class);
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_main);

        mProgressBar = findViewById(R.id.pb_main_progress);
        mResponseTextView = findViewById(R.id.response_textView);

        findViewById(R.id.btn_main_get).setOnClickListener(this);
        findViewById(R.id.btn_main_post).setOnClickListener(this);
        findViewById(R.id.btn_main_exec).setOnClickListener(this);
        findViewById(R.id.btn_main_update).setOnClickListener(this);
        findViewById(R.id.btn_main_download).setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_main_get) {

            EasyHttp.get(this)
                    .api(new SearchAuthorApi()
                            .setId(190000))
                    .request(new OnHttpListener<HttpData<List<SearchAuthorApi.Bean>>>() {
                        @Override
                        public void onSucceed(HttpData<List<SearchAuthorApi.Bean>> result) {
                            ToastUtils.showRoundRectToast("get 请求成功，请看日志");
                            Gson gson = new Gson();
                            String s = gson.toJson(result);
                            mResponseTextView.setText(s);
                        }

                        @Override
                        public void onFail(Exception e) {
                            mResponseTextView.setText(e.getMessage());
                        }
                    });

        } else if (viewId == R.id.btn_main_post) {

            EasyHttp.post(this)
                    .api(new SearchBlogsApi()
                            .setKeyword("android"))
                    .request(new OnHttpListener<HttpData<SearchBlogsApi.Bean>>() {
                        @Override
                        public void onSucceed(HttpData<SearchBlogsApi.Bean> result) {
                            ToastUtils.showRoundRectToast("post 请求成功，请看日志");
                            Gson gson = new Gson();
                            String s = gson.toJson(result);
                            mResponseTextView.setText(s);
                        }

                        @Override
                        public void onFail(Exception e) {
                            mResponseTextView.setText(e.getMessage());
                        }
                    });

        } else if (viewId == R.id.btn_main_exec) {

            // 在主线程中不能做耗时操作
            new Thread(() -> {
                try {
                    HttpData<SearchBlogsApi.Bean> data = EasyHttp.post(NetMainActivity.this)
                            .api(new SearchBlogsApi()
                                    .setKeyword("搬砖不再有"))
                            .execute(new ResponseClass<HttpData<SearchBlogsApi.Bean>>() {});
                    ToastUtils.showRoundRectToast("同步请求成功，请看日志");
                } catch (Exception e) {
                    ToastUtils.showRoundRectToast(e.getMessage());
                }
            }).start();

        } else if (viewId == R.id.btn_main_update) {

            if (mProgressBar.getVisibility() == View.VISIBLE) {
                ToastUtils.showRoundRectToast("当前正在上传或者下载，请等待完成之后再进行操作");
                return;
            }

            // 如果是放到外部存储目录下则需要适配分区存储
//            String fileName = "EasyHttp.png";
//            File file;
//            Uri outputUri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // 适配 Android 10 分区存储特性
//                ContentValues values = new ContentValues();
//                // 设置显示的文件名
//                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
//                // 生成一个新的 uri 路径
//                outputUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                file = new FileContentResolver(getContentResolver(), outputUri, fileName);
//            } else {
//                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
//            }

            // 如果是放到外部存储的应用专属目录则不需要适配分区存储特性
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "EasyHttp.png");

            if (!file.exists()) {
                // 生成图片到本地
                try {
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_material);
                    OutputStream outputStream = EasyUtils.openFileOutputStream(file);
                    if (((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                        outputStream.flush();
                    }
                    // 通知系统多媒体扫描该文件，否则会导致拍摄出来的图片或者视频没有及时显示到相册中，而需要通过重启手机才能看到
                    MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, null, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            EasyHttp.post(this)
                    .api(new UpdateImageApi(file))
                    .request(new OnUpdateListener<Void>() {

                        @Override
                        public void onStart(Call call) {
                            mProgressBar.setProgress(0);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onProgress(int progress) {
                            mProgressBar.setProgress(progress);
                        }

                        @Override
                        public void onSucceed(Void result) {
                            ToastUtils.showRoundRectToast("上传成功");
                        }

                        @Override
                        public void onFail(Exception e) {
                            ToastUtils.showRoundRectToast("上传失败");
                        }

                        @Override
                        public void onEnd(Call call) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });

        } else if (viewId == R.id.btn_main_download) {

            if (mProgressBar.getVisibility() == View.VISIBLE) {
                ToastUtils.showRoundRectToast("当前正在上传或者下载，请等待完成之后再进行操作");
                return;
            }

            // 如果是放到外部存储目录下则需要适配分区存储
//            String fileName = "微信 8.0.15.apk";
//
//            File file;
//            Uri outputUri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // 适配 Android 10 分区存储特性
//                ContentValues values = new ContentValues();
//                // 设置显示的文件名
//                values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
//                // 生成一个新的 uri 路径
//                outputUri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
//                file = new FileContentResolver(getContentResolver(), outputUri, fileName);
//            } else {
//                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
//            }

            // 如果是放到外部存储的应用专属目录则不需要适配分区存储特性
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "微信 8.0.15.apk");

            EasyHttp.download(this)
                    .method(HttpMethod.GET)
                    .file(file)
                    //.url("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk")
                    .url("https://dldir1.qq.com/weixin/android/weixin8015android2020_arm64.apk")
                    .md5("b05b25d4738ea31091dd9f80f4416469")
                    .listener(new OnDownloadListener() {

                        @Override
                        public void onStart(File file) {
                            mProgressBar.setProgress(0);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onProgress(File file, int progress) {
                            mProgressBar.setProgress(progress);
                        }

                        @Override
                        public void onComplete(File file) {
                            ToastUtils.showRoundRectToast("下载完成：" + file.getPath());
                        }

                        @Override
                        public void onError(File file, Exception e) {
                            ToastUtils.showRoundRectToast("下载出错：" + e.getMessage());
                        }

                        @Override
                        public void onEnd(File file) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    })
                    .start();
        }
    }

}