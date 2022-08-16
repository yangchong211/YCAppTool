package com.ycbjie.ycwebview;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.widget.WebProgress;


public class FirstActivity2 extends AppCompatActivity {

    private WebView webView;
    private WebProgress progress;
    private TextView tvTitle;
    private TextView tvRefresh;
    private RotateAnimation rotate;
    private String url;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
                //退出网页
            } else {
                handleFinish();
            }
        }
        return false;
    }

    public void handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        try {
            if (webView != null) {
                webView.destroy();
                webView = null;
            }
            if (rotate!=null){
                rotate.cancel();
            }
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view3);
        webView = findViewById(R.id.web_view);
        progress = findViewById(R.id.progress);
        tvTitle = findViewById(R.id.tv_title);
        tvRefresh = findViewById(R.id.tv_refresh);
        initWebView();
        initListener();
    }

    private void initWebView() {
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent),this.getResources().getColor(R.color.colorPrimaryDark));
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        url = "https://www.wanandroid.com/index";
        webView.loadUrl(url);
    }

    private void initListener() {
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl(url);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
                finish();
            }
        });
    }

//    int running = 0;

    private class MyWebViewClient extends WebViewClient {

//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            String s = request.getUrl().toString();
//            X5LogUtils.i("-------shouldOverrideUrlLoading----->21--"+s);
//            //view.loadUrl(s);
//            //return true;
//            return super.shouldOverrideUrlLoading(view, request);
//        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //判断重定向的方式一
            X5LogUtils.i("-----shouldOverrideUrlLoading------loadUrl-------"+url);
            WebView.HitTestResult hitTestResult = view.getHitTestResult();
            if(hitTestResult == null) {
                return false;
            }
            if(hitTestResult.getType() == WebView.HitTestResult.UNKNOWN_TYPE) {
                //X5LogUtils.i("-------重定向-------");
                return false;
            }
            view.loadUrl(url);
//            running++;
            return true;
            //return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            X5LogUtils.i("-------onPageStarted-------"+url);
//            running = Math.max(running, 1);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            X5LogUtils.i("-------onPageFinished-------"+url);
//            if (--running==0) {
//                //做操作，隐藏加载进度条或者加载loading
//                X5LogUtils.i("-------onPageFinished-----结束--");
//            }
        }
    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            X5LogUtils.i("-------onReceivedTitle-------"+title);
            //bar.setTitle(title));
            getWebTitle(view);
//            if (--running==0) {
//                bar.setTitle(title);
//            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progress.setWebProgress(newProgress);
        }
    }

    private void getWebTitle(WebView view){
        WebBackForwardList forwardList = view.copyBackForwardList();
        WebHistoryItem item = forwardList.getCurrentItem();
        if (item != null) {
            tvTitle.setText(item.getTitle());
            // X5LogUtils.i("-------onReceivedTitle----getWebTitle---"+item.getTitle());
        }
    }
}
