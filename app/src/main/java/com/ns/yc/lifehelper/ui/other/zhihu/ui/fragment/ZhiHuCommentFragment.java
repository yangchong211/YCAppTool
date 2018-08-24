package com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.state.BaseStateFragment;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuCommentContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuCommentBean;
import com.ns.yc.lifehelper.ui.other.zhihu.presenter.ZhiHuCommentPresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.activity.ZhiHuCommentActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter.ZhiHuCommentAdapter;
import com.ns.yc.ycstatelib.StateLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/1
 * 描    述：知乎评论
 * 修订历史：
 * ================================================
 */
public class ZhiHuCommentFragment extends BaseStateFragment implements ZhiHuCommentContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ZhiHuCommentContract.Presenter presenter = new ZhiHuCommentPresenter(this);
    private ZhiHuCommentActivity activity;
    List<ZhiHuCommentBean.CommentsBean> mList;
    private ZhiHuCommentAdapter mAdapter;
    private Bundle bundle;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ZhiHuCommentActivity) context;
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
        bundle = getArguments();
        mList = new ArrayList<>();
        mAdapter = new ZhiHuCommentAdapter(activity,mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        statusLayoutManager.showLoading();
        presenter.getCommentData(bundle.getInt("id"),bundle.getInt("kind"));
    }

    @Override
    public void showContent(ZhiHuCommentBean zhiHuCommentBean) {
        if(mAdapter!=null){
            mList.clear();
        }else {
            mAdapter = new ZhiHuCommentAdapter(activity,mList);
        }
        mList.addAll(zhiHuCommentBean.getComments());
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
