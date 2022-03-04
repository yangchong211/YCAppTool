package com.yc.webviewlib.cache;

import android.os.Build;
import androidx.annotation.RequiresApi;

import com.tencent.smtt.export.external.interfaces.WebResourceResponse;

import java.io.InputStream;
import java.util.Map;

public class WebResponseAdapter extends WebResourceResponse {

    private WebResourceResponse mWebResourceResponse;

    private WebResponseAdapter(WebResourceResponse webResourceResponse){
        mWebResourceResponse = webResourceResponse;
    }

    public static WebResponseAdapter adapter(WebResourceResponse webResourceResponse){
        if (webResourceResponse == null){
            return null;
        }
        return new WebResponseAdapter(webResourceResponse);

    }

    @Override
    public String getMimeType() {
        return mWebResourceResponse.getMimeType();
    }

    @Override
    public InputStream getData() {
        return mWebResourceResponse.getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int getStatusCode() {
        return mWebResourceResponse.getStatusCode();
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Map<String, String> getResponseHeaders() {
        return mWebResourceResponse.getResponseHeaders();
    }

    @Override
    public String getEncoding() {
        return mWebResourceResponse.getEncoding();
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public String getReasonPhrase() {
        return mWebResourceResponse.getReasonPhrase();
    }
}


