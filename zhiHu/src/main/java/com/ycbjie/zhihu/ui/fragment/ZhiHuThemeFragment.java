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
import com.ycbjie.library.constant.Constant;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.contract.ZhiHuThemeContract;
import com.ycbjie.zhihu.model.ZhiHuThemeBean;
import com.ycbjie.zhihu.presenter.ZhiHuThemePresenter;
import com.ycbjie.zhihu.ui.activity.ZhiHuNewsActivity;
import com.ycbjie.zhihu.ui.activity.ZhiHuThemeListActivity;
import com.ycbjie.zhihu.ui.adapter.ZhiHuThemeAdapter;
import com.ns.yc.ycstatelib.StateLayoutManager;

import java.util.ArrayList;
import java.util.List;



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

    RecyclerView recyclerView;
    SwipeRefreshLayout refresher;

    private ZhiHuNewsActivity activity;
    private ZhiHuThemeContract.Presenter presenter = new ZhiHuThemePresenter(this);
    private List<ZhiHuThemeBean.OthersBean> mList = new ArrayList<>();
    private ZhiHuThemeAdapter mAdapter;


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
        mAdapter.setOnItemClickListener(new ZhiHuThemeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent();
                intent.setClass(activity, ZhiHuThemeListActivity.class);
                intent.putExtra(Constant.ID, id);
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
                //刷新数据
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
