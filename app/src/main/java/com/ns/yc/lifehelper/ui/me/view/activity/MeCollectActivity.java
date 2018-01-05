package com.ns.yc.lifehelper.ui.me.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.me.view.fragment.MeDocCollectFragment;
import com.ns.yc.lifehelper.ui.me.view.fragment.MeGanKCollectFragment;
import com.ns.yc.lifehelper.ui.me.view.fragment.MeNewsCollectFragment;
import com.ns.yc.lifehelper.ui.me.view.fragment.MePicCollectFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12
 * 描    述：我的收藏页面
 * 修订历史：
 * ================================================
 */
public class MeCollectActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.fl_title_menu)
    FrameLayout flTitleMenu;
    @Bind(R.id.stl_table)
    SlidingTabLayout stlTable;
    @Bind(R.id.vp_content)
    ViewPager vpContent;

    private List<String> list = new ArrayList<>();
    private final String[] mTitles = {"干货", "新闻", "图片","文件"};

    @Override
    public int getContentView() {
        return R.layout.activity_me_collect;
    }

    @Override
    public void initView() {
        initViewPager();
        initTabLayout();
    }

    @Override
    public void initListener() {
        flTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_title_menu:
                finish();
                break;
        }
    }


    private void initViewPager() {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mFragments.clear();
        mFragments.add(new MeGanKCollectFragment());
        mFragments.add(new MeNewsCollectFragment());
        mFragments.add(new MePicCollectFragment());
        mFragments.add(new MeDocCollectFragment());
        list.clear();
        for(int a=0 ; a<mTitles.length ; a++){
            list.add(mTitles[a]);
        }
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(),mFragments,list);
        vpContent.setAdapter(adapter);
        //限制预加载页面为4，这一步至关重要
        vpContent.setOffscreenPageLimit(4);
    }

    private void initTabLayout() {
        stlTable.setViewPager(vpContent);
        stlTable.setCurrentTab(0);
        stlTable.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


}
