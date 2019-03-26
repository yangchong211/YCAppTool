package com.ycbjie.zhihu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ycbjie.library.base.state.BaseStateFragment;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.contract.ZhiHuSectionContract;
import com.ycbjie.zhihu.model.ZhiHuSectionBean;
import com.ycbjie.zhihu.presenter.ZhiHuSectionPresenter;
import com.ycbjie.zhihu.ui.activity.ZhiHuNewsActivity;
import com.ycbjie.zhihu.ui.activity.ZhiHuSectionListActivity;
import com.ycbjie.zhihu.ui.adapter.ZhiHuSectionAdapter;
import com.ns.yc.ycstatelib.StateLayoutManager;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        专栏
 * 修订历史：
 * ================================================
 */
public class ZhiHuSectionFragment extends BaseStateFragment implements ZhiHuSectionContract.View{


    RecyclerView recyclerView;
    SwipeRefreshLayout refresher;

    private ZhiHuNewsActivity activity;

    private ZhiHuSectionContract.Presenter presenter = new ZhiHuSectionPresenter(this);
    private List<ZhiHuSectionBean.DataBean> mList = new ArrayList<>();
    private ZhiHuSectionAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
    public void initView(View view) {
        refresher = view.findViewById(R.id.refresher);
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(new ZhiHuSectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view , int position) {
                Intent intent = new Intent();
                intent.setClass(activity, ZhiHuSectionListActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                intent.putExtra("title",mList.get(position).getName());
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
        mAdapter = new ZhiHuSectionAdapter(activity, mList);
        recyclerView.setAdapter(mAdapter);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getData();
            }
        });
    }


    @Override
    public void setView(ZhiHuSectionBean zhiHuSectionBean) {
        if(refresher.isRefreshing()) {
            refresher.setRefreshing(false);
        }
        mList.clear();
        mList.addAll(zhiHuSectionBean.getData());
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
