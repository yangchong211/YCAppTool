package com.yc.webviewlib.cache;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/17
 *     desc  : 拦截资源的接口
 *     revise:
 * </pre>
 */
public interface WebViewRequestClient {

    WebResourceResponse interceptRequest(WebResourceRequest request);
    WebResourceResponse interceptRequest(String url);
    File getCachePath();

    void clearCache();
    void enableForce(boolean force);
    InputStream getCacheFile(String url);
    void initAssetsData();
    void loadUrl(WebView webView, String url);
    void loadUrl(String url, String userAgent);
    void loadUrl(String url, Map<String, String> additionalHttpHeaders, String userAgent);
    void loadUrl(WebView webView, String url, Map<String, String> additionalHttpHeaders);

}
