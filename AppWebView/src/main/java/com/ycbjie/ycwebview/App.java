package com.ycbjie.ycwebview;

import android.app.Application;


import com.yc.webviewlib.cache.WebCacheType;
import com.yc.webviewlib.cache.WebViewCacheDelegate;
import com.yc.webviewlib.cache.WebViewCacheWrapper;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.utils.X5WebUtils;

import java.io.File;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        X5WebUtils.init(this);
        X5LogUtils.setIsLog(true);
//        X5WebUtils.initCache(this);
//        WebViewCacheDelegate.getInstance().init(new WebViewCacheWrapper.Builder(this));


        //1.创建委托对象
        WebViewCacheDelegate webViewCacheDelegate = WebViewCacheDelegate.getInstance();
        //2.创建调用处理器对象，实现类
        WebViewCacheWrapper.Builder builder = new WebViewCacheWrapper.Builder(this);
        //设置缓存路径，默认getCacheDir，名称CacheWebViewCache
        builder.setCachePath(new File(this.getCacheDir().toString(),"CacheWebView"))
                //设置缓存大小，默认100M
                .setCacheSize(1024*1024*100)
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
}
