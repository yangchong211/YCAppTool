package com.ycbjie.ycwebview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.yc.webviewlib.wv.MethodExistCallback;
import com.yc.webviewlib.wv.ResponseCallback;
import com.yc.webviewlib.wv.WvJsHandler;
import com.yc.webviewlib.wv.X5WvWebView;


import static android.widget.Toast.LENGTH_SHORT;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/17
 *     desc  : webView页面
 *     revise: 暂时先用假数据替代
 * </pre>
 */
public class WvNativeActivity2 extends AppCompatActivity {

    private X5WvWebView mWebView;
    private WebProgress progress;
    private Button btn;

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
        setContentView(R.layout.activity_wv_native_view2);
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


        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
        initWebViewBridge();

        findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 无参数调用
                mWebView.callHandler("jsRcvResponseTest", "", new ResponseCallback<Object>() {
                    @Override
                    public void onResult(Object data) {
                        Toast.makeText(WvNativeActivity2.this,"reponse data from js " + data, LENGTH_SHORT).show();
                        Log.i("java调用web----", "reponse data from js " + data);
                    }
                });
            }
        });
        findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.callHandler("echoHandler", "你个傻逼", new ResponseCallback<Object>() {
                    @Override
                    public void onResult(Object data) {
                        Log.i("java调用web----", "reponse data from js " + data);
                        Toast.makeText(WvNativeActivity2.this,"reponse data from js " + data, LENGTH_SHORT).show();
                    }
                });
            }
        });
        findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.hasJavascriptMethod("echoHandler", new MethodExistCallback() {
                    @Override
                    public void onResult(boolean exist) {
                        if(exist) {
                            Toast.makeText(WvNativeActivity2.this,"逗比，有这个方法" , LENGTH_SHORT).show();
                            Log.d("java调用web----", "Javascript handler 'echoHandler' exist. ");
                        }
                    }
                });
            }
        });
        mWebView.loadUrl("file:///android_asset/echo.html");
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
        //js调native
        mWebView.registerHandler("toSeller", new WvJsHandler<Object, Object>() {
            @Override
            public void handler(Object data, ResponseCallback<Object> callback) {
                callback.onResult(data);
            }
        });
        mWebView.registerHandler("javaEchoToJs", new WvJsHandler() {
            @Override
            public void handler(Object data, ResponseCallback callback) {
                Toast.makeText(WvNativeActivity2.this,data.toString(),LENGTH_SHORT).show();
                Log.d("js调native",data.toString());
                callback.onResult(data+"我是小杨逗比");
            }
        });
    }

}
