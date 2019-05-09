package com.ycbjie.library.web.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.R;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.ycbjie.library.db.cache.CacheZhLike;
import com.ycbjie.library.db.realm.RealmDbHelper;
import com.ycbjie.library.utils.AppUtils;
import com.ycbjie.library.utils.DoShareUtils;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/12/5
 *     desc  : 外部链接跳转的页面
 *     revise: 腾讯x5技术
 * </pre>
 */
public class WebViewQQActivity extends BaseActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private WebView webView;
    private ProgressBar pb;
    private LinearLayout llFab;
    private FloatingActionButton fabFavorite;
    private FloatingActionButton fabTop;

    private String title,url,imgUrl,id;
    private int type;
    private View mErrorView;

    @Override
    public int getContentView() {
        return R.layout.base_x5_web_view;
    }


    @Override
    public void initView() {
        initFindViewById();
        initToolBar();
        initIntentData();
        initWebView();
        setLikeState(RealmDbHelper.getInstance().queryLikeId(id));
    }

    private void initFindViewById() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        webView = findViewById(R.id.webView);
        pb = findViewById(R.id.pb);
        llFab = findViewById(R.id.ll_fab);
        fabFavorite = findViewById(R.id.fab_favorite);
        fabTop =  findViewById(R.id.fab_top);
    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else if (i == R.id.fab_favorite) {
            collectNews();
        } else if (i == R.id.fab_top) {
            webView.scrollTo(0, 0);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (webView.canGoBack()) {
                //返回上一页面
                webView.goBack();
                return true;
            } else {
                //退出网页
                webView.loadUrl("about:blank");
                finish();
            }
        }
        return false;
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
            DoShareUtils.shareText(this, url, title);
        } else if (i == R.id.collect) {
            ToastUtils.showRoundRectToast("后期添加");
        } else if (i == R.id.cope) {
            AppUtils.copy(url);
            ToastUtils.showRoundRectToast("复制成功");
        } else if (i == R.id.open) {
            AppUtils.openLink(this, url);
        } else if (i == R.id.capture) {

        } else {

        }
        return super.onOptionsItemSelected(item);
    }


    boolean isLiked;
    private void collectNews() {
        if(isLiked) {
            fabFavorite.setBackgroundResource(R.drawable.ic_gank_un_favorite);
            RealmDbHelper.getInstance().deleteLikeBean(this.id);
            isLiked = false;
        } else {
            fabFavorite.setBackgroundResource(R.drawable.ic_gank_favorite);
            CacheZhLike bean = new CacheZhLike();
            bean.setId(this.id);
            bean.setImage(imgUrl);
            bean.setUrl(url);
            bean.setTitle(title);
            bean.setType(type);
            bean.setTime(System.currentTimeMillis());
            RealmDbHelper.getInstance().insertLikeBean(bean);
            isLiked = true;
        }
    }


    private void setLikeState(boolean state) {
        if(state) {
            fabFavorite.setBackgroundResource(R.drawable.ic_gank_favorite);
            isLiked = true;
        } else {
            fabFavorite.setBackgroundResource(R.drawable.ic_gank_un_favorite);
            isLiked = false;
        }
    }


    @SuppressWarnings("deprecation")
    private void initIntentData() {
        Intent intent = getIntent();
        type = intent.getExtras().getInt(Constant.DetailKeys.IT_DETAIL_TYPE);
        title = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_TITLE);
        url = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_URL);
        imgUrl = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_IMG_URL);
        id = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_ID);
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        //设置是否有图片展示
        //网页在加载的时候暂时不加载图片，当所有的HTML标签加载完成时
        //在加载图片具体的做法如下初始化webView的时候设置不加载图
        settings.setBlockNetworkImage(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        if (NetworkUtils.isConnected()) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (pb == null) {
                return;
            }
            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
            } else {
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(newProgress);
            }
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            toolbarTitle.setText(title);
            if (title.contains("404") || title.contains("网页无法打开")){
                showErrorPage();
            }
        }
    }



    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            LogUtils.e("WebViewActivity-----onPageFinished-------"+url);
            //在html标签加载完成之后在加载图片内容
            webView.getSettings().setBlockNetworkImage(false);
        }
    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    boolean mIsErrorPage;
    private void showErrorPage() {
        FrameLayout webParentView = (FrameLayout) webView.getParent();
        initErrorPage();//初始化自定义页面
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
        @SuppressWarnings("deprecation")
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
        webParentView.addView(webView, 0, lp);
        mIsErrorPage = true;
    }


    /**
     * 显示加载失败时自定义的网页
     */
    protected void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.view_custom_data_error, null);
            LinearLayout ll_error_view = mErrorView.findViewById(R.id.ll_error_view);
            ll_error_view.setOnClickListener(v -> {
                if(NetworkUtils.isConnected()){
                    //如果有网络，则刷新页面
                    webView.reload();
                    recreate();
                }else {
                    //没有网络，不处理
                    ToastUtils.showToast("请检查是否连上网络");
                }
            });
            mErrorView.setOnClickListener(null);
        }
    }


    public static class Builder {

        private String title;
        private String url;
        private String imgUrl;
        private String id;
        private int type;
        private View shareView;
        private Context mContext;

        public Builder() {}

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void toWhere(Builder builder) {
        if (builder.shareView != null) {
            Intent intent = new Intent();
            intent.setClass(builder.mContext, WebViewQQActivity.class);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_URL, builder.url);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_IMG_URL, builder.imgUrl);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_TITLE, builder.title);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_ID, builder.id);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_TYPE, builder.type);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) builder.mContext, builder.shareView, "shareView");
            builder.mContext.startActivity(intent,options.toBundle());
        } else {
            Intent intent = new Intent();
            intent.setClass(builder.mContext, WebViewQQActivity.class);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_URL, builder.url);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_IMG_URL, builder.imgUrl);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_TITLE, builder.title);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_ID, builder.id);
            intent.putExtra(Constant.DetailKeys.IT_DETAIL_TYPE, builder.type);
            builder.mContext.startActivity(intent);
        }
    }


}
