package com.ns.yc.lifehelper.ui.other.vtex.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.state.BaseStateBarActivity;
import com.ns.yc.lifehelper.inter.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.vtex.contract.WTNodeListContract;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeBean;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeListBean;
import com.ns.yc.lifehelper.ui.other.vtex.presenter.WTNodeListPresenter;
import com.ns.yc.lifehelper.ui.other.vtex.view.adapter.WTNodeListAdapter;
import com.ns.yc.ycstatelib.StateLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class WTNodeListActivity extends BaseStateBarActivity implements
        View.OnClickListener , WTNodeListContract.View{

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    private WTNodeListContract.Presenter presenter = new WTNodeListPresenter(this);

    private String nodeName;
    private WTNodeListAdapter mAdapter;

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
    protected void initStatusLayout() {
        statusLayoutManager = StateLayoutManager.newBuilder(this)
                .contentView(R.layout.base_refresh_recycle)
                .emptyDataView(R.layout.view_custom_empty_data)
                .errorView(R.layout.view_custom_data_error)
                .loadingView(R.layout.view_custom_loading_data)
                .netWorkErrorView(R.layout.view_custom_network_error)
                .build();
    }

    @Override
    public void initView() {
        initIntentData();
        initToolBar();
        initRecycleView();
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                //intent.setClass(WTNodeListActivity.this, RepliesActivity.class);
                if (position - 1 < 0 || mAdapter.get(position - 1) == null)
                    return;
                intent.putExtra("id", mAdapter.get(position-1).getId());
                intent.putExtra("top_info", mAdapter.get(position - 1));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        statusLayoutManager.showLoading();
        presenter.getContent(nodeName);
        presenter.getTopInfo(nodeName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initIntentData() {
        nodeName = getIntent().getStringExtra("node_name");
    }

    private void initToolBar() {
        if(nodeName!=null && nodeName.length()>0){
            toolbarTitle.setText(nodeName);
        }else {
            toolbarTitle.setText("详情帖子页面");
        }
    }


    private void initRecycleView() {
        mAdapter = new WTNodeListAdapter(this, new ArrayList<NodeListBean>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getContent(nodeName);
            }
        });
    }


    @Override
    public void showContent(List<NodeListBean> nodeListBeen) {
        if(refresher.isRefreshing()) {
            refresher.setRefreshing(false);
        }
        if(nodeListBeen!=null && nodeListBeen.size()>0){
            mAdapter.setContentData(nodeListBeen);
            statusLayoutManager.showContent();
        }else {
            statusLayoutManager.showEmptyData();
        }
    }

    @Override
    public void showTopInfo(NodeBean nodeBeen) {
        mAdapter.setTopData(nodeBeen);
    }
}
