package com.ycbjie.ycwebview;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.widget.WebProgress;

public class FirstActivity3 extends AppCompatActivity {

    private BaseWebView webView;
    private WebProgress progress;
    private TextView tvTitle;
    private ImageView ivRefresh;
    private RotateAnimation rotate;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view1);
        webView = findViewById(R.id.web_view);
        progress = findViewById(R.id.progress);
        tvTitle = findViewById(R.id.tv_title);
        ivRefresh = findViewById(R.id.iv_refresh);


        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent),this.getResources().getColor(R.color.colorPrimaryDark));
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        String url = "https://github.com/yangchong211/YCVideoPlayer";
        webView.loadUrl(url);

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
                if (rotate != null) {
                    ivRefresh.startAnimation(rotate);
                }  else {
                    rotate = new RotateAnimation(0, 3600,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(2000);
                    rotate.setFillAfter(true);
                    //减速- 动画插入器
                    rotate.setInterpolator(new DecelerateInterpolator());
                    ivRefresh.setAnimation(rotate);
                    ivRefresh.startAnimation(rotate);
                }
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
            WebView.HitTestResult hitTestResult = view.getHitTestResult();
            if(hitTestResult == null) {
                return false;
            }
            if(hitTestResult.getType() == WebView.HitTestResult.UNKNOWN_TYPE) {
                //X5LogUtils.i("-------重定向-------");
                return false;
            }

            if (webView.isTouchByUser()){
                X5LogUtils.i("-------点击事件-------");
            }
            view.loadUrl(url);
            X5LogUtils.i("-----shouldOverrideUrlLoading------loadUrl-------");
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
