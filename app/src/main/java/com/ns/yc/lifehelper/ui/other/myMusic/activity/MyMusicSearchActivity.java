package com.ns.yc.lifehelper.ui.other.myMusic.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;

import org.yczbj.ycrefreshviewlib.YCRefreshView;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐搜索页面
 * 修订历史：
 * ================================================
 */
public class MyMusicSearchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;

    @Override
    public int getContentView() {
        return R.layout.base_refresh_recycle_bar;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    private void initToolBar() {
        llSearch.setVisibility(View.VISIBLE);
        llSearch.setOnClickListener(this);
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initListener() {

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
            case R.id.ll_search:
                startActivity(MyMusicRippleActivity.class);
                break;
        }
    }
}
