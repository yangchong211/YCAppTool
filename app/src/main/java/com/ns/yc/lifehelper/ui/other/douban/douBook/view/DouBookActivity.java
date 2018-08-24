package com.ns.yc.lifehelper.ui.other.douban.douBook.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.douban.douBook.view.fragment.DouBookFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣读书页面
 * 修订历史：
 * ================================================
 */
public class DouBookActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mFragments;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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
        toolbarTitle.setText("豆瓣读书");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            default:
                break;
        }
    }


    private void initFragmentList() {
        mTitleList = new ArrayList<>();
        mFragments = new ArrayList<>();
        mTitleList.add("综合");
        mTitleList.add("文学");
        mTitleList.add("文化");
        mTitleList.add("生活");
        mTitleList.add("励志");
        mFragments.add(DouBookFragment.newInstance("综合"));
        mFragments.add(DouBookFragment.newInstance("文学"));
        mFragments.add(DouBookFragment.newInstance("文化"));
        mFragments.add(DouBookFragment.newInstance("生活"));
        mFragments.add(DouBookFragment.newInstance("励志"));
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
        vpContent.setOffscreenPageLimit(5);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpContent);
    }


}
