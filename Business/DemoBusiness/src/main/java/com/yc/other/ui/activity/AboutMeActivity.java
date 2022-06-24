package com.yc.other.ui.activity;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yc.library.base.config.Constant;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.other.R;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/9/11
 *     desc  : 关于我的页面
 *     revise:
 * </pre>
 */
public class AboutMeActivity extends BaseActivity implements View.OnClickListener {


    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private Toolbar toolbar;
    private String title;

    @Override
    public int getContentView() {
        return R.layout.activity_about_me;
    }

    @Override
    public void initView() {
        initFindById();
        initToolBar();
        initIntentData();
    }

    private void initFindById() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
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
        if (intent!=null){
            title = intent.getStringExtra(Constant.TITLE);
            if (title!=null){
                toolbarTitle.setText(title);
            }else {
                toolbarTitle.setText("关于我");
            }
        }
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
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        }
    }
}
