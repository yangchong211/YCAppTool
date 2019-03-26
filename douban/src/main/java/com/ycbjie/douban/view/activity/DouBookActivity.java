package com.ycbjie.douban.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.douban.R;
import com.ycbjie.douban.view.fragment.DouBookFragment;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.base.mvp.BaseActivity;

import java.util.ArrayList;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣读书页面
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_DOU_BOOK_ACTIVITY)
public class DouBookActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    TabLayout tabLayout;
    ViewPager vpContent;

    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tabLayout = findViewById(R.id.tab_layout);
        vpContent = findViewById(R.id.vp_content);

        initToolBar();
        initFragmentList();
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
        int i = view.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        }
    }


    private void initFragmentList() {
        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
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

        /*
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager,
                mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(5);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpContent);
    }



}
