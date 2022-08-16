package com.ycbjie.ycwebview;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.webviewlib.base.X5WebChromeClient;
import com.yc.webviewlib.base.X5WebViewClient;
import com.yc.webviewlib.inter.DefaultVideoListener;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;
import com.ycbjie.ycwebview.traffic.TrafficUtils;


public class FiveActivity extends AppCompatActivity {

    private X5WebView webView;
    private X5WebChromeClient x5WebChromeClient;
    private X5WebViewClient x5WebViewClient;
    private WebProgress progress;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (x5WebChromeClient!=null && x5WebChromeClient.inCustomView()) {
                x5WebChromeClient.hideCustomView();
                return true;
                //返回网页上一页
            } else if (webView.canGoBack()) {
                //退出网页
                webView.goBack();
                return true;
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
            if (x5WebChromeClient!=null){
                x5WebChromeClient.removeVideoView();
            }
            webView.destroy();
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yc_web_view);
        webView = findViewById(R.id.web_view);

        String movieUrl = "https://www.iqiyi.com/";
        webView.loadUrl(movieUrl);
        progress = findViewById(R.id.progress);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent));

        x5WebChromeClient = webView.getX5WebChromeClient();
        x5WebViewClient = webView.getX5WebViewClient();
        x5WebChromeClient.setWebListener(interWebListener);
        x5WebViewClient.setWebListener(interWebListener);
        //设置是否自定义视频视图
        webView.setShowCustomVideo(false);
        x5WebChromeClient.setVideoWebListener(new DefaultVideoListener() {
            @Override
            public void showVideoFullView() {
                //视频全频播放时监听
            }

            @Override
            public void hindVideoFullView() {
                //隐藏全频播放，也就是正常播放视频
            }

            @Override
            public void showWebView() {
                //显示webView
            }

            @Override
            public void hindWebView() {
                //隐藏webView
            }
        });
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
