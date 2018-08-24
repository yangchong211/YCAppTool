package com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.state.BaseStateFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuHotContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuHotBean;
import com.ns.yc.lifehelper.ui.other.zhihu.presenter.ZhiHuHotPresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.ZhiHuNewsActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter.ZhiHuHotAdapter;
import com.ns.yc.lifehelper.ui.webView.view.WebViewAnimActivity;
import com.ns.yc.ycstatelib.StateLayoutManager;

import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        热门
 * 修订历史：
 * ================================================
 */
public class ZhiHuHotFragment extends BaseStateFragment implements ZhiHuHotContract.View{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    private ZhiHuNewsActivity activity;
    private ZhiHuHotContract.Presenter presenter = new ZhiHuHotPresenter(this);
    private List<ZhiHuHotBean.RecentBean> mList = new ArrayList<>();
    private ZhiHuHotAdapter mAdapter;


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
        refresher = null;
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
        mAdapter.setOnItemClickListener(new ZhiHuHotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                presenter.insertReadToDB(mList.get(position).getNews_id());
                mAdapter.setReadState(position,true);
                mAdapter.notifyItemChanged(position);
                Intent intent = new Intent();
                intent.setClass(activity, WebViewAnimActivity.class);
                intent.putExtra("id",mList.get(position).getNews_id());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view, "view");
                    activity.startActivity(intent,options.toBundle());
                }else {
                    activity.startActivity(intent);
                }
            }
        });
    }


    @Override
    public void initData() {
        statusLayoutManager.showLoading();
        presenter.getData();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new ZhiHuHotAdapter(activity, mList);
        recyclerView.setAdapter(mAdapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getData();
            }
        });
    }


    @Override
    public void setView(ZhiHuHotBean zhiHuHotBean) {
        if(refresher.isRefreshing()) {
            refresher.setRefreshing(false);
        }
        mList.clear();
        mList.addAll(zhiHuHotBean.getRecent());
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
