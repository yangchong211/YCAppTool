package com.ns.yc.lifehelper.ui.other.toDo.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.toDo.bean.MainPageItem;
import com.ns.yc.lifehelper.ui.other.toDo.contract.ToDoTimerContract;
import com.ns.yc.lifehelper.ui.other.toDo.presenter.ToDoTimerPresenter;
import com.ns.yc.lifehelper.ui.other.toDo.view.activity.ToDoTimerFragment;
import com.ns.yc.lifehelper.utils.AppUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14.
 * 描    述：时光日志页面
 * 修订历史：
 * ================================================
 */
public class ToDoTimerActivity extends BaseActivity implements View.OnClickListener , ToDoTimerContract.View{

    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.fab_edit)
    FloatingActionButton fabEdit;
    @Bind(R.id.cl_main)
    CoordinatorLayout clMain;

    List<MainPageItem> items = new ArrayList<>();
    List<String> title = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private String[] titleLists = {"周一","周二","周三","周四","周五","周六","周日"};
    private ToDoTimerContract.Presenter presenter = new ToDoTimerPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_to_do_main;
    }


    @Override
    public void initView() {
        initToolBar();
        initTabLayoutAndVp();
    }

    private void initToolBar() {
        setSupportActionBar(toolBar);
        toolBar.setTitle("时光日志");
    }

    @Override
    public void initListener() {
        fabEdit.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return presenter.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_edit:
                presenter.addNote();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化tabLayout
     */
    private void initTabLayoutAndVp() {
        for (int a=0 ; a<titleLists.length ; a++) {
            fragments.add(ToDoTimerFragment.newInstance(titleLists[a]));
            tab.addTab(tab.newTab().setText(titleLists[a]));
            title.add(titleLists[a]);
        }
        tab.setTabGravity(TabLayout.GRAVITY_CENTER);
        tab.setTabTextColors(Color.WHITE,getResources().getColor(R.color.colorAccent));
        tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tab.setSelectedTabIndicatorHeight(SizeUtils.dp2px(1.0f));
        tab.setTabMode(TabLayout.MODE_FIXED);

        FragmentManager fragmentManager = getSupportFragmentManager();
        BasePagerAdapter adapter = new BasePagerAdapter(fragmentManager,fragments,title);
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(0);
        tab.setupWithViewPager(vpPager);
    }


    /**
     * 获取指示器所在位置
     */
    @Override
    public int getCurrentViewPagerItem() {
        return tab.getSelectedTabPosition();
    }

    /**
     * 获取上下文
     */
    @Override
    public Context getActivity() {
        return this;
    }

    /**
     * 跳转
     */
    @Override
    public void startActivityAndForResult(Intent intent, int code) {
        startActivityForResult(intent, code);
    }

    /**
     * 关闭Activity
     */
    @Override
    public void finishActivity() {
        if(AppUtil.isActivityLiving(this)){
            finish();
        }
    }

    /**
     * 获取item
     */
    @Override
    public List<MainPageItem> getPageItem() {
        if (items == null || items.size() != 7) {
            items = new ArrayList<>();
            items.add(new MainPageItem(Calendar.MONDAY, "周一", new ToDoTimerFragment()));
            items.add(new MainPageItem(Calendar.TUESDAY, "周二", new ToDoTimerFragment()));
            items.add(new MainPageItem(Calendar.WEDNESDAY, "周三", new ToDoTimerFragment()));
            items.add(new MainPageItem(Calendar.THURSDAY, "周四", new ToDoTimerFragment()));
            items.add(new MainPageItem(Calendar.FRIDAY, "周五", new ToDoTimerFragment()));
            items.add(new MainPageItem(Calendar.SATURDAY, "周六", new ToDoTimerFragment()));
            items.add(new MainPageItem(Calendar.SUNDAY, "周日", new ToDoTimerFragment()));
        }
        return items;
    }

}
