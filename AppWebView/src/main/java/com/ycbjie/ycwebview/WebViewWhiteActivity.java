package com.ycbjie.ycwebview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.net.AppHttpUtils;
import com.yc.toolutils.net.AppNetworkUtils;
import com.yc.webviewlib.base.X5WebChromeClient;
import com.yc.webviewlib.client.JsX5WebViewClient;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;


import java.util.ArrayList;
import java.util.Random;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/17
 *     desc  : webView页面
 *     revise: 暂时先用假数据替代
 * </pre>
 */
public class WebViewWhiteActivity extends AppCompatActivity {

    private X5WebView mWebView;
    private WebProgress progress;
    private String url;
    private boolean hide = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() ==
                KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mWebView.canGoBack()) {
                //退出网页
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.resume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWebView != null) {
            mWebView.stop();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yc_web_view);
        initData();
        initView();
    }

    public void initData() {
        Intent intent = getIntent();
        if (intent!=null){
            url = intent.getStringExtra("url");
            hide = intent.getBooleanExtra("hide",false);
        }
    }
    
    public void initView() {
        mWebView = findViewById(R.id.web_view);
        progress = findViewById(R.id.progress);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent),this.getResources().getColor(R.color.colorPrimaryDark));

        //String url = "http://10.3.138.78:8080/wangzhebiyeji";
        if (url==null || url.length()==0){
            Random random = new Random();
            int i = random.nextInt(10);
            if (i>=0 && i<4){
                url = "https://www.baidu.com";
            } else if (i>=4 && i<7){
                url = "https://www.json.cn/";
            } else {
                url = "https://www.taobao.com";
            }
            X5LogUtils.d("WebViewUtils----i----"+i+"----"+url);
        }
        mWebView.loadUrl(url);
        if (!AppNetworkUtils.isConnected()){
            ToastUtils.showRoundRectToast("请先连接上网络");
        }

        YcX5WebViewClient webViewClient = new YcX5WebViewClient(mWebView, this);
        mWebView.setWebViewClient(webViewClient);
        YcX5WebChromeClient webChromeClient = new YcX5WebChromeClient(mWebView,this);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
    }


    private DefaultWebListener interWebListener = new DefaultWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
        }

        @Override
        public void showErrorView(@X5WebUtils.ErrorType int type) {
            switch (type){
                //没有网络
                case X5WebUtils.ErrorMode.NO_NET:
                    break;
                //404，网页无法打开
                case X5WebUtils.ErrorMode.STATE_404:

                    break;
                //onReceivedError，请求网络出现error
                case X5WebUtils.ErrorMode.RECEIVED_ERROR:

                    break;
                //在加载资源时通知主机应用程序发生SSL错误
                case X5WebUtils.ErrorMode.SSL_ERROR:

                    break;
                //代理异常
                case X5WebUtils.ErrorMode.ERROR_PROXY:
                    
                    break;
                default:
                    break;
            }
        }

        @Override
        public void startProgress(int newProgress) {
            progress.setWebProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {

        }
    };

    private ArrayList<String> domainList = new ArrayList<>();

    private class YcX5WebViewClient extends JsX5WebViewClient {
        public YcX5WebViewClient(X5WebView webView, Context context) {
            super(webView, context);
            //添加白名单
            try {
                X5LogUtils.d("WebViewUtils----添加白名单----");
                String host1 = Uri.parse("https://www.baidu.com").getHost();
                String host2 = Uri.parse("https://www.json.cn/").getHost();
                domainList.clear();
                domainList.add(host1);
                domainList.add(host2);
                X5LogUtils.d("WebView----初始化webView操作"+"-host1---"+host1);
                X5LogUtils.d("WebView----初始化webView操作"+"-host2---"+host2);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            X5LogUtils.i("----shouldOverrideUrlLoading"+"start to shouldOverrideUrlLoading url:"+url);
            String host1 = Uri.parse(url).getHost();
            X5LogUtils.d("WebViewUtils----添加白名单--1-host1-"+host1);
            if (AppHttpUtils.isWhiteList(domainList,url)) {
                //域名校验通过，允许请求
                return super.shouldOverrideUrlLoading(view, url);
            }
            X5LogUtils.d("WebViewUtils----添加白名单--1--");
            //域名校验失败，终止请求。然后load自己的地址
            String myUrl = "https://github.com/yangchong211/";
            view.loadUrl(myUrl);
            return true;
            //return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            X5LogUtils.i("----shouldOverrideUrlLoading"+"start to shouldOverrideUrlLoading url>23:"+request.getUrl());
            String inputUrl = request.getUrl().toString();
            String host2 = Uri.parse(inputUrl).getHost();
            X5LogUtils.d("WebViewUtils----添加白名单--2-host2-"+host2);
            if (AppHttpUtils.isWhiteList(domainList,inputUrl)) {
                //域名校验通过，允许请求
                return super.shouldOverrideUrlLoading(view, request);
            }
            X5LogUtils.d("WebViewUtils----添加白名单--2--");
            //域名校验失败，终止请求。然后load自己的地址
            String url = "https://github.com/yangchong211/";
            view.loadUrl(url);
            return true;
        }
    }


    private class YcX5WebChromeClient extends X5WebChromeClient {
        public YcX5WebChromeClient(X5WebView webView, Activity activity) {
            super(webView,activity);
        }
    }

}
