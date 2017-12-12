package com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuHotContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuHotBean;
import com.ns.yc.lifehelper.ui.other.zhihu.presenter.ZhiHuHotPresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.ZhiHuNewsActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter.ZhiHuHotAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        热门
 * 修订历史：
 * ================================================
 */
public class ZhiHuHotFragment extends BaseFragment implements ZhiHuHotContract.View{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresher)
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
        recyclerView = null;
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
    public int getContentView() {
        return R.layout.base_refresh_recycle;
    }

    @Override
    public void initView() {
        initRecycleView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        presenter.getData();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new ZhiHuHotAdapter(activity, mList);
        recyclerView.setAdapter(mAdapter);
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
    }
}
