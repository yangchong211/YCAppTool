package com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuSectionContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionBean;
import com.ns.yc.lifehelper.ui.other.zhihu.presenter.ZhiHuSectionPresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.ZhiHuNewsActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.activity.ZhiHuSectionListActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter.ZhiHuSectionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        专栏
 * 修订历史：
 * ================================================
 */
public class ZhiHuSectionFragment extends BaseFragment implements ZhiHuSectionContract.View{


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;

    private ZhiHuNewsActivity activity;

    private ZhiHuSectionContract.Presenter presenter = new ZhiHuSectionPresenter(this);
    private List<ZhiHuSectionBean.DataBean> mList = new ArrayList<>();
    private ZhiHuSectionAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    }
}
