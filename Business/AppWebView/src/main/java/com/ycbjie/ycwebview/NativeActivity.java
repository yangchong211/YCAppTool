package com.ycbjie.ycwebview;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.webviewlib.base.X5WebChromeClient;
import com.yc.webviewlib.base.X5WebViewClient;
import com.yc.webviewlib.bridge.DefaultHandler;
import com.yc.webviewlib.inter.BridgeHandler;
import com.yc.webviewlib.inter.CallBackFunction;
import com.yc.webviewlib.inter.DefaultVideoListener;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/17
 *     desc  : webView页面
 *     revise: 暂时先用假数据替代
 * </pre>
 */
public class NativeActivity extends AppCompatActivity {

    private X5WebView mWebView;
    private WebProgress progress;
    private Button btn;

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
            //mWebView = null;
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
        setContentView(R.layout.activity_native_view);
        initData();
        initView();
    }


    public void initData() {

    }

    public void initView() {
        mWebView = findViewById(R.id.web_view);
        progress = findViewById(R.id.progress);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent));

        btn = findViewById(R.id.btn);
        mWebView.loadUrl("file:///android_asset/demo.html");
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.callHandler("functionInJs", "data from Java", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(NativeActivity.this,"你个逗比" + data,Toast.LENGTH_SHORT).show();
                        Log.i("java调用web----", "reponse data from js " + data);
                    }

                });
                /*具体看demo.html的这段代码
                bridge.registerHandler("functionInJs", function(data, responseCallback) {
                    document.getElementById("show").innerHTML = ("data from Java: = " + data);
                    if (responseCallback) {
                        var responseData = "Javascript Says Right back aka!";
                        responseCallback(responseData);
                    }
                });*/
            }
        });
        mWebView.setInitialScale(250);
        //js交互方法
        initWebViewBridge();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //这个是处理回调逻辑
        mWebView.getX5WebChromeClient().uploadMessageForAndroid5(data,resultCode);
    }

    @JavascriptInterface
    public void initWebViewBridge() {
        mWebView.setDefaultHandler(new DefaultHandler());
        //js调native
        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(NativeActivity.this,data+"yc",Toast.LENGTH_LONG).show();
                Log.i("registerHandler", "handler = submitFromWeb, data from web = " + data);
                //这个是回调给web端，比如
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }
        });

        //Android调用js
        mWebView.callHandler("functionInJs", "小杨逗比", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Toast.makeText(NativeActivity.this,data+"逗比",Toast.LENGTH_LONG).show();
            }
        });
    }

}
