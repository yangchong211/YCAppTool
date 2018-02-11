package com.ns.yc.lifehelper.ui.other.gank.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;
import com.ns.yc.lifehelper.ui.other.gank.contract.GanKWebContract;
import com.ns.yc.lifehelper.ui.other.gank.presenter.GanKWebPresenter;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.DoShareUtils;
import com.ns.yc.lifehelper.utils.MDTintUtil;
import com.ns.yc.lifehelper.utils.statusbar.GanKStatusBarUtil;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/14
 * 描    述：干货集中营详情页面
 * 修订历史：
 * ================================================
 */
public class GanKWebActivity extends BaseActivity implements View.OnClickListener, GanKWebContract.View {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.fab_favorite)
    FloatingActionButton fabFavorite;
    @Bind(R.id.fab_top)
    FloatingActionButton fabTop;
    @Bind(R.id.ll_fab)
    LinearLayout llFab;

    private GanKWebContract.Presenter presenter = new GanKWebPresenter(this);
    private String url;
    private String title;
    private GanKFavorite favorite;
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
        initIntent();
        initToolBar();
        GanKStatusBarUtil.immersive(this);
        GanKStatusBarUtil.setPaddingSmart(this, toolbar);
        initWebView();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            favorite = (GanKFavorite) intent.getSerializableExtra("favorite");
        }
        if(title==null || title.length()==0){
            title = "新闻详情页面";
        }
    }

    private void initToolBar() {
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
        getMenuInflater().inflate(R.menu.movie_web_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                DoShareUtils.shareText(this,"分享",title);
                break;
            case R.id.collect:
                break;
            case R.id.cope:
                if(TextUtils.isEmpty(url)){
                    ToastUtil.showToast(GanKWebActivity.this,"复制链接错误");
                }else {
                    AppUtil.copy(url);
                    ToastUtil.showToast(GanKWebActivity.this,"复制链接成功");
                }
                break;
            case R.id.open:
                AppUtil.openLink(this,url);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.fab_favorite:
                presenter.meFavorite();
                break;
            case R.id.fab_top:
                webView.scrollTo(0,0);
                break;
        }
    }


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
    public GanKFavorite getFavoriteData() {
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
