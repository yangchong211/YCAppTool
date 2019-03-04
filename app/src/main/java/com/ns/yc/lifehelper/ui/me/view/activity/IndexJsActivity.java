package com.ns.yc.lifehelper.ui.me.view.activity;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ycbjie.library.base.mvp.BaseActivity;
/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/12
 *     desc  : js
 *     revise:
 * </pre>
 */
public class IndexJsActivity extends BaseActivity {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;


    @Override
    public int getContentView() {
        return R.layout.base_web_view;
    }

    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("客户端测试JS接口");
        llTitleMenu.setOnClickListener(v -> finish());
        WebView webView = findViewById(R.id.webView);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
