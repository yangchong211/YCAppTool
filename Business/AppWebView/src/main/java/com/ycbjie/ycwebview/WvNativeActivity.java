package com.ycbjie.ycwebview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.ValueCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;
import com.yc.webviewlib.base.X5WebChromeClient;
import com.yc.webviewlib.base.X5WebViewClient;
import com.yc.webviewlib.bridge.DefaultHandler;
import com.yc.webviewlib.inter.BridgeHandler;
import com.yc.webviewlib.inter.CallBackFunction;
import com.yc.webviewlib.inter.DefaultVideoListener;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;
import com.yc.webviewlib.wv.ResponseCallback;
import com.yc.webviewlib.wv.WvJsHandler;
import com.yc.webviewlib.wv.X5WvWebView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/17
 *     desc  : webView页面
 *     revise: 暂时先用假数据替代
 * </pre>
 */
public class WvNativeActivity extends AppCompatActivity {

    private X5WvWebView mWebView;
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
        setContentView(R.layout.activity_wv_native_view);
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
        progress.setColor(this.getResources().getColor(R.color.colorAccent));
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
        if (url==null || url.length()==0){
            url = "https://baidu.com";
            ToastUtils.showRoundRectToast("输入地址不能为空");
        }
        mWebView.loadUrl(url);
    }

    private DefaultWebListener interWebListener = new DefaultWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
            setJavascript("ugc-content");
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


    public void setJavascript(String className) {
        try {
            //https://h5.zybang.com/plat/app-vue/ugcDetail.html?articleId=0f89385c4205377ef2442ed0969e0b9c&hideNativeTitleBar=1
            //定义javaScript方法
            //定义javaScript方法
            String javascript = "javascript:function getDivContent() { "
                    + "document.getElementsByClassName('" + className + "')[0]";
            String javaScript = "javascript:function getDivContent() { "
                    + "document.getElementsByClassName(\"ugc-content\")[0]";
            mWebView.loadUrl(javaScript);
            //mWebView.loadUrl("javascript:getDivContent();");
            mWebView.evaluateJavascript("javascript:getDivContent();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    //此处为 js 返回的结果
                    X5LogUtils.i("抓包返回数据---"+s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
