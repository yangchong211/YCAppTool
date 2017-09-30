package com.ns.yc.lifehelper.ui.other.toDo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.toDo.view.ToDoAddActivity;
import com.ns.yc.lifehelper.ui.other.toDo.view.ToDoTimerFragment;

import java.util.ArrayList;
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
public class ToDoTimerActivity extends BaseActivity implements View.OnClickListener {

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

    private List<Fragment> fragments = new ArrayList<>();
    private String[] titleLists = {"周一","周二","周三","周四","周五","周六","周日"};

    @Override
    public int getContentView() {
        return R.layout.activity_to_do_main;
    }


    @Override
    public void initView() {
        initToolBar();
        initTabLayoutAndVp();
    }


    @Override
    public void initListener() {
        fabEdit.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }

    private void initToolBar() {
        setSupportActionBar(toolBar);
        toolBar.setTitle("时光日志");
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                ToastUtils.showShortSafe("设置");
                break;
        }
        return true;
    }


    private void initTabLayoutAndVp() {
        List<String> title = new ArrayList<>();
        for (int a=0 ; a<titleLists.length ; a++) {
            fragments.add(ToDoTimerFragment.newInstance(titleLists[a]));
            title.add(titleLists[a]);
            tab.addTab(tab.newTab().setText(titleLists[a]));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_edit:
                toAddNote();
                break;
        }
    }

    /**
     * 添加新笔记
     */
    private void toAddNote() {
        int selectedTabPosition = tab.getSelectedTabPosition();
        String weekDate = "";
        switch (selectedTabPosition){
            case 0:
                weekDate = titleLists[0];
                break;
            case 1:
                weekDate = titleLists[1];
                break;
            case 2:
                weekDate = titleLists[2];
                break;
            case 3:
                weekDate = titleLists[3];
                break;
            case 4:
                weekDate = titleLists[4];
                break;
            case 5:
                weekDate = titleLists[5];
                break;
            case 6:
                weekDate = titleLists[6];
                break;
        }
        Intent intent = new Intent(this, ToDoAddActivity.class);
        intent.putExtra("type",selectedTabPosition);
        intent.putExtra("weekDate",weekDate);
        intent.putExtra("from","new");
        startActivityForResult(intent,100,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==100){
                int type = data.getExtras().getInt("type");
                //int extra = data.getIntExtra("type", 0);
                ToDoTimerFragment fragment = ToDoTimerFragment.newInstance(titleLists[type]);
                fragment.addDataNotify();
            }
        }
    }
}
