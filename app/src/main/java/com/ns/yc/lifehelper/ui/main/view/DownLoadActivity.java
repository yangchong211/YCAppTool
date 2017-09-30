package com.ns.yc.lifehelper.ui.main.view;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.utils.PermissionsUtils;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12.
 * 描    述：下载页面
 * 修订历史：
 * ================================================
 */
public class DownLoadActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_update)
    TextView tvUpdate;

    @Override
    public int getContentView() {
        return R.layout.activity_download_view;
    }

    @Override
    public void initView() {
        PermissionsUtils.verifyStoragePermissions(this);
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("版本更新");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
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
            case R.id.tv_update:

                break;
        }
    }



}
