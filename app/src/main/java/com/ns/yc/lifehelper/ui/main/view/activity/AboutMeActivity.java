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

import butterknife.BindView;

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


    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.ll_search)
    FrameLayout llSearch;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ig_avatar_drakeet)
    ImageView igAvatarDrakeet;
    @BindView(R.id.drakeet)
    TextView drakeet;
    @BindView(R.id.ig_avatar_daimajia)
    ImageView igAvatarDaimajia;
    @BindView(R.id.daimajia)
    TextView daimajia;
    @BindView(R.id.main_content)
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
