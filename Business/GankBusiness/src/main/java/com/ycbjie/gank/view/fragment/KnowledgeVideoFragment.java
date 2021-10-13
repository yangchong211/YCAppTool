package com.ycbjie.gank.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ns.yc.ycutilslib.fragmentBack.BackHandledFragment;
import com.ycbjie.gank.R;
import com.ycbjie.gank.view.activity.MyKnowledgeActivity;
import com.ycbjie.webviewlib.view.X5WebView;
import com.ycbjie.webviewlib.widget.WebProgress;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货页面  生活福利
 * 修订历史：
 * ================================================
 */
public class KnowledgeVideoFragment extends BackHandledFragment {

    private Toolbar toolbar;
    private X5WebView mWebView;
    private WebProgress progress;
    private String url = "https://www.github.com/yangchong211";
    private MyKnowledgeActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container , false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        initData();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyKnowledgeActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroy() {
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


    public int getContentView() {
        return R.layout.base_web_view;
    }

    public void initView(View view) {
        mWebView = view.findViewById(R.id.webView);
        progress =view.findViewById(R.id.progress);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    public void initListener() {

    }

    public void initData() {
        initWebView();
    }


    private void initWebView() {
        progress.show();
        progress.setColor(this.getResources().getColor(com.ycbjie.library.R.color.colorAccent),
                this.getResources().getColor(com.ycbjie.library.R.color.colorPrimaryDark));
        mWebView.loadUrl(url);
    }

    @Override
    public boolean interceptBackPressed() {
        if (activity.backHandled) {
            if(mWebView!=null && mWebView.pageCanGoBack()){
                mWebView.pageGoBack();
                return true;
            }else {
                return false;
            }
        }
        return false;
    }


}
