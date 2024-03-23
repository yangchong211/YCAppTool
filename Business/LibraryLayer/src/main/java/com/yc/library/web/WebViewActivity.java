package com.yc.library.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.yc.library.base.config.Constant;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.intent.AppIntentUtils;
import com.yc.toolutils.appinfo.AppWindowUtils;
import com.yc.webviewlib.inter.BridgeHandler;
import com.yc.webviewlib.inter.CallBackFunction;
import com.yc.webviewlib.inter.InterWebListener;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;
import com.yc.library.R;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 外部链接跳转的页面
 *     revise: 原生webView
 * </pre>
 */
public class WebViewActivity extends BaseActivity {

    private Toolbar toolbar;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private X5WebView mWebView;
    private WebProgress progress;
    public String url;
    private String name;

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.unregisterHandler("doPhone");
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView!=null && mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void lunch(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(Constant.URL, url);
        intent.putExtra(Constant.TITLE, title);
        activity.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(url==null || url.length()==0){
            url = "about:blank";
        }
        int i = item.getItemId();
        if (i == R.id.share) {
            //DoShareUtils.shareText(this, url, name);
        } else if (i == R.id.collect) {
            ToastUtils.showRoundRectToast("后期添加");
        } else if (i == R.id.cope) {
            AppWindowUtils.copyToClipBoard(url);
            ToastUtils.showRoundRectToast("复制成功");
        } else if (i == R.id.open) {
            AppIntentUtils.openLink(this,url);
        } else if (i == R.id.capture) {
            ToastUtils.showRoundRectToast("屏幕截图，后期处理");
        } else if (i == R.id.about){

        } else {

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getContentView() {
        return R.layout.base_web_view;
    }


    @Override
    public void initView() {
        initFindById();
        initToolBar();
        initIntentData();
    }

    private void initFindById() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        mWebView = findViewById(R.id.webView);
        progress = findViewById(R.id.progress);
    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    public void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            url = intent.getStringExtra(Constant.URL);
            name = intent.getStringExtra(Constant.TITLE);
        }else {
            url = "https://github.com/yangchong211";
            name = "新闻";
        }
        toolbarTitle.setText(name==null?"":name);
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(view -> finish());
    }


    @Override
    public void initData() {
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent),
                this.getResources().getColor(R.color.colorPrimaryDark));
        mWebView.loadUrl(url);
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
        //Android调用js
        mWebView.callHandler("go", "", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                X5LogUtils.d("js call back data : " + data);
            }
        });
        //js调用Android
        mWebView.registerHandler("toPhone", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });
    }

    private final InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void showErrorView(@X5WebUtils.ErrorType int type) {
            switch (type){
                //没有网络
                case X5WebUtils.ErrorMode.NO_NET:
                    toolbarTitle.setText("没有网络");
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
            //设置进度
            progress.setWebProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {
            toolbarTitle.setText(title);
        }

        @Override
        public void onPageFinished(String url) {

        }
    };

}
