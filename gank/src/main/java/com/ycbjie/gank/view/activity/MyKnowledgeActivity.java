package com.ycbjie.gank.view.activity;

import android.content.Intent;
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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.gank.R;
import com.ycbjie.gank.view.fragment.KnowledgeVideoFragment;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.gank.view.fragment.KnowledgeAndroidFragment;
import com.ycbjie.gank.view.fragment.KnowledgeCustomFragment;
import com.ycbjie.gank.view.fragment.KnowledgeOtherFragment;
import com.ns.yc.ycutilslib.fragmentBack.BackHandlerHelper;
import java.util.ArrayList;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/3/28
 *     desc  : 我的干货页面
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_GANK_KNOWLEDGE_ACTIVITY)
public class MyKnowledgeActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    TabLayout tabLayout;
    ViewPager vpContent;


    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        //避免切换横竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public int getContentView() {
        return R.layout.base_tab_view;
    }

    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        tabLayout = findViewById(R.id.tab_layout);
        vpContent = findViewById(R.id.vp_content);

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
        int i = view.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else if (i == R.id.ll_search) {
            startActivity(new Intent(this,GanKSearchActivity.class));
        }
    }

    private void initFragmentList() {
        mTitleList.clear();
        mFragments.clear();
        mTitleList.add("干货定制");
        mTitleList.add("Android");
        mTitleList.add("生活福利");
        mTitleList.add("休息视频");
        mFragments.add(new KnowledgeCustomFragment());
        mFragments.add(new KnowledgeAndroidFragment());
        mFragments.add(new KnowledgeOtherFragment());
        mFragments.add(new KnowledgeVideoFragment());
    }



    private void initViewPagerAndTab() {
        /*
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
