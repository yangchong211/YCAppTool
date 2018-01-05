package com.ns.yc.lifehelper.ui.main.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.main.contract.WebViewAnimContract;
import com.ns.yc.lifehelper.ui.main.presenter.WebViewAnimPresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhihuDetailBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDetailExtraBean;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.activity.ZhiHuCommentActivity;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.DoShareUtils;
import com.ns.yc.lifehelper.utils.HtmlUtil;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/30V
 * 描    述：可动画的详情页面，用腾讯x5库的webView
 * 修订历史：
 * ================================================
 */
public class WebViewAnimActivity extends BaseActivity implements WebViewAnimContract.View, View.OnClickListener {

    @Bind(R.id.iv_bar_image)
    ImageView ivBarImage;
    @Bind(R.id.tv_bar_copyright)
    TextView tvBarCopyright;
    @Bind(R.id.view_toolbar)
    Toolbar viewToolbar;
    @Bind(R.id.clp_toolbar)
    CollapsingToolbarLayout clpToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.wv_detail_content)
    WebView wvDetailContent;
    @Bind(R.id.nsv_scroller)
    NestedScrollView nsvScroller;
    @Bind(R.id.tv_detail_bottom_like)
    TextView tvDetailBottomLike;
    @Bind(R.id.tv_detail_bottom_comment)
    TextView tvDetailBottomComment;
    @Bind(R.id.tv_detail_bottom_share)
    TextView tvDetailBottomShare;
    @Bind(R.id.ll_detail_bottom)
    FrameLayout llDetailBottom;
    @Bind(R.id.fab_like)
    FloatingActionButton fabLike;

    private WebViewAnimContract.Presenter presenter = new WebViewAnimPresenter(this);

    private int id;
    private String imgUrl;
    private String shareUrl;
    private boolean isBottomShow = true;
    private boolean isTransitionEnd = false;
    private boolean isImageShow = false;
    boolean isNotTransition = false;
    private int allNum = 0;
    private int shortNum = 0;
    private int longNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.bindView(this);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        if (wvDetailContent != null) {
            //wvDetailContent.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wvDetailContent.clearHistory();
            ViewGroup parent = (ViewGroup) wvDetailContent.getParent();
            if (parent != null) {
                parent.removeView(wvDetailContent);
            }
            wvDetailContent.destroy();
            wvDetailContent = null;
        }
        super.onDestroy();
        presenter.unSubscribe();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (wvDetailContent.canGoBack()) {
                //返回上一页面
                wvDetailContent.goBack();
                return true;
            } else {
                //退出网页
                wvDetailContent.loadUrl("about:blank");
                finish();
            }
        }
        return false;
    }


    @Override
    public int getContentView() {
        return R.layout.base_anim_web_view;
    }

    @Override
    public void initView() {
        setToolBar(viewToolbar,"");
        initIntent();
        initWebView();
        initScroll();
    }


    private void initIntent() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
    }


    @Override
    public void initListener() {
        initWindowListener();
        tvDetailBottomComment.setOnClickListener(this);
        tvDetailBottomShare.setOnClickListener(this);
        fabLike.setOnClickListener(this);
    }


    @Override
    public void initData() {
        presenter.getDetailData(id);
        presenter.getExtraData(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_detail_bottom_comment:
                goToComment();
                break;
            case R.id.tv_detail_bottom_share:
                DoShareUtils.shareImage(this,imgUrl);
                break;
            case R.id.fab_like:
                collectNews();
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_web_view_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url = "https://github.com/yangchong211";
        if(id==0){
            url = "about:blank";
            wvDetailContent.loadUrl("about:blank");
        }

        switch (item.getItemId()) {
            case R.id.share:
                DoShareUtils.shareText(this,url,"潇湘剑雨");
                break;
            case R.id.collect:
                /*if(Constant.isLogin){
                    goToCollect();
                }else {
                    startActivity(MeLoginActivity.class);
                }*/
                ToastUtils.showShortSafe("后期添加");
                break;
            case R.id.cope:
                AppUtil.copy("");
                ToastUtils.showShortSafe("复制成功");
                break;
            case R.id.open:
                AppUtil.openLink(this, "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void collectNews() {
        if (fabLike.isSelected()) {
            fabLike.setSelected(false);
            presenter.deleteLikeData();
        } else {
            fabLike.setSelected(true);
            presenter.insertLikeData();
        }
    }

    private void goToComment() {
        Intent intent = getIntent();
        intent.setClass(this,ZhiHuCommentActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("allNum",allNum);
        intent.putExtra("shortNum",shortNum);
        intent.putExtra("longNum",longNum);
        startActivity(intent);
    }


    private void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initScroll() {
        nsvScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX,
                                       int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY - oldScrollY > 0 && isBottomShow) {  //下移隐藏
                    isBottomShow = false;
                    llDetailBottom.animate().translationY(llDetailBottom.getHeight());
                } else if(scrollY - oldScrollY < 0 && !isBottomShow){    //上移出现
                    isBottomShow = true;
                    llDetailBottom.animate().translationY(0);
                }
            }
        });
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = wvDetailContent.getSettings();
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
        wvDetailContent.setWebViewClient(new MyWebViewClient());
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    private void initWindowListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (getWindow().getSharedElementEnterTransition()).addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }
                @Override
                public void onTransitionEnd(Transition transition) {
                    /**
                     * 测试发现部分手机(如红米note2)上加载图片会变形,没有达到centerCrop效果
                     * 查阅资料发现Glide配合SharedElementTransition是有坑的,需要在Transition动画结束后再加载图片
                     */
                    isTransitionEnd = true;
                    if (imgUrl != null) {
                        isImageShow = true;
                        ImageUtils.loadImgByPicasso(WebViewAnimActivity.this, imgUrl, ivBarImage);
                    }
                }
                @Override
                public void onTransitionCancel(Transition transition) {}
                @Override
                public void onTransitionPause(Transition transition) {}
                @Override
                public void onTransitionResume(Transition transition) {}
            });
        }
    }



    @Override
    public void showContent(ZhihuDetailBean zhihuDetailBean) {
        imgUrl = zhihuDetailBean.getImage();
        shareUrl = zhihuDetailBean.getShare_url();
        if (isNotTransition) {
            ImageUtils.loadImgByPicasso(this, zhihuDetailBean.getImage(), ivBarImage);
        } else {
            if (!isImageShow && isTransitionEnd) {
                ImageUtils.loadImgByPicasso(this, zhihuDetailBean.getImage(), ivBarImage);
            }
        }
        clpToolbar.setTitle(zhihuDetailBean.getTitle());
        tvBarCopyright.setText(zhihuDetailBean.getImage_source());
        String htmlData = HtmlUtil.createHtmlData(zhihuDetailBean.getBody(),zhihuDetailBean.getCss(),zhihuDetailBean.getJs());
        wvDetailContent.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void showExtraInfo(ZhiHuDetailExtraBean zhiHuDetailExtraBean) {
        tvDetailBottomLike.setText(String.format("%d个赞",zhiHuDetailExtraBean.getPopularity()));
        tvDetailBottomComment.setText(String.format("%d条评论",zhiHuDetailExtraBean.getComments()));
        allNum = zhiHuDetailExtraBean.getComments();
        shortNum = zhiHuDetailExtraBean.getShort_comments();
        longNum = zhiHuDetailExtraBean.getLong_comments();
    }


}
