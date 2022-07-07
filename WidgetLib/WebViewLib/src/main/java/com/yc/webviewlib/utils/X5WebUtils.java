/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.webviewlib.utils;


import android.app.Application;
import android.content.Context;

import androidx.annotation.IntDef;

import com.tencent.smtt.sdk.QbSdk;
import com.yc.webviewlib.cache.WebCacheType;
import com.yc.webviewlib.cache.WebViewCacheDelegate;
import com.yc.webviewlib.cache.WebViewCacheWrapper;
import com.yc.webviewlib.tools.WebViewException;
import com.yc.webviewlib.view.X5WebView;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : WebView工具类
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public final class X5WebUtils {

    /**
     * 全局上下文
     */
    private static Application application;

    /**
     * 不能直接new，否则抛个异常
     */
    private X5WebUtils() {
        throw new WebViewException(1, "u can't instantiate me...");
    }

    /**
     * 初始化腾讯x5浏览器webView，建议在application初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (context instanceof Application) {
            application = (Application) context;
            //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                @Override
                public void onViewInitFinished(boolean arg0) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功
                    //否则表示x5内核加载失败，会自动切换到系统内核。
                    X5LogUtils.i("app" + " onViewInitFinished is " + arg0);
                }

                @Override
                public void onCoreInitFinished() {
                    X5LogUtils.i("app" + "onCoreInitFinished ");
                }
            };
            //x5内核初始化接口
            QbSdk.initX5Environment(context, cb);
            X5WebView.isLongClick = true;
            X5LogUtils.setIsLog(true);
        } else {
            throw new UnsupportedOperationException("context must be application...");
        }
    }

    /**
     * 初始化缓存
     *
     * @param application 上下文
     */
    public static void initCache(Application application, String path) {
        if (path == null || path.length() == 0) {
            path = "YcCacheWebView";
        }
        //1.创建委托对象
        WebViewCacheDelegate webViewCacheDelegate = WebViewCacheDelegate.getInstance();
        //2.创建调用处理器对象，实现类
        WebViewCacheWrapper.Builder builder = new WebViewCacheWrapper.Builder(application);
        //设置缓存路径，默认getCacheDir，名称CacheWebViewCache
        builder.setCachePath(new File(application.getCacheDir().toString(), path))
                //设置缓存大小，默认100M
                .setCacheSize(1024 * 1024 * 100)
                //设置本地路径
                //.setAssetsDir("yc")
                //设置http请求链接超时，默认20秒
                .setConnectTimeoutSecond(20)
                //设置http请求链接读取超时，默认20秒
                .setReadTimeoutSecond(20)
                //设置缓存为正常模式，默认模式为强制缓存静态资源
                .setCacheType(WebCacheType.FORCE);
        webViewCacheDelegate.init(builder);
    }

    /**
     * 获取全局上下文
     *
     * @return 上下文
     */
    public static Application getApplication() {
        return application;
    }

    /**
     * 注解限定符
     */
    @IntDef({ErrorMode.NO_NET, ErrorMode.STATE_404, ErrorMode.RECEIVED_ERROR, ErrorMode.SSL_ERROR,
            ErrorMode.TIME_OUT, ErrorMode.STATE_500, ErrorMode.ERROR_PROXY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorType {
    }

    /**
     * 异常状态模式
     * NO_NET                       没有网络
     * STATE_404                    404，网页无法打开
     * RECEIVED_ERROR               onReceivedError，请求网络出现error
     * SSL_ERROR                    在加载资源时通知主机应用程序发生SSL错误
     * TIME_OUT                     网络连接超时
     * STATE_500                    服务器异常
     * ERROR_PROXY                  代理异常
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorMode {
        int NO_NET = 1001;
        int STATE_404 = 1002;
        int RECEIVED_ERROR = 1003;
        int SSL_ERROR = 1004;
        int TIME_OUT = 1005;
        int STATE_500 = 1006;
        int ERROR_PROXY = 1007;
    }

}
