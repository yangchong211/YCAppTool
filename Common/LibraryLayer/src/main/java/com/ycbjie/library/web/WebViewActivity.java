package com.ycbjie.library.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.yc.configlayer.arounter.ARouterUtils;
import com.yc.configlayer.arounter.RouterConfig;
import com.yc.configlayer.constant.Constant;
import com.ycbjie.library.R;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.utils.AppUtils;
import com.ycbjie.library.utils.DoShareUtils;
import com.ycbjie.webviewlib.inter.InterWebListener;
import com.ycbjie.webviewlib.utils.X5WebUtils;
import com.ycbjie.webviewlib.view.X5WebView;
import com.ycbjie.webviewlib.widget.WebProgress;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 外部链接跳转的页面
 *     revise: 原生webView
 * </pre>
 */
@Route(path = RouterConfig.Library.ACTIVITY_LIBRARY_WEB_VIEW)
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
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView!=null && mWebView.pageGoBack()){
                mWebView.pageGoBack();
            }
        }
        return false;
    }


    public static void lunch(Activity activity, String url, String title) {
        if(activity!=null){
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra(Constant.URL, url);
            intent.putExtra(Constant.TITLE, title);
            activity.startActivity(intent);
        }
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
            DoShareUtils.shareText(this, url, name);
        } else if (i == R.id.collect) {
            ToastUtils.showRoundRectToast("后期添加");
        } else if (i == R.id.cope) {
            AppUtils.copy(url);
            ToastUtils.showRoundRectToast("复制成功");
        } else if (i == R.id.open) {
            AppUtils.openLink(this, url);
        } else if (i == R.id.capture) {
            ToastUtils.showRoundRectToast("屏幕截图，后期处理");
        } else if (i == R.id.about){
            ARouterUtils.navigation(RouterConfig.Demo.ACTIVITY_OTHER_ABOUT_ME);
        }else {
            ARouterUtils.navigation(RouterConfig.Demo.ACTIVITY_OTHER_ABOUT_ME,
                    this, new NavCallback() {
                @Override
                public void onArrival(Postcard postcard) {
                    LogUtils.i("ARouterUtils"+"---跳转完了");
                }

                @Override
                public void onFound(Postcard postcard) {
                    super.onFound(postcard);
                    LogUtils.i("ARouterUtils"+"---找到了");
                }

                @Override
                public void onInterrupt(Postcard postcard) {
                    super.onInterrupt(postcard);
                    LogUtils.i("ARouterUtils"+"---被拦截了");
                }

                @Override
                public void onLost(Postcard postcard) {
                    super.onLost(postcard);
                    LogUtils.i("ARouterUtils"+"---找不到了");
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getContentView() {
        return R.layout.base_web_view;
    }


    @Override
    public void initView() {
        ARouterUtils.injectActivity(this);
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
    }

    private InterWebListener interWebListener = new InterWebListener() {
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
            progress.setWebProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {
            toolbarTitle.setText(StringUtils.null2Length0(title));
        }
    };

}
