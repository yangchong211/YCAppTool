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
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.douban.view.fragment.DouMusicFragment;

import java.util.ArrayList;



/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣音乐页面
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_DOU_MUSIC_ACTIVITY)
public class DouMusicActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    TabLayout tabLayout;
    ViewPager vpContent;

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

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
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        vpContent = (ViewPager) findViewById(R.id.vp_content);

        initToolBar();
        initFragmentList();
        initViewPagerAndTab();
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    private void initToolBar() {
        toolbarTitle.setText("豆瓣音乐");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ll_title_menu) {
            finish();

        } else {
        }
    }


    private void initFragmentList() {
        mTitleList.add("经典");
        mTitleList.add("流行");
        mTitleList.add("韩系");
        mTitleList.add("欧美");
        mFragments.add(DouMusicFragment.newInstance("经典"));
        mFragments.add(DouMusicFragment.newInstance("流行"));
        mFragments.add(DouMusicFragment.newInstance("韩系"));
        mFragments.add(DouMusicFragment.newInstance("欧美"));
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
