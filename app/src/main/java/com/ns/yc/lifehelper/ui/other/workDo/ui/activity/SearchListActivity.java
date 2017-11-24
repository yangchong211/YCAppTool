package com.ns.yc.lifehelper.ui.other.workDo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.toDo.bean.TaskDetailEntity;
import com.ns.yc.lifehelper.ui.other.workDo.contract.SearchListContract;
import com.ns.yc.lifehelper.ui.other.workDo.dagger.DaggerUiComponent;
import com.ns.yc.lifehelper.ui.other.workDo.dagger.UiModule;
import com.ns.yc.lifehelper.ui.other.workDo.presenter.SearchListPresenter;
import com.ns.yc.lifehelper.ui.other.workDo.ui.adapter.SearchListAdapter;
import com.ns.yc.lifehelper.utils.AppUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/28
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class SearchListActivity extends BaseActivity implements SearchListContract.View {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    @Inject
    SearchListPresenter presenter;
    private SearchListAdapter adapter;

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
        presenter = null;
    }

    @Override
    public int getContentView() {
        return R.layout.base_bga_recycle_header;
    }

    @Override
    public void initView() {
        initRecycleView();
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchListAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new SearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TaskDetailEntity entity) {
                presenter.onItemClick(position, entity);
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        DaggerUiComponent.builder().uiModule(new UiModule(this)).build().inject(this);
    }

    @Override
    public Intent intent() {
        return getIntent();
    }

    @Override
    public void hideNoResults() {
        ToastUtils.showShort("有数据");
    }

    @Override
    public void updateToolbarTitle(String title) {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void showNoResults() {
        ToastUtils.showShort("没有数据");
    }

    @Override
    public void updateList(List<TaskDetailEntity> list) {
        adapter.setList(list);
    }

    @Override
    public void finishActivity() {
        if(AppUtil.isActivityLiving(this)){
            finish();
        }
    }

    @Override
    public void startActivityAndForResult(Intent intent, int code) {
        startActivityForResult(intent, code);
    }
}
