package com.yc.webviewlib.helper;

import android.os.Bundle;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.view.X5WebView;

import java.io.InputStream;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/10
 *     desc  : 测试：web中抓包工具，分析拦截，打印测试数据
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
@Deprecated
public class WebFidderHelper {


    public WebFidderHelper() {

    }

    /**
     * 此方法废弃于API21，调用于非UI线程，拦截资源请求并返回响应数据，返回null时WebView将继续加载资源
     * 注意：API21以下的AJAX请求会走onLoadResource，无法通过此方法拦截
     *
     * 其中 WebResourceRequest 封装了请求，WebResourceResponse 封装了响应
     * 封装了一个Web资源的响应信息，包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     * @param webView                           view
     * @param s                                 s
     */
    public WebResourceResponse shouldInterceptRequest(X5WebView webView, String s) {
        X5LogUtils.i("---shouldInterceptRequest-------->1---"+s);
        WebResourceResponse webResourceResponse = webView.getX5WebViewClient().shouldInterceptRequest(webView, s);
        X5LogUtils.i("---shouldInterceptRequest-----webResourceResponse--->1---"+webResourceResponse);
        /*int statusCode = webResourceResponse.getStatusCode();
        String encoding = webResourceResponse.getEncoding();
        InputStream data = webResourceResponse.getData();
        String mimeType = webResourceResponse.getMimeType();
        Map<String, String> responseHeaders = webResourceResponse.getResponseHeaders();
        X5LogUtils.i("---shouldInterceptRequest-------->1---"+statusCode + "----" +encoding
                + "----"+data + "-----"+ mimeType + "------"+responseHeaders);*/
        return webResourceResponse;
        //return super.shouldInterceptRequest(webView, s);
    }

    /**
     * 此方法添加于API21，调用于非UI线程，拦截资源请求并返回数据，返回null时WebView将继续加载资源
     *
     * 其中 WebResourceRequest 封装了请求，WebResourceResponse 封装了响应
     * 封装了一个Web资源的响应信息，包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     * @param webView                           view
     * @param webResourceRequest                request，添加于API21，封装了一个Web资源的请求信息，
     *                                          包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @return
     */
    public WebResourceResponse shouldInterceptRequest(X5WebView webView, WebResourceRequest webResourceRequest) {
        String method = webResourceRequest.getMethod();
        Map<String, String> headerFields = webResourceRequest.getRequestHeaders();
        String url = webResourceRequest.getUrl().toString();
        boolean b = webResourceRequest.hasGesture();
        boolean redirect = webResourceRequest.isRedirect();
        X5LogUtils.i("---shouldInterceptRequest----Request---->2---"+method + "----" +url
                + "----"+headerFields + "-----"+ b + "------"+redirect);
        WebResourceResponse webResourceResponse = webView.getX5WebViewClient()
                .shouldInterceptRequest(webView, webResourceRequest);
        int statusCode = webResourceResponse.getStatusCode();
        String encoding = webResourceResponse.getEncoding();
        InputStream data = webResourceResponse.getData();
        String mimeType = webResourceResponse.getMimeType();
        Map<String, String> responseHeaders = webResourceResponse.getResponseHeaders();
        X5LogUtils.i("---shouldInterceptRequest----Response---->2---"+statusCode + "----" +encoding
                + "----"+data + "-----"+ mimeType + "------"+responseHeaders);
        return webResourceResponse;
    }

    /**
     * 其中 WebResourceRequest 封装了请求，WebResourceResponse 封装了响应
     * 封装了一个Web资源的响应信息，包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     * @param webView                           view
     * @param webResourceRequest                request，添加于API21，封装了一个Web资源的请求信息，
     *                                          包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @param bundle                            bundle
     * @return
     */
    public WebResourceResponse shouldInterceptRequest(X5WebView webView,
                                                      WebResourceRequest webResourceRequest, Bundle bundle) {
        String method = webResourceRequest.getMethod();
        Map<String, String> headerFields = webResourceRequest.getRequestHeaders();
        String url = webResourceRequest.getUrl().toString();
        boolean b = webResourceRequest.hasGesture();
        boolean redirect = webResourceRequest.isRedirect();
        X5LogUtils.i("---shouldInterceptRequest-----Request--->3---"+method + "----" +url
                + "----"+headerFields + "-----"+ b + "------"+redirect);


        WebResourceResponse webResourceResponse = webView.getX5WebViewClient()
                .shouldInterceptRequest(webView, webResourceRequest, bundle);
        int statusCode = webResourceResponse.getStatusCode();
        String encoding = webResourceResponse.getEncoding();
        InputStream data = webResourceResponse.getData();
        String mimeType = webResourceResponse.getMimeType();
        Map<String, String> responseHeaders = webResourceResponse.getResponseHeaders();
        X5LogUtils.i("---shouldInterceptRequest----Response---->3---"+statusCode + "----" +encoding
                + "----"+data + "-----"+ mimeType + "------"+responseHeaders);

        return webResourceResponse;
        //return super.shouldInterceptRequest(webView, webResourceRequest, bundle);
    }


}
