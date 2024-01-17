package com.ycbjie.ycwebview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
public class WebViewActivity2 extends AppCompatActivity {

    private X5WebView mWebView;
    private WebProgress progress;
    private String url;
    private boolean isLoadFinish = false;

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
        setContentView(R.layout.activity_web_view2);
        initData();
        initView();
        final RelativeLayout rl_window = findViewById(R.id.rl_window);
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadFinish){
                    //Bitmap bitmap = AppUtils.measureSize(WebViewActivity2.this, mWebView);
                    Bitmap bitmap = AppUtils.activityShot(WebViewActivity2.this);
                    ModelStorage.getInstance().setBitmap(bitmap);
                    startActivity(new Intent(WebViewActivity2.this,ImageActivity.class));
                } else {
                    Toast.makeText(WebViewActivity2.this,"还没有加载完",Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadFinish){
                    //Bitmap bitmap = AppUtils.activityShot(WebViewActivity2.this);
                    //第一种
                    //Bitmap bitmap = AppUtils.getRelativeLayoutBitmap(rl_window);
                    //第二种，实际开发中，建议用这种
                    Bitmap bitmap = AppUtils.measureSize(WebViewActivity2.this, rl_window);
                    ModelStorage.getInstance().setBitmap(bitmap);
                    startActivity(new Intent(WebViewActivity2.this,ImageActivity.class));
                } else {
                    Toast.makeText(WebViewActivity2.this,"还没有加载完",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        progress.setColor(this.getResources().getColor(R.color.colorAccent),this.getResources().getColor(R.color.colorPrimaryDark));

        mWebView.loadUrl("https://github.com/yangchong211/LifeHelper");
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
    }


    private DefaultWebListener interWebListener = new DefaultWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
            isLoadFinish = true;
            findViewById(R.id.btn_1).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_2).setVisibility(View.VISIBLE);

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
