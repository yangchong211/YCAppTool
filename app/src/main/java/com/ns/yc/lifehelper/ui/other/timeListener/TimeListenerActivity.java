package com.ns.yc.lifehelper.ui.other.timeListener;

import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.timeListener.view.TimeListenerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/1
 * 描    述：时光聆听页面
 * 修订历史：
 * ================================================
 */
public class TimeListenerActivity extends BaseActivity {


    @Bind(R.id.iv_outgoing)
    ImageView ivOutgoing;
    @Bind(R.id.iv_target)
    ImageView ivTarget;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolBar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @Bind(R.id.col)
    CoordinatorLayout col;
    private Constant.CollapsingToolbarLayoutState state;
    private List<String> mTitles;
    private ArrayList<Fragment> mFragments;



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_time_listener_main;
    }

    @Override
    public void initView() {
        initToolBar();
        initFragments();
        initViewPager();
    }

    @Override
    public void initListener() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != Constant.CollapsingToolbarLayoutState.EXPANDED) {
                        state = Constant.CollapsingToolbarLayoutState.EXPANDED;
                        collapsingToolbar.setTitle("");
                        tvTitle.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != Constant.CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbar.setTitle("");
                        state = Constant.CollapsingToolbarLayoutState.COLLAPSED;
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("专题详情");
                    }
                } else {
                    if (state != Constant.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        collapsingToolbar.setTitle("");
                        state = Constant.CollapsingToolbarLayoutState.INTERNEDIATE;
                        tvTitle.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    private void initToolBar() {
        toolBar.inflateMenu(R.menu.menu_time_listener);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.about:
                        Toast.makeText(TimeListenerActivity.this, "关于", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private String[] proName = {"猜谜语", "绕口令", "急转弯", "笑话大全", "股票", "身份证", "人脸识别", "时光聆听", "空气质量", "今日油价", "翻译", "缴费"};
    private String[] mTabs = {"公开", "民谣", "摇滚", "电子", "流行", "爵士", "独立", "故事", "新世纪", "精品推荐", "原声"};
    private void initFragments() {
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();
        for (int a = 0; a < mTabs.length; a++) {
            mTitles.add(mTabs[a]);
            mFragments.add(TimeListenerFragment.getInstance(mTabs[a]));
        }
    }


    private void initViewPager() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitles);
        viewPager.setAdapter(myAdapter);
        // 左右预加载页面的个数
        viewPager.setOffscreenPageLimit(1);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

}
