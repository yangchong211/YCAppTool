package com.ns.yc.lifehelper.ui.other.workDo.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.toDo.bean.TaskDetailEntity;
import com.ns.yc.lifehelper.ui.other.workDo.contract.WorkDoContract;
import com.ns.yc.lifehelper.ui.other.workDo.dagger.DaggerUiComponent;
import com.ns.yc.lifehelper.ui.other.workDo.dagger.UiModule;
import com.ns.yc.lifehelper.ui.other.workDo.presenter.WorkDoPresenter;
import com.ns.yc.lifehelper.ui.other.workDo.ui.adapter.WorkPageAdapter;
import com.ns.yc.lifehelper.ui.other.workDo.ui.fragment.PageFragment;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.pedaily.yc.ycdialoglib.bottomMenu.CustomBottomDialog;
import com.pedaily.yc.ycdialoglib.bottomMenu.CustomItem;
import com.pedaily.yc.ycdialoglib.bottomMenu.OnItemClickListener;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/14.
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class WorkDoActivity extends BaseActivity implements WorkDoContract.View,
        View.OnClickListener , PageFragment.OnPageListener{

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


    @Inject
    WorkDoPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.bindView(this);
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
        initializeInjector();
        initTabView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_edit:
                presenter.setOnFabClick();
                break;
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    /**
     * 这个很重要
     * DaggerUiComponent这个可以在本工程目录下build/generated/source/dagger目录下查看
     */
    private void initializeInjector() {
        DaggerUiComponent.builder().uiModule(new UiModule(this)).build().inject(this);
    }


    private void initTabView() {
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setupWithViewPager(vpPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onListTaskItemClick(int position, TaskDetailEntity entity) {
        presenter.onListTaskItemClick(position, entity);
    }

    @Override
    public void onListTaskItemLongClick(int position, TaskDetailEntity entity) {
        presenter.onListTaskItemLongClick(position, entity);
    }

    /**
     * 设置适配器
     */
    @Override
    public void setViewPagerAdapter(WorkPageAdapter mAdapter) {
        vpPager.setAdapter(mAdapter);
    }

    /**
     * 设置指示器位置
     */
    @Override
    public void setViewPagerCurrentItem(int currIndex, boolean b) {
        vpPager.setCurrentItem(currIndex, b);
    }

    /**
     * 设置指示器位置
     */
    @Override
    public int getCurrentViewPagerItem() {
        return vpPager.getCurrentItem();
    }

    /**
     * 设置指示器位置
     */
    @Override
    public void startActivityAndForResult(Intent intent, int code) {
        startActivityForResult(intent, code);
    }

    /**
     * 弹窗
     */
    @Override
    public void showDialog(final int position, final TaskDetailEntity entity) {
        new CustomBottomDialog(this)
                .title("选择分类")
                .setCancel(true,"取消选择")
                .orientation(CustomBottomDialog.VERTICAL)
                .inflateMenu(R.menu.to_do_bottom_sheet, new OnItemClickListener() {
                    @Override
                    public void click(CustomItem item) {
                        int id = item.getId();
                        switch (id){
                            case R.id.to_do_flag:
                                presenter.dialogActionFlagTask(position, entity);
                                break;
                            case R.id.to_do_put_off:
                                presenter.dialogActionEditTask(position, entity);
                                break;
                            case R.id.to_do_edit:
                                presenter.dialogActionDeleteTask(position, entity);
                                break;
                            case R.id.to_do_delete:
                                presenter.dialogActionPutOffTask(position, entity);
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    public void finishActivity() {
        if(AppUtil.isActivityLiving(this)){
            finish();
        }
    }

}
