package com.ns.yc.lifehelper.ui.other.myMusic.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.myMusic.fragment.MusicLocalDetailFragment;
import com.ns.yc.lifehelper.ui.other.myMusic.weight.OutPlayerController;

import java.lang.reflect.Method;
import java.util.ArrayList;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐，本地音乐详情页面
 * 修订历史：
 * ================================================
 */
public class MyMusicLocalActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.player_controller)
    OutPlayerController playerController;

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_music_scan_menu, menu);
        return true;
    }

    //解决menu中图标不显示的问题
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_local:

                break;
            case R.id.scan_music:

                break;
            case R.id.sort:

                break;
            case R.id.cover_lyric:

                break;
            case R.id.upgrade_quality:

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_my_music_local;
    }

    @Override
    public void initView() {
        initToolbar();
        initFragmentList();
        initViewPager();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //设置显示返回箭头和customView
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setTitle("本地音乐");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initFragmentList() {
        mFragments.clear();
        mTitleList.clear();
        mTitleList.add("单曲");
        mTitleList.add("歌手");
        mTitleList.add("专辑");
        mTitleList.add("文件夹");
        mFragments.add(MusicLocalDetailFragment.newInstance("单曲"));
        mFragments.add(MusicLocalDetailFragment.newInstance("歌手"));
        mFragments.add(MusicLocalDetailFragment.newInstance("专辑"));
        mFragments.add(MusicLocalDetailFragment.newInstance("文件夹"));
    }


    private void initViewPager() {
        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(),mFragments,mTitleList));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }


}
