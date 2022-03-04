package com.yc.webviewlib.cache;

import android.net.Uri;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;

import java.util.Map;

public class WebRequestAdapter implements WebResourceRequest {

    private WebResourceRequest mWebResourceRequest;

    private WebRequestAdapter(WebResourceRequest x5Request){
        mWebResourceRequest = x5Request;
    }

    public static WebRequestAdapter adapter(WebResourceRequest x5Request){
        return new WebRequestAdapter(x5Request);
    }

    @Override
    public Uri getUrl() {
        return mWebResourceRequest.getUrl();
    }

    @Override
    public boolean isForMainFrame() {
        return mWebResourceRequest.isForMainFrame();
    }

    @Override
    public boolean isRedirect() {
        return mWebResourceRequest.isRedirect();
    }

    @Override
    public boolean hasGesture() {
        return mWebResourceRequest.hasGesture();
    }

    @Override
    public String getMethod() {
        return mWebResourceRequest.getMethod();
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return mWebResourceRequest.getRequestHeaders();
    }
}

