package com.ns.yc.lifehelper.ui.other.zhihu.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuDailyFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuHotFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuSectionFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuThemeFragment;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块
 * 修订历史：
 * ================================================
 */
public class ZhiHuNewsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mFragments;


    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        initToolBar();
        initFragmentList();
        initViewPagerAndTab();
    }

    private void initToolBar() {
        toolbarTitle.setText("知乎日报");
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


    private void initFragmentList() {
        mTitleList = new ArrayList<>();
        mFragments = new ArrayList<>();
        mTitleList.add("日报");
        mTitleList.add("主题");
        mTitleList.add("专栏");
        mTitleList.add("热门");
        mFragments.add(new ZhiHuDailyFragment());
        mFragments.add(new ZhiHuThemeFragment());
        mFragments.add(new ZhiHuSectionFragment());
        mFragments.add(new ZhiHuHotFragment());
    }



    private void initViewPagerAndTab() {
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(mFragments.size());
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpContent);
    }



}
