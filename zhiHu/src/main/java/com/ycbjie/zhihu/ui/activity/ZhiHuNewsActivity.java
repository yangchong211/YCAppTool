package com.ycbjie.zhihu.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SizeUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.ui.fragment.ZhiHuDailyFragment;
import com.ycbjie.zhihu.ui.fragment.ZhiHuHotFragment;
import com.ycbjie.zhihu.ui.fragment.ZhiHuSectionFragment;
import com.ycbjie.zhihu.ui.fragment.ZhiHuThemeFragment;

import java.util.ArrayList;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/29
 *     desc  : 知乎日报模块
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_ZHIHU_ACTIVITY)
public class ZhiHuNewsActivity extends BaseActivity implements View.OnClickListener {

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
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        }
    }


    private void initFragmentList() {
        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mTitleList.add("日报");
        mTitleList.add("主题");
        mTitleList.add("专栏");
        mTitleList.add("热门");
        mFragments.add(new ZhiHuDailyFragment());
        mFragments.add(new ZhiHuThemeFragment());
        mFragments.add(new ZhiHuSectionFragment());
        mFragments.add(new ZhiHuHotFragment());
        /*
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
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorTheme),
                getResources().getColor(R.color.redTab));
        tabLayout.setSelectedTabIndicatorHeight(SizeUtils.dp2px(1));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorTheme));
    }


}
