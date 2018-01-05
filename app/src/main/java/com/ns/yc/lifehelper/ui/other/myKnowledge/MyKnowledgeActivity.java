package com.ns.yc.lifehelper.ui.other.myKnowledge;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.myKnowledge.fragment.KnowledgeAndroidFragment;
import com.ns.yc.lifehelper.ui.other.myKnowledge.fragment.KnowledgeCustomFragment;
import com.ns.yc.lifehelper.ui.other.myKnowledge.fragment.KnowledgeOtherFragment;
import com.ns.yc.ycutilslib.fragmentBack.BackHandlerHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货页面
 * 修订历史：
 * ================================================
 */
public class MyKnowledgeActivity extends FragmentActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_content)
    ViewPager vpContent;


    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);              //避免切换横竖屏
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public int getContentView() {
        return R.layout.base_tab_view;
    }

    public void initView() {
        initToolBar();
        initFragmentList();
        initViewPagerAndTab();
    }

    private void initToolBar() {
        toolbarTitle.setText("干货集中营");
        llSearch.setVisibility(View.VISIBLE);
    }


    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        llSearch.setOnClickListener(this);
    }

    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.ll_search:

                break;
        }
    }

    private void initFragmentList() {
        mTitleList.clear();
        mFragments.clear();
        //mTitleList.add("每日推荐");
        mTitleList.add("干货定制");
        mTitleList.add("Android");
        mTitleList.add("生活福利");
        mTitleList.add("休息视频");
        //mFragments.add(new KnowledgeEveryFragment());
        mFragments.add(new KnowledgeCustomFragment());
        mFragments.add(new KnowledgeAndroidFragment());
        mFragments.add(new KnowledgeOtherFragment());
        mFragments.add(new KnowledgeVideoFragment());
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
        vpContent.setOffscreenPageLimit(3);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpContent);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3){
                    backHandled = true;
                }else {
                    backHandled = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private long lastBackPress;
    public boolean backHandled;
    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (System.currentTimeMillis() - lastBackPress < 1000) {
                super.onBackPressed();
            } else {
                lastBackPress = System.currentTimeMillis();
                finish();
            }
        }
    }

}
