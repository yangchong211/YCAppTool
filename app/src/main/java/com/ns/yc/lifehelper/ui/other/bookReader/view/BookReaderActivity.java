package com.ns.yc.lifehelper.ui.other.bookReader.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.BookReaderLocalActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.fragment.CommunityFragment;
import com.ns.yc.lifehelper.ui.other.bookReader.view.fragment.ReaderFindFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/18
 * 描    述：小说阅读器煮主页面
 * 修订历史：
 * ================================================
 */
public class BookReaderActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.ll_search)
    FrameLayout llSearch;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.stl_tab)
    SegmentTabLayout stlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] titles = {"书架","社区","发现"};


    @Override
    public int getContentView() {
        return R.layout.activity_book_reader_main;
    }

    @Override
    public void initView() {
        initToolBar();
        initFragment();
        initViewPager();
    }

    private void initToolBar() {
        toolbarTitle.setText("阅读器");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_reader_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                break;
            case R.id.action_login:

                break;
            case R.id.action_my_message:

                break;
            case R.id.action_sync_bookshelf:

                break;
            case R.id.action_scan_local_book:
                startActivity(BookReaderLocalActivity.class);
                break;
            case R.id.action_wifi_book:

                break;
            case R.id.action_feedback:

                break;
            case R.id.action_night_mode:

                break;
            case R.id.action_settings:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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


    private void initFragment() {
        mFragments.clear();
        CommunityFragment recommendFragment = new CommunityFragment();
        CommunityFragment communityFragment = new CommunityFragment();
        ReaderFindFragment readerFindFragment = new ReaderFindFragment();
        mFragments.add(recommendFragment);
        mFragments.add(communityFragment);
        mFragments.add(readerFindFragment);
    }

    private void initViewPager() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(3);
        //myAdapter.notifyDataSetChanged();
        stlTab.setTabData(titles);
        stlTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpContent.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stlTab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpContent.setCurrentItem(1);
    }


    public void setCurrentItem(int position) {
        vpContent.setCurrentItem(position);
    }


}
