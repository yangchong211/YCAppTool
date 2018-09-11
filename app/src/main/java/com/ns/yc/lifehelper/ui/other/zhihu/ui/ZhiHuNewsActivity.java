package com.ns.yc.lifehelper.ui.other.zhihu.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuDailyFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuHotFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuSectionFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuThemeFragment;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/29
 *     desc  : 知乎日报模块
 *     revise:
 * </pre>
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

    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
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
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            default:
                break;
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
