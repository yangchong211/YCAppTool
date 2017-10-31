package com.ns.yc.lifehelper.ui.other.nicePic.meiZhi;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;

import butterknife.Bind;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/30
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MeiZhiActivity extends BaseActivity {


    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bga_refresh)
    BGARefreshLayout bgaRefresh;

    @Override
    public int getContentView() {
        return R.layout.base_bga_recycle_header;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("妹纸图");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


}
