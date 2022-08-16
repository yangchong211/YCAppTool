package com.ycbjie.ycwebview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;
import com.ycbjie.ycwebview.traffic.TrafficUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/17
 *     desc  : webView页面
 *     revise: 暂时先用假数据替代
 * </pre>
 */
public class CacheWebViewActivity2 extends AppCompatActivity {

    //public static String url =  "https://www.jianshu.com/u/b7b2c6ed9284";
    public static String url =  "https://h5.youzan.com/v2/showcase/homepage?alias=lUWblj8NNI";
    private X5WebView mWebView;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    private WebProgress progress;
    private TrafficUtils instance;

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
        }
        super.onDestroy();
        if (instance!=null){
            instance.stopCalculateNetSpeed();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_cache);
        initData();
        initView();
    }


    public void initData() {

    }

    public void initView() {
        initFindViewById();
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent),this.getResources().getColor(R.color.colorPrimaryDark));
        mWebView.loadUrl(url);
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
        initTraffic();
    }

    private void initFindViewById() {
        mWebView = findViewById(R.id.web_view);
        progress = findViewById(R.id.progress);
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTv4 = findViewById(R.id.tv_4);
    }

    @SuppressLint("SetTextI18n")
    private void initTraffic() {
        long networkRxBytes = TrafficUtils.getNetworkRxBytes();
        long networkTxBytes = TrafficUtils.getNetworkTxBytes();
        instance = TrafficUtils.getInstance(this, new Handler());
        instance.startCalculateNetSpeed();
        double netSpeed = instance.getNetSpeed();
        long trafficInfo = instance.getTrafficInfo();
        mTv1.setText("当前网速"+netSpeed);
        mTv2.setText("下载流量总和"+networkRxBytes);
        mTv3.setText("上传流量总和"+networkTxBytes);
        mTv4.setText("流量总和"+trafficInfo);
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
