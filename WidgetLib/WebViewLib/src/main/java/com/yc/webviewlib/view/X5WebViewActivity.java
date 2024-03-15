package com.yc.webviewlib.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.toolutils.intent.AppIntentUtils;
import com.yc.webviewlib.base.X5WebChromeClient;
import com.yc.webviewlib.base.X5WebViewClient;
import com.yc.webviewlib.inter.DefaultVideoListener;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.tools.AndroidBug5497Workaround;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.widget.WebProgress;
import com.ycbjie.webviewlib.R;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 应用被作为第三方浏览器打开的activity
 *     revise: 这里可供选择
 *             demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public class X5WebViewActivity extends AppCompatActivity {

    private LinearLayout mLlBack;
    private TextView mTvTitle;
    private ImageView mIvMore;
    private X5WebView mWebView;
    private WebProgress mProgress;
    private X5WebChromeClient x5WebChromeClient;
    private X5WebViewClient x5WebViewClient;
    private AndroidBug5497Workaround workaround;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (x5WebChromeClient!=null && x5WebChromeClient.inCustomView()) {
                x5WebChromeClient.hideCustomView();
                return true;
                //返回网页上一页
            } else if (mWebView.canGoBack()) {
                //退出网页
                mWebView.goBack();
                return false;
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
    protected void onDestroy() {
        try {
            if (x5WebChromeClient!=null){
                x5WebChromeClient.removeVideoView();
            }
            mWebView.destroy();
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        if (workaround!=null){
            workaround.onDestroy();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yc_web_view);
        initFindViewById();
        initToolBar();
        initWebView();
        // 处理 作为三方浏览器打开传过来的值
        getDataFromBrowser(getIntent());
        //AndroidBug5497Workaround workaround = new AndroidBug5497Workaround(this);
    }

    private void initFindViewById() {
        mLlBack = findViewById(R.id.ll_back);
        mTvTitle = findViewById(R.id.tv_title);
        mIvMore = findViewById(R.id.iv_more);
        mWebView = findViewById(R.id.web_view);
        mProgress = findViewById(R.id.progress);

        //显示进度条
        mProgress.show();
        //设置进度条过度颜色
        mProgress.setColor(Color.BLUE,Color.RED);
        //设置单色进度条
        mProgress.setColor(Color.BLUE);
    }

    private void initToolBar() {
        mTvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvTitle.setSelected(true);
            }
        }, 1000);
        mTvTitle.setText("加载中……");
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全屏播放退出全屏
                if (x5WebChromeClient!=null && x5WebChromeClient.inCustomView()) {
                    x5WebChromeClient.hideCustomView();
                    //返回网页上一页
                } else if (mWebView.canGoBack()) {
                    //退出网页
                    mWebView.goBack();
                } else {
                    handleFinish();
                }
            }
        });
        mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppIntentUtils.openLink(X5WebViewActivity.this,
                        "https://github.com/yangchong211/YCWebView");
            }
        });
    }

    private void initWebView() {
        x5WebChromeClient = mWebView.getX5WebChromeClient();
        x5WebViewClient = mWebView.getX5WebViewClient();
        x5WebChromeClient.setVideoWebListener(videoWebListener);
        x5WebViewClient.setWebListener(interWebListener);
        x5WebChromeClient.setWebListener(interWebListener);
    }

    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     * 如果这个实例已经存在，intent就会通过onNewIntent传递到这个Activity。
     * 否则新的Activity实例被创建。
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //这个是处理回调逻辑，上传图片成功后的回调
        if (requestCode == X5WebChromeClient.FILE_CHOOSER_RESULT_CODE) {
            x5WebChromeClient.uploadMessage(intent, resultCode);
        } else if (requestCode == X5WebChromeClient.FILE_CHOOSER_RESULT_CODE_5) {
            x5WebChromeClient.uploadMessageForAndroid5(intent, resultCode);
        }
    }


    /**
     * 作为三方浏览器打开传过来的值
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            try {
                X5LogUtils.i("X5WebViewActivity-------uri-----"+data);
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "path: " + path;
                X5LogUtils.i("X5WebViewActivity-------data-----"+text);
                String url = scheme + "://" + host + path;
                X5LogUtils.i("X5WebViewActivity-------url-----"+url);
                //mWebView.loadUrl(url);
                mWebView.loadUrl(data.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private DefaultVideoListener videoWebListener = new DefaultVideoListener() {
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
    };

    private DefaultWebListener interWebListener = new DefaultWebListener() {
        @Override
        public void hindProgressBar() {
            //pb.setVisibility(View.GONE);
            //进度完成后消失
            mProgress.hide();
        }

        @Override
        public void showErrorView(int type) {
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
            //为单独处理WebView进度条
            mProgress.setWebProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {
            mTvTitle.setText(title);
        }
    };

}
