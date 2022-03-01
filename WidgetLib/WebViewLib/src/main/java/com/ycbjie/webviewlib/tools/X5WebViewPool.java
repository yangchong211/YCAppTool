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
package com.ycbjie.webviewlib.tools;

import android.content.Context;
import android.view.ViewGroup;

import com.ycbjie.webviewlib.view.X5WebView;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : webView缓存池
 *     revise:
 * </pre>
 */
public class X5WebViewPool {

    //Android Webview复用池+独立进程
    //https://www.heng666.cn/324.html
    private static List<X5WebView> available = new ArrayList<>();
    private static final byte[] lock = new byte[]{};
    private static int maxSize = 2;
    private int currentSize = 0;
    private static volatile X5WebViewPool instance = null;

    public static X5WebViewPool getInstance() {
        if (instance == null) {
            synchronized (X5WebViewPool.class) {
                if (instance == null) {
                    instance = new X5WebViewPool();
                }
            }
        }
        return instance;
    }

    /**
     * webView 初始化
     * 最好放在application oncreate里
     */
    public static void init(Context context) {
        for (int i = 0; i < maxSize; i++) {
            X5WebView webView = new X5WebView(context);
            //webView.loadUrl(DEMO_URL);
            available.add(webView);
        }
    }

    /**
     * 获取webView
     */
    public X5WebView getWebView(Context context) {
        synchronized (lock) {
            X5WebView webView;
            if (available.size() > 0) {
                webView = available.get(0);
                available.remove(0);
                currentSize--;
            } else {
                webView = new X5WebView(context);
                available.add(webView);
                currentSize++;
            }
            return webView;
        }
    }

    /**
     * 回收webview ,不解绑
     *
     * @param webView 需要被回收的webview
     */
    public void removeWebView(X5WebView webView) {
        if (currentSize==0){
            return;
        }
        webView.loadUrl("");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.clearCache(true);
        webView.clearHistory();
        synchronized (lock) {
            if (available.size() > 0 ) {
                available.remove(webView);
            } else {
                webView = null;
            }
            currentSize--;
        }
    }

    /**
     * 回收webView ,解绑
     *
     * @param webView 需要被回收的webview
     */
    public void removeWebView(ViewGroup viewGroup, X5WebView webView) {
        if (currentSize==0){
            return;
        }
        viewGroup.removeView(webView);
        webView.loadUrl("");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.clearCache(true);
        webView.clearHistory();
        synchronized (lock) {
            if (available.size() > 0) {
                available.remove(webView);
            } else {
                webView = null;
            }
            currentSize--;
        }
    }

    /**
     * 设置webView池个数
     *
     * @param size webview池个数
     */
    public void setMaxPoolSize(int size) {
        synchronized (lock) {
            maxSize = size;
        }
    }

}
