package com.ycbjie.todo.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pedaily.yc.ycdialoglib.dialogMenu.CustomBottomDialog;
import com.pedaily.yc.ycdialoglib.snackbar.SnackBarUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.todo.R;
import com.ycbjie.todo.contract.WorkDoContract;
import com.ycbjie.todo.dagger.DaggerUiComponent;
import com.ycbjie.todo.dagger.UiModule;
import com.ycbjie.todo.presenter.WorkDoPresenter;
import com.ycbjie.todo.ui.adapter.WorkPageAdapter;
import com.ycbjie.todo.ui.fragment.PageFragment;

import javax.inject.Inject;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 此版块训练dagger2+MVP
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_WORK_ACTIVITY)
public class WorkDoActivity extends BaseActivity implements WorkDoContract.View,
        View.OnClickListener , PageFragment.OnPageListener{

    private Toolbar toolBar;
    private TabLayout tab;
    private ViewPager vpPager;
    private FloatingActionButton fabEdit;

    @Inject
    WorkDoPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化读写权限
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
        initFindViewById();
        initToolBar();
    }

    private void initFindViewById() {
        toolBar = findViewById(R.id.tool_bar);
        tab = findViewById(R.id.tab);
        vpPager = findViewById(R.id.vp_pager);
        fabEdit = findViewById(R.id.fab_edit);
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
        int i = v.getId();
        if (i == R.id.fab_edit) {
            presenter.setOnFabClick();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
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
        DaggerUiComponent.builder().uiModule(new UiModule(this))
                .build().inject(this);
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
    public void onListTaskItemClick(int position, CacheTaskDetailEntity entity) {
        presenter.onListTaskItemClick(position, entity);
    }

    @Override
    public void onListTaskItemLongClick(int position, CacheTaskDetailEntity entity) {
        presenter.onListTaskItemLongClick(position, entity);
    }

    /**
     * 设置适配器
     */
    @Override
    public void setViewPagerAdapter(WorkPageAdapter mAdapter) {
        if (mAdapter!=null){
            vpPager.setAdapter(mAdapter);
        }
    }

    /**
     * 设置指示器位置
     */
    @Override
    public void setViewPagerCurrentItem(int currIndex, boolean b) {
        if (currIndex>=0 && currIndex<Constant.DAY_OF_WEEK){
            vpPager.setCurrentItem(currIndex, b);
        }
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
    public void showDialog(final int position, final CacheTaskDetailEntity entity) {
        CustomBottomDialog dialog = new CustomBottomDialog(this)
                .title("选择分类")
                .setCancel(true,"取消选择")
                .orientation(CustomBottomDialog.VERTICAL)
                .inflateMenu(R.menu.to_do_bottom_sheet, item -> {
                    int id = item.getId();
                    if (id == R.id.to_do_flag) {
                        presenter.dialogActionFlagTask(position, entity);
                    } else if (id == R.id.to_do_put_off) {
                        presenter.dialogActionPutOffTask(position, entity);
                    } else if (id == R.id.to_do_edit) {
                        presenter.dialogActionEditTask(position, entity);
                    } else if (id == R.id.to_do_delete) {
                        presenter.dialogActionDeleteTask(position, entity);
                    }
                });
        dialog.show();
    }


    @Override
    public void finishActivity() {
        finish();
    }


    @Override
    public void showAction(String message, String action, View.OnClickListener listener) {
        SnackBarUtils.showSnackBar(this,message,action,listener);
    }


}
