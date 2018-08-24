package com.ns.yc.lifehelper.ui.other.douban.douMovie.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.utils.AppUtil;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣电影H5外部页面
 * 修订历史：
 * ================================================
 */
public class MovieWebViewActivity extends BaseActivity {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.video_fullView)
    FrameLayout videoFullView;
    @BindView(R.id.ll_web_view)
    LinearLayout llWebView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String alt;
    private String name;
    private MyWebChromeClient webChromeClient;

    @Override
    protected void onResume() {
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
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (webChromeClient.inCustomView()) {
                hideCustomView();
                return true;
            } else if (mWebView.canGoBack()) {
                //返回上一页面
                mWebView.goBack();
                return true;
            } else {
                //退出网页
                mWebView.loadUrl("about:blank");
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
        if(alt==null || alt.length()==0){
            alt = "about:blank";
        }

        switch (item.getItemId()) {
            case R.id.share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cope:
                AppUtil.copy(alt);
                ToastUtils.showShort("复制成功");
                break;
            case R.id.open:
                AppUtil.openLink(this, alt);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getContentView() {
        return R.layout.base_video_web_view;
    }

    @Override
    public void initView() {
        videoFullView.setVisibility(View.GONE);
        llWebView.setVisibility(View.VISIBLE);
        initToolBar();
        initIntentData();
        initWebView();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initIntentData() {
        Intent intent = getIntent();
        alt = intent.getStringExtra("alt");
        name = intent.getStringExtra("name");
        toolbarTitle.setText(name);
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        mWebView.loadUrl(alt);
    }

    private void initWebView() {
        WebSettings ws = mWebView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 缩放比例 1
        mWebView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true);
        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(webChromeClient = new MyWebChromeClient());
    }

    /**
     * 上传图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 201) {
            webChromeClient.mUploadMessageForAndroid5(intent, resultCode);
        }
    }

    /**
     * 全屏时按返加键执行退出全屏方法，切换为竖屏
     */
    public void hideCustomView() {
        webChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    /**
     * 监听网页链接:
     * 优酷视频直接跳到自带浏览器
     * 根据标识:打电话、发短信、发邮件
     * 添加javascript监听
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://v.youku.com/")) {
                //优酷视频跳转浏览器播放
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addCategory("android.intent.category.BROWSABLE");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                MovieWebViewActivity.this.startActivity(intent);
            } else if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
                //电话、短信、邮箱
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    MovieWebViewActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException ignored) {

                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            addImageClickListener();
            super.onPageFinished(view, url);
        }


        // 视频全屏播放按返回页面被放大的问题
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            if (newScale - oldScale > 7) {
                view.setInitialScale((int) (oldScale / newScale * 100));    //异常放大，缩回去。
            }
        }
    }

    /**
     * 相关配置
     * 播放网络视频配置
     * 上传图片(兼容)
     */
    private class MyWebChromeClient extends WebChromeClient {

        private View progressVideo;
        private View customView;
        private CustomViewCallback customViewCallback;
        private ValueCallback<Uri[]> mUploadMessageForAndroid5;
        private ValueCallback<Uri> mUploadMessage;

        @Override
        public void onReceivedTitle(WebView view, String title) {
            toolbarTitle.setText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb.setProgress(newProgress);
            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
            }
        }

        @Override
        public View getVideoLoadingProgressView() {
            if (progressVideo == null) {
                LayoutInflater inflater = LayoutInflater.from(MovieWebViewActivity.this);
                progressVideo = inflater.inflate(R.layout.layout_progress_loading, null);
            }
            return progressVideo;
        }

        // 播放网络视频时全屏会被调用的方法，播放视频切换为横屏
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            MovieWebViewActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (customView != null) {
                callback.onCustomViewHidden();
                return;
            }

            fullViewAddView(view);
            customView = view;
            customViewCallback = callback;

            videoFullView.setVisibility(View.VISIBLE);
            llWebView.setVisibility(View.GONE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (customView == null)// 不是全屏播放状态
                return;
            MovieWebViewActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            customView.setVisibility(View.GONE);
            if (MovieWebViewActivity.this.getVideoFullView() != null) {
                MovieWebViewActivity.this.getVideoFullView().removeView(customView);
            }
            customView = null;
            customViewCallback.onCustomViewHidden();

            videoFullView.setVisibility(View.GONE);
            llWebView.setVisibility(View.VISIBLE);
        }


        // For Android > 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
            openFileChooserImplForAndroid5(uploadMsg);
            return true;
        }

        private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
            mUploadMessageForAndroid5 = uploadMsg;
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");

            MovieWebViewActivity.this.startActivityForResult(chooserIntent, 201);
        }

        /**
         * 5.0以下 上传图片成功后的回调
         */
        public void mUploadMessage(Intent intent, int resultCode) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }

        /**
         * 5.0以上 上传图片成功后的回调
         */
        public void mUploadMessageForAndroid5(Intent intent, int resultCode) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }

        /**
         * 判断是否是全屏
         */
        public boolean inCustomView() {
            return (customView != null);
        }

    }

    public FrameLayout getVideoFullView() {
        return videoFullView;
    }

    private void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        videoFullView = new FullscreenHolder(MovieWebViewActivity.this);
        videoFullView.addView(view);
        decor.addView(videoFullView);
    }

    private class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }
    }

    private void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        mWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        mWebView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }


}
