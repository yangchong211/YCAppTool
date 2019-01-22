package com.ycbjie.library.web.js;

import android.content.Context;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;


public abstract class BaseAppToJsInterface {

    protected Context mContext;
    protected WebView mWebView;
    public BaseAppToJsInterface(Context context, WebView webView) {
        this.mContext = context;
        this.mWebView=webView;
    }

    //app调用js
    public void appToJsInterface(String MethodName, String data){
        this.mWebView.loadUrl("javascript:"+MethodName+"('" + data + "')");
    }

    //app返回给js的String
    public String toJson(int code, String data){
        Gson gson = new Gson();
        WebViewResponseModel webviewResponseModel = new WebViewResponseModel();
        webviewResponseModel.setCode(code);
        webviewResponseModel.setData(data);
        return gson.toJson(webviewResponseModel);
    }
}
