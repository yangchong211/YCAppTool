package com.ycbjie.ycwebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.yc.toolutils.component.AppActivityUtils;
import com.yc.webviewlib.client.JsX5WebViewClient;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/17
 *     desc  : webView页面
 *     revise: 暂时先用假数据替代
 * </pre>
 */
public class DeepLinkActivity extends AppCompatActivity {

    private X5WebView mWebView;
    private WebProgress progress;
    private String url;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && event.getKeyCode() ==
                KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearHistory();
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.destroy();
            //mWebView = null;
        }
        super.onDestroy();
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(false);
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
        }
    }

    public void initView() {
        mWebView = findViewById(R.id.web_view);
        progress = findViewById(R.id.progress);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent),
                this.getResources().getColor(R.color.colorPrimaryDark));

        MyX5WebViewClient webViewClient = new MyX5WebViewClient(mWebView, this);
        webViewClient.setWebListener(interWebListener);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl("file:///android_asset/deeplink.html");
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
    }


    private class MyX5WebViewClient extends JsX5WebViewClient {

        /**
         * 构造方法
         *
         * @param webView 需要传进来webview
         * @param context 上下文
         */
        public MyX5WebViewClient(WebView webView, Context context) {
            super(mWebView, context);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            boolean activityAlive = AppActivityUtils.isActivityAlive(DeepLinkActivity.this);
            if (!activityAlive){
                return false;
            }
            if (TextUtils.isEmpty(url)) {
                return false;
            }
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            //如果是返回数据
            if(url.startsWith("https:")){
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(url);
                intent1.setData(uri);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DeepLinkActivity.this.startActivity(intent1);
                return true;
            }else if(url.startsWith("weixin:")){
                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(url);
                intent2.setData(uri);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DeepLinkActivity.this.startActivity(intent2);
                return true;
            }else if(url.startsWith("taobao:")){
                Intent intent3 = new Intent();
                intent3.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(url);
                intent3.setData(uri);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DeepLinkActivity.this.startActivity(intent3);
                return true;
            } else {
                mWebView.loadUrl(url);
                return true;
            }
            //return super.shouldOverrideUrlLoading(webView, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            boolean activityAlive = AppActivityUtils.isActivityAlive(DeepLinkActivity.this);
            if (!activityAlive){
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String url = request.getUrl().toString();
                if (TextUtils.isEmpty(url)) {
                    return false;
                }
                try {
                    url = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
                //如果是返回数据
                if(url.startsWith("yilu:")){
                    Intent intent1 = new Intent();
                    intent1.setAction("android.intent.action.VIEW");
                    Uri uri = Uri.parse(url);
                    intent1.setData(uri);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DeepLinkActivity.this.startActivity(intent1);
                    return true;
                }else if(url.startsWith("weixin:")){
                    Intent intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    Uri uri = Uri.parse(url);
                    intent2.setData(uri);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DeepLinkActivity.this.startActivity(intent2);
                    return true;
                }else if(url.startsWith("taobao:")){
                    Intent intent3 = new Intent();
                    intent3.setAction("android.intent.action.VIEW");
                    Uri uri = Uri.parse(url);
                    intent3.setData(uri);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DeepLinkActivity.this.startActivity(intent3);
                    return true;
                }else {
                    mWebView.loadUrl(url);
                    return true;
                }
            }else {
                return super.shouldOverrideUrlLoading(webView, request);
            }
        }
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
}
