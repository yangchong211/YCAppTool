package com.ns.yc.lifehelper.ui.main.view.activity;

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

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.cache.CacheZhLike;
import com.ns.yc.lifehelper.ui.other.zhihu.model.db.RealmHelper;
import com.ns.yc.lifehelper.utils.DoShareUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.Bind;
import io.realm.Realm;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/5
 * 描    述：腾讯x5技术
 * 修订历史：
 * ================================================
 */
public class WebViewQQActivity extends BaseActivity implements View.OnClickListener {


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

    private String title,url,imgUrl,id;
    private int type;
    private Realm realm;

    @Override
    public int getContentView() {
        return R.layout.base_x5_web_view;
    }


    @Override
    public void initView() {
        initRealm();
        initToolBar();
        initIntentData();
        initWebView();
        setLikeState(RealmHelper.queryLikeId(realm , id));
    }


    private void initRealm() {
        if(realm==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
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
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.fab_favorite:
                collectNews();
                break;
            case R.id.fab_top:
                //webView.scrollTo(0,0);
                //webView.scrollBy(0,0);
                break;
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
        getMenuInflater().inflate(R.menu.movie_web_view_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                DoShareUtils.shareText(this ,url,"分享一篇文章");
                break;
            case R.id.collect:
                collectNews();
                break;
            case R.id.cope:

                break;
            case R.id.open:

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    boolean isLiked;
    private void collectNews() {
        if(isLiked) {
            fabFavorite.setBackgroundResource(R.drawable.ic_gank_un_favorite);
            RealmHelper.deleteLikeBean(realm , this.id);
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
            RealmHelper.insertLikeBean(realm , bean);
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


    private void initIntentData() {
        Intent intent = getIntent();
        type = intent.getExtras().getInt(Constant.DetailKeys.IT_DETAIL_TYPE);
        title = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_TITLE);
        url = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_URL);
        imgUrl = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_IMG_URL);
        id = intent.getExtras().getString(Constant.DetailKeys.IT_DETAIL_ID);
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        //设置是否有图片展示
        if (SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.SP_NO_IMAGE,false)) {
            settings.setBlockNetworkImage(true);
        }
        if (SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.SP_AUTO_CACHE,true)) {
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            if (NetworkUtils.isConnected()) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
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
            if (pb == null) return;
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
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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
        private Activity mActivity;

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

        public Builder setAnimConfig(Activity mActivity, View shareView) {
            this.mActivity = mActivity;
            this.shareView = shareView;
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
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(builder.mActivity, builder.shareView, "shareView");
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
