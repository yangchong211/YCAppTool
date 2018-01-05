package com.ns.yc.lifehelper.ui.main.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/2/18
 * 描    述：关于我的页面
 * 修订历史：
 * ================================================
 */
public class AboutMeActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_right_img)
    ImageView ivRightImg;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ig_avatar_drakeet)
    ImageView igAvatarDrakeet;
    @Bind(R.id.drakeet)
    TextView drakeet;
    @Bind(R.id.ig_avatar_daimajia)
    ImageView igAvatarDaimajia;
    @Bind(R.id.daimajia)
    TextView daimajia;
    @Bind(R.id.main_content)
    LinearLayout mainContent;

    @Override
    public int getContentView() {
        return R.layout.activity_about_me;
    }

    @Override
    public void initView() {
        initToolBar();
    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbarTitle.setText("关于我");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
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
        }
    }
}
