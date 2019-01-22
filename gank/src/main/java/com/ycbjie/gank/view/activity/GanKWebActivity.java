package com.ycbjie.gank.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ycbjie.gank.R;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.gank.bean.cache.CacheGanKFavorite;
import com.ycbjie.gank.contract.GanKWebContract;
import com.ycbjie.gank.presenter.GanKWebPresenter;
import com.ycbjie.library.utils.AppUtils;
import com.ycbjie.library.utils.DoShareUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.utils.MDTintUtil;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 干货集中营详情页面
 *     revise:
 * </pre>
 */
public class GanKWebActivity extends BaseActivity implements View.OnClickListener, GanKWebContract.View {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    Toolbar toolbar;
    private WebView webView;
    private ProgressBar pb;
    private LinearLayout llFab;
    private FloatingActionButton fabFavorite;
    private FloatingActionButton fabTop;




    private GanKWebContract.Presenter presenter = new GanKWebPresenter(this);
    private String url;
    private String title;
    private CacheGanKFavorite favorite;
    private boolean isForResult;            // 是否回传结果

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.base_web_view;
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        webView = (WebView) findViewById(R.id.webView);
        pb = (ProgressBar) findViewById(R.id.pb);
        llFab = (LinearLayout) findViewById(R.id.ll_fab);
        fabFavorite = (FloatingActionButton) findViewById(R.id.fab_favorite);
        fabTop = (FloatingActionButton) findViewById(R.id.fab_top);
        initIntent();
        initToolBar();
        initWebView();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            favorite = (CacheGanKFavorite) intent.getSerializableExtra("favorite");
        }
        if(title==null || title.length()==0){
            title = "新闻详情页面";
        }
    }

    private void initToolBar() {
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbarTitle.setText(title);
        llFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        fabFavorite.setOnClickListener(this);
        fabTop.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_view_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.share) {
            DoShareUtils.shareText(this, "分享", title);
        } else if (i == R.id.collect) {
            ToastUtils.showToast("收藏");
        } else if (i == R.id.cope) {
            if (TextUtils.isEmpty(url)) {
                ToastUtils.showToast("复制链接错误");
            } else {
                AppUtils.copy(url);
                ToastUtils.showToast("复制链接成功");
            }
        } else if (i == R.id.open) {
            AppUtils.openLink(this, url);
        } else {
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else if (i == R.id.fab_favorite) {
            presenter.meFavorite();
        } else if (i == R.id.fab_top) {
            webView.scrollTo(0, 0);
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);

        webView.setWebChromeClient(new MyWebChrome());
        webView.setWebViewClient(new MyWebClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY-oldScrollY > 0) {
                        fabFavorite.hide();
                        fabTop.hide();
                    } else {
                        fabFavorite.show();
                        fabTop.show();
                    }
                }
            });
        }else {
            webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int y = (int) webView.getY();
                    int scrollY = webView.getScrollY();
                    if (y-scrollY > 0) {
                        fabFavorite.hide();
                        fabTop.hide();
                    } else {
                        fabFavorite.show();
                        fabTop.show();
                    }
                }
            });
        }
    }



    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            pb.setVisibility(View.GONE);
        }
    }


    private class MyWebChrome extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            toolbarTitle.setText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(pb!=null){
                pb.setProgress(newProgress);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                }
            }
        }
    }


    /**
     * 设置fab按钮的颜色
     */
    @Override
    public void setFabColor() {
        MDTintUtil.setTint(fabFavorite, this.getResources().getColor(R.color.colorTheme));
        MDTintUtil.setTint(fabTop, this.getResources().getColor(R.color.colorTheme));
    }

    /**
     * 获取收藏序列化内存
     */
    @Override
    public CacheGanKFavorite getFavoriteData() {
        return favorite;
    }

    /**
     * 获取加载url
     */
    @Override
    public String getLoadUrl() {
        return url == null ? "about:blank" : url;
    }

    /**
     * 加载内容
     */
    @Override
    public void loadURL(String url) {
        webView.loadUrl(url);
    }

    /**
     * 设置收藏的状态
     */
    @Override
    public void setFavoriteState(boolean isFavorite) {
        if (isFavorite) {
            fabFavorite.setImageResource(R.drawable.ic_gank_favorite);
        } else {
            fabFavorite.setImageResource(R.drawable.ic_gank_un_favorite);
        }
        isForResult = !isFavorite;
    }

    @Override
    public void hideFavoriteFab() {
        fabFavorite.setVisibility(View.GONE);
        fabTop.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(null);
        }else {
            webView.getViewTreeObserver().addOnScrollChangedListener(null);
        }
    }

}
