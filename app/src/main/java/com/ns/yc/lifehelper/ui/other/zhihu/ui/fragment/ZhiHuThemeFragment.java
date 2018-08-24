package com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.state.BaseStateFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuThemeContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuThemeBean;
import com.ns.yc.lifehelper.ui.other.zhihu.presenter.ZhiHuThemePresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.ZhiHuNewsActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.activity.ZhiHuThemeListActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter.ZhiHuThemeAdapter;
import com.ns.yc.ycstatelib.StateLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        主题
 * 修订历史：
 * ================================================
 */
public class ZhiHuThemeFragment extends BaseStateFragment implements ZhiHuThemeContract.View{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    private ZhiHuNewsActivity activity;
    private ZhiHuThemeContract.Presenter presenter = new ZhiHuThemePresenter(this);
    private List<ZhiHuThemeBean.OthersBean> mList = new ArrayList<>();
    private ZhiHuThemeAdapter mAdapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
        if(mAdapter !=null){
            mAdapter = null;
        }
        mList = null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ZhiHuNewsActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    protected void initStatusLayout() {
        statusLayoutManager = StateLayoutManager.newBuilder(activity)
                .contentView(R.layout.base_refresh_recycle)
                .emptyDataView(R.layout.view_custom_empty_data)
                .errorView(R.layout.view_custom_data_error)
                .loadingView(R.layout.view_custom_loading_data)
                .netWorkErrorView(R.layout.view_custom_network_error)
                .build();
    }


    @Override
    public void initView() {
        initRecycleView();
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(new ZhiHuThemeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent();
                intent.setClass(activity, ZhiHuThemeListActivity.class);
                intent.putExtra("id", id);
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
        statusLayoutManager.showLoading();
        presenter.getData();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        mAdapter = new ZhiHuThemeAdapter(activity, mList);
        recyclerView.setAdapter(mAdapter);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getData();
            }
        });
    }


    @Override
    public void setView(ZhiHuThemeBean zhiHuThemeBean) {
        if(refresher.isRefreshing()) {
            refresher.setRefreshing(false);
        }
        mList.clear();
        mList.addAll(zhiHuThemeBean.getOthers());
        mAdapter.notifyDataSetChanged();
        statusLayoutManager.showContent();
    }



    @Override
    public void setEmptyView() {
        statusLayoutManager.showEmptyData();
    }


    @Override
    public void setNetworkErrorView() {
        statusLayoutManager.showError();
    }


    @Override
    public void setErrorView() {
        statusLayoutManager.showNetWorkError();
    }


}
