package com.ns.yc.lifehelper.ui.main.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.ycutilslib.scrollView.NestedScrollViewListener;
import com.ns.yc.ycutilslib.scrollView.NoNestedScrollview;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/13
 * 描    述：新闻详情页，带有渐变效果
 * 修订历史：
 * ================================================
 */
public class WebViewChangeActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.scrollView)
    NoNestedScrollview scrollView;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tool_bar)
    Toolbar toolbar;
    private String url;
    private String name;
    private WebSettings ws;
    private int mAlpha;


    @Override
    protected void onStart() {
        super.onStart();
        toolbarTitle.setText("");
        setViewBackgroundAlpha(toolbar, 0);
    }


    @Override
    protected void onPause() {
        super.onPause();
        setViewBackgroundAlpha(toolbar, 255);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_news_content;
    }


    @Override
    public void initView() {
        initIntentData();
        initWebView();
        initScrollView();
    }


    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            url = intent.getStringExtra("url");
            name = intent.getStringExtra("name");
        }else {
            url = "http://baidu.com";
            name = "新闻";
        }
        toolbarTitle.setText(name);
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }


    @Override
    public void initData() {
        webView.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initWebView() {
        ws = webView.getSettings();
        ws.setUseWideViewPort(true);
        ws.setJavaScriptEnabled(true);                                  //设置可以调用js,启用javascript
        ws.setDomStorageEnabled(true);                                  //WebView缓存
        ws.setDefaultTextEncodingName("UTF-8");                         //设置默认为utf-8
        webView.setWebViewClient(new MyWebViewClient());
    }


    private void initScrollView() {
        scrollView.setScrollViewListener(new NestedScrollViewListener() {
            @Override
            public void onScrollChanged(NestedScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 滑动的最小距离（自行定义，you happy jiu ok）
                int minHeight = 50;
                // 滑动的最大距离（自行定义，you happy jiu ok）
                int maxHeight = SizeUtils.dp2px(160);


                if (scrollView.getScrollY() <= minHeight) {
                    // 滑动距离小于定义得最小距离
                    mAlpha = 0;
                } else if (scrollView.getScrollY() > maxHeight) {
                    // 滑动距离大于定义得最大距离
                    mAlpha = 255;
                } else {
                    // 滑动距离处于最小和最大距离之间
                    // （滑动距离 - 开始变化距离）：最大限制距离 = mAlpha ：255
                    mAlpha = (scrollView.getScrollY() - minHeight) * 255 / (maxHeight - minHeight);
                }


                if (mAlpha <= 0) {
                    // 初始状态 标题栏/导航栏透明等
                    setViewBackgroundAlpha(toolbar, 0);
                    toolbar.setTitleTextColor(Color.argb(255, 255, 255, 255));
                    toolbarTitle.setText("");
                } else if (mAlpha >= 255) {
                    // 终止状态：标题栏/导航栏 不在进行变化
                    setViewBackgroundAlpha(toolbar, 255);
                    toolbar.setTitleTextColor(Color.argb(255, 0, 0, 0));
                    toolbarTitle.setText(name);
                } else {
                    // 变化中状态：标题栏/导航栏随ScrollView 的滑动而产生相应变化
                    setViewBackgroundAlpha(toolbar, mAlpha);
                    toolbar.setTitleTextColor(Color.argb(255, 255 - mAlpha, 255 - mAlpha, 255 - mAlpha));
                }
            }

            @Override
            public void onScroll(int scrollY) {

            }
        });
    }

    /**
     * 设置View的背景透明度
     */
    public void setViewBackgroundAlpha(View view, int alpha) {
        if (view == null) return;
        Drawable drawable = view.getBackground();
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url!=null && url.length()>0){
                view.loadUrl(url);
            }
            return true;
        }
    }
}
