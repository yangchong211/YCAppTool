package com.ycbjie.ycwebview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.net.AppNetworkUtils;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/17
 *     desc  : webView页面
 *     revise: 暂时先用假数据替代
 * </pre>
 */
public class WebViewActivity extends AppCompatActivity {

    private X5WebView mWebView;
    private WebProgress progress;
    private String url;
    private boolean hide = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() ==
                KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mWebView.canGoBack()) {
                //退出网页
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && hide) {
            hideSysBar();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initData();
        initView();
    }

    public void initData() {
        Intent intent = getIntent();
        if (intent!=null){
            url = intent.getStringExtra("url");
            hide = intent.getBooleanExtra("hide",false);
            if (hide){
                hideSysBar();
            }
        }
    }

    private void hideSysBar() {
        if (getWindow()==null){
            return;
        }
        View decorView = this.getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void hideSystemUI() {
        if (getWindow()==null){
            return;
        }
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY //(修改这个选项，可以设置不同模式)
                        //使用下面三个参数，可以使内容显示在system bar的下面，防止system bar显示或
                        //隐藏时，Activity的大小被resize。
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // 隐藏导航栏和状态栏
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void initView() {
        mWebView = findViewById(R.id.web_view);
        progress = findViewById(R.id.progress);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccent),this.getResources().getColor(R.color.colorPrimaryDark));

        //String url = "http://10.3.138.78:8080/wangzhebiyeji";
        if (url==null || url.length()==0){
            url = "https://baidu.com";
            ToastUtils.showRoundRectToast("输入地址不能为空");
        }
        mWebView.loadUrl(url);
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
        if (!AppNetworkUtils.isConnected()){
            ToastUtils.showRoundRectToast("请先连接上网络");
        }
    }


    private DefaultWebListener interWebListener = new DefaultWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
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


    private void test(){
        //夜间模式，enable:true(日间模式)，enable：false（夜间模式）
        mWebView.setDayOrNight(true);
        //前进后退缓存，true表示缓存
        mWebView.setContentCacheEnable(true);
        //对于无法缩放的页面当用户双指缩放时会提示强制缩放，再次操作将触发缩放功能
        mWebView.setForcePinchScaleEnabled(true);
        //设置无痕模式
        mWebView.setShouldTrackVisitedLinks(true);
        //刘海屏适配
        mWebView.setDisplayCutoutEnable(true);
        //一次性删除所有缓存
        mWebView.clearAllWebViewCache(true);
        //缓存清除，针对性删除
        mWebView.clearCache();
    }
}
