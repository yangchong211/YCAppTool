package com.yc.webviewlib.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.URLUtil;
import com.tencent.smtt.sdk.WebView;
import com.yc.networklib.net.AppNetworkUtils;
import com.yc.webviewlib.utils.X5LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Dns;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.CacheInterceptor;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/17
 *     desc  : 拦截资源的接口的实现类
 *     revise:
 * </pre>
 */
public class WebViewCacheWrapper implements WebViewRequestClient {

    private final File mCacheFile;
    private final long mCacheSize;
    private final long mConnectTimeout;
    private final long mReadTimeout;
    private final CacheExtensionConfig mCacheExtensionConfig;
    private final Context mContext;
    private WebCacheType mCacheType;
    /**
     * 资源文件
     */
    private final String mAssetsDir;
    /**
     * 是否是信任的host
     */
    private final boolean mTrustAllHostname;
    private final SSLSocketFactory mSSLSocketFactory;
    private final X509TrustManager mX509TrustManager;
    private final Dns mDns;
    private final InterResourceInterceptor mResourceInterceptor;
    private final boolean mIsSuffixMod;

    //==============
    private OkHttpClient mHttpClient = null;
    private String mOrigin = "";
    private String mReferer = "";
    private String mUserAgent = "";
    public static final String KEY_CACHE = "WebResourceInterceptor-Key-Cache";


    public WebViewCacheWrapper(Builder builder) {
        this.mCacheExtensionConfig = builder.mCacheExtensionConfig;
        this.mCacheFile = builder.mCacheFile;
        this.mCacheSize = builder.mCacheSize;
        this.mCacheType = builder.mCacheType;
        this.mConnectTimeout = builder.mConnectTimeout;
        this.mReadTimeout = builder.mReadTimeout;
        this.mContext = builder.mContext;
        this.mAssetsDir = builder.mAssetsDir;
        this.mX509TrustManager = builder.mX509TrustManager;
        this.mSSLSocketFactory = builder.mSSLSocketFactory;
        this.mTrustAllHostname = builder.mTrustAllHostname;
        this.mResourceInterceptor = builder.mResourceInterceptor;
        this.mIsSuffixMod = builder.mIsSuffixMod;
        this.mDns = builder.mDns;

        initHttpClient();
        if (isEnableAssets()) {
            initAssetsLoader();
        }
    }

    private boolean isEnableAssets() {
        return mAssetsDir != null;
    }

    private void initAssetsLoader() {
        WebAssetsLoader.getInstance().init(mContext)
                .setDir(mAssetsDir)
                .isAssetsSuffixMod(mIsSuffixMod);
    }

    /**
     * 创建okhttp，主要是用它进行缓存
     */
    private void initHttpClient() {
        //设置缓存的位置，还有缓存的大小，默认是100M
        final Cache cache = new Cache(mCacheFile, mCacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(mConnectTimeout, TimeUnit.SECONDS)
                .readTimeout(mReadTimeout, TimeUnit.SECONDS);
        if (mTrustAllHostname) {
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        if (mSSLSocketFactory != null && mX509TrustManager != null) {
            builder.sslSocketFactory(mSSLSocketFactory, mX509TrustManager);
        }
        if(mDns!=null){
            builder.dns(mDns);
        }
        mHttpClient = builder.build();
    }

    /**
     * 拦截处理的入口，可以直接从这里拓展
     * @param request                                   request请求
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse interceptRequest(WebResourceRequest request) {
        //获取请求链接
        String url = request.getUrl().toString();
        //获取请求链接header
        Map<String, String> requestHeaders = request.getRequestHeaders();
        return interceptRequest(url, requestHeaders);
    }

    private Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(mOrigin)) {
            headers.put("Origin", mOrigin);
        }
        if (!TextUtils.isEmpty(mReferer)) {
            //先前网页的地址，当前请求网页紧随其后,即来路
            headers.put("Referer", mReferer);
        }
        if (!TextUtils.isEmpty(mUserAgent)) {
            //User-Agent的内容包含发出请求的用户信息
            headers.put("User-Agent", mUserAgent);
        }
        return headers;
    }

    /**
     * 拦截处理的入口
     * @param url                                       url链接
     * @return
     */
    @Override
    public WebResourceResponse interceptRequest(String url) {
        Map<String, String> requestHeaders = buildHeaders();
        return interceptRequest(url, requestHeaders);
    }

    /**
     * 检查url，是否是http开发的
     * 如果不是以http开发，没有资源拦截，以及不是缓存的类型，或者是资源媒体，就直接返回false，表示这些不可缓存
     * @param url                                       url链接
     * @return
     */
    private boolean checkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        //okhttp only deal with http[s]
        if (!url.startsWith("http")) {
            return false;
        }

        if (mResourceInterceptor != null && !mResourceInterceptor.interceptor(url)) {
            return false;
        }

        String extension = MimeTypeMapUtils.getFileExtensionFromUrl(url);
        X5LogUtils.d("WebViewCacheWrapper---interceptRequest检查url--------checkUrl---" +extension);
        if (TextUtils.isEmpty(extension)) {
            return false;
        }
        if (mCacheExtensionConfig.isMedia(extension)) {
            return false;
        }
        if (!mCacheExtensionConfig.canCache(extension)) {
            return false;
        }
        return true;
    }

    @Override
    public void loadUrl(WebView webView, String url) {
        if (!isValidUrl(url)) {
            return;
        }
        webView.loadUrl(url);
        mReferer = webView.getUrl();
        mOrigin = getOriginUrl(mReferer);
        mUserAgent = webView.getSettings().getUserAgentString();
    }

    @Override
    public void loadUrl(String url, String userAgent) {
        if (!isValidUrl(url)) {
            return;
        }
        mReferer = url;
        mOrigin = getOriginUrl(mReferer);
        mUserAgent = userAgent;
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders, String userAgent) {
        if (!isValidUrl(url)) {
            return;
        }
        mReferer = url;
        mOrigin = getOriginUrl(mReferer);
        mUserAgent = userAgent;
    }

    @Override
    public void loadUrl(WebView webView, String url, Map<String, String> additionalHttpHeaders) {
        if (!isValidUrl(url)) {
            return;
        }
        webView.loadUrl(url, additionalHttpHeaders);
        mReferer = webView.getUrl();
        mOrigin = getOriginUrl(mReferer);
        mUserAgent = webView.getSettings().getUserAgentString();
    }

    /**
     * 清除缓存，删除缓存文件，然后清除内存缓存
     */
    @Override
    public void clearCache() {
        WebFileUtils.deleteDirs(mCacheFile.getAbsolutePath(), false);
        WebAssetsLoader.getInstance().clear();
    }

    @Override
    public void enableForce(boolean force) {
        if (force) {
            mCacheType = WebCacheType.FORCE;
        } else {
            mCacheType = WebCacheType.NORMAL;
        }
    }

    @Override
    public InputStream getCacheFile(String url) {
        return WebHttpFileUtils.getCacheFile(mCacheFile, url);
    }

    @Override
    public void initAssetsData() {
        WebAssetsLoader.getInstance().initData();
    }


    @Override
    public File getCachePath() {
        return mCacheFile;
    }

    /**
     * 添加header头
     * @param reqBuilder                            builder
     * @param headers                               headers
     */
    public void addHeader(Request.Builder reqBuilder, Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            reqBuilder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 拦截处理的核心逻辑
     * @param url                                       链接url
     * @param headers                                   header头信息
     * @return
     */
    private WebResourceResponse interceptRequest(String url, Map<String, String> headers) {
        //标准缓存，不用该方案
        if (mCacheType == WebCacheType.NORMAL) {
            return null;
        }
        //第一步：判断拦截资源的条件。比如那些需要拦截，那些不需要拦截
        //第一步：判断拦截资源的条件。比如那些需要拦截，那些不需要拦截
        if (!checkUrl(url)) {
            return null;
        }

        //先从缓存中获取数据，也就是本地缓存文件中获取数据
        if (isEnableAssets()) {
            //这种方式读数据效率不高
            //阻塞I/O通信模式：调用InputStream.read()方法时是阻塞的，它会一直等到数据到来时才返回
            //NIO通信模式：是一种非阻塞I/O，在Java NIO的服务端由一个专门的线程来处理所有I/O事件，并负责分发；线程之间通讯通过wait和notify等方式
            InputStream inputStream = WebAssetsLoader.getInstance().getResByUrl(url);
            if (inputStream != null) {
                X5LogUtils.d("WebViewCacheWrapper---interceptRequest1--" +String.format("from assets: %s", url));
                String mimeType = MimeTypeMapUtils.getMimeTypeFromUrl(url);
                WebResourceResponse webResourceResponse = new WebResourceResponse(mimeType, "", inputStream);
                return webResourceResponse;
            }
        }

        //创建OkHttp的Request请求，将资源网络请求交给okHttp来处理，并且用它自带的缓存功能
        //高效缓存
        //1.三级缓存，网络缓存(http)，磁盘缓存(file)，内存缓存(Lru)
        //2.使用okio流，数据进行了分块处理(Segment)，提供io流超时处理，对数据的读写都进行了封装和交给Buffer管理
        try {
            Request.Builder reqBuilder = new Request.Builder().url(url);
            String extension = MimeTypeMapUtils.getFileExtensionFromUrl(url);
            if (mCacheExtensionConfig.isHtml(extension)) {
                headers.put(KEY_CACHE, mCacheType.ordinal() + "");
            }
            addHeader(reqBuilder, headers);
            if (!AppNetworkUtils.isConnected()) {
                //设置缓存策略
                //仅使用缓存的缓存控制请求指令，即使缓存的响应已经过时。
                //如果响应在缓存中不可用或需要服务器验证，调用将失败，并带有{@code 504 unsatisrequest}。
                reqBuilder.cacheControl(CacheControl.FORCE_CACHE);
            }
            Request request = reqBuilder.build();
            //直接交给OkHttpClient去做网络请求
            Response response = mHttpClient.newCall(request).execute();
            //返回从缓存接收到的原始响应。如果此响应没有使用缓存，则为null。
            //对于有条件的get请求，缓存响应和网络响应可能都是非空的。不应该读取返回的响应的正文。
            Response cacheRes = response.cacheResponse();
            if (cacheRes != null) {
                //应用缓存
                X5LogUtils.d("WebViewCacheWrapper---interceptRequest2--" +String.format("from cache: %s", url));
            } else {
                //没有缓存
                X5LogUtils.d("WebViewCacheWrapper---interceptRequest3--" +String.format("from server: %s", url));
            }
            //获取资源响应的MIME类型，例如text/html
            String mimeType = MimeTypeMapUtils.getMimeTypeFromUrl(url);
            WebResourceResponse webResourceResponse = null;
            if (response.body() != null) {
                webResourceResponse = new WebResourceResponse(mimeType, "", response.body().byteStream());
            }
            //没有联网，或者504，直接返回为空
            if (response.code() == 504 && !AppNetworkUtils.isConnected()){
                return null;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String message = response.message();
                if (TextUtils.isEmpty(message)) {
                    message = "OK";
                }
                try {
                    if (webResourceResponse != null) {
                        //设置状态代码和原因短语
                        webResourceResponse.setStatusCodeAndReasonPhrase(response.code(), message);
                    }
                } catch (Exception e) {
                    return null;
                }
                if (webResourceResponse != null) {
                    //设置响应头
                    webResourceResponse.setResponseHeaders(multimapToSingle(response.headers().toMultimap()));
                }
            }
            return webResourceResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder {

        private File mCacheFile;
        private long mCacheSize = 100 * 1024 * 1024;
        private long mConnectTimeout = 20;
        private long mReadTimeout = 20;
        private CacheExtensionConfig mCacheExtensionConfig;
        private Context mContext;
        private WebCacheType mCacheType = WebCacheType.FORCE;


        private boolean mTrustAllHostname = false;
        private SSLSocketFactory mSSLSocketFactory = null;
        private X509TrustManager mX509TrustManager = null;
        private InterResourceInterceptor mResourceInterceptor;

        private String mAssetsDir = null;
        private boolean mIsSuffixMod = false;
        private Dns mDns=null;
        public Builder(Context context) {
            mContext = context;
            mCacheFile = new File(context.getCacheDir().toString(), "YcWebViewCache");
            mCacheExtensionConfig = new CacheExtensionConfig();
        }

        public void setResourceInterceptor(InterResourceInterceptor resourceInterceptor) {
            mResourceInterceptor = resourceInterceptor;
        }

        public Builder setTrustAllHostname() {
            mTrustAllHostname = true;
            return this;
        }

        public Builder setSSLSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
            if (sslSocketFactory != null && trustManager != null) {
                mSSLSocketFactory = sslSocketFactory;
                mX509TrustManager = trustManager;
            }
            return this;
        }

        public Builder setCachePath(File file) {
            if (file != null) {
                mCacheFile = file;
            }
            return this;
        }

        public Builder setCacheSize(long cacheSize) {
            if (cacheSize > 1024) {
                mCacheSize = cacheSize;
            }
            return this;
        }

        public Builder setReadTimeoutSecond(long time) {
            if (time >= 0) {
                mReadTimeout = time;
            }
            return this;
        }

        public Builder setConnectTimeoutSecond(long time) {
            if (time >= 0) {
                mConnectTimeout = time;
            }

            return this;
        }

        public Builder setCacheExtensionConfig(CacheExtensionConfig config) {
            if (config != null) {
                mCacheExtensionConfig = config;
            }
            return this;
        }

        public Builder setCacheType(WebCacheType cacheType) {
            mCacheType = cacheType;
            return this;
        }

        public Builder isAssetsSuffixMod(boolean suffixMod) {
            this.mIsSuffixMod = suffixMod;
            return this;
        }

        public Builder setAssetsDir(String dir) {
            if (dir != null) {
                mAssetsDir = dir;
            }
            return this;
        }

        public void setDns(Dns dns) {
            this.mDns = dns;
        }

        public WebViewRequestClient build() {
            return new WebViewCacheWrapper(this);
        }

    }

    /**
     * 判断url是否有效
     * @param url                                   url地址
     * @return
     */
    boolean isValidUrl(String url) {
        return URLUtil.isValidUrl(url);
    }

    /**
     * 获取起源url地址
     * @param referer                               当前地址url
     * @return
     */
    private String getOriginUrl(String referer) {
        String ou = referer;
        if (TextUtils.isEmpty(ou)) {
            return "";
        }
        try {
            URL url = new URL(ou);
            int port = url.getPort();
            ou = url.getProtocol() + "://" + url.getHost() + (port == -1 ? "" : ":" + port);
            X5LogUtils.i("WebViewCacheWrapper---getOriginUrl--" +ou);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ou;
    }

    private Map<String,String> multimapToSingle(Map<String, List<String>> maps){
        StringBuilder sb = new StringBuilder();
        Map<String,String> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry: maps.entrySet()) {
            List<String> values = entry.getValue();
            sb.delete(0,sb.length());
            if (values!=null&&values.size()>0){
                for (String v:values) {
                    sb.append(v);
                    sb.append(";");
                }
            }
            if (sb.length()>0){
                sb.deleteCharAt(sb.length()-1);
            }
            map.put(entry.getKey(),sb.toString());
        }
        return map;
    }

}
