package com.ns.yc.lifehelper.ui.other.gold.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.base.state.BaseStateFragment;
import com.ns.yc.lifehelper.ui.other.gold.contract.GoldPagerContract;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;
import com.ns.yc.lifehelper.ui.other.gold.presenter.GoldPagerPresenter;
import com.ns.yc.lifehelper.ui.other.gold.view.activity.GoldMainActivity;
import com.ns.yc.lifehelper.ui.other.gold.view.adapter.GoldListAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.ns.yc.ycstatelib.StateLayoutManager;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/5
 * 描    述：稀土掘金模块
 * 修订历史：
 * ================================================
 */
public class GoldPagerFragment extends BaseStateFragment implements GoldPagerContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    private GoldPagerContract.Presenter presenter = new GoldPagerPresenter(this);
    private GoldMainActivity activity;
    private String mType;
    private String str;
    private GoldListAdapter adapter;
    private boolean isLoadingMore = false;

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
        activity = (GoldMainActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(Constant.DetailKeys.IT_GOLD_TYPE);
            str = getArguments().getString(Constant.DetailKeys.IT_GOLD_TYPE_STR);
        }
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

    }


    @Override
    public void initData() {
        statusLayoutManager.showLoading();
        presenter.getGoldData(mType);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GoldListAdapter(activity, new ArrayList<GoldListBean>(),str);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        //刷新
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    if (!adapter.getHotFlag()) {
                        recyclerView.addItemDecoration(line);
                    }
                    adapter.setHotFlag(true);
                    presenter.getGoldData(mType);
                } else {
                    refresher.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }

            }
        });

        adapter.setOnHotCloseListener(new GoldListAdapter.OnHotCloseListener() {
            @Override
            public void onClose() {
                recyclerView.removeItemDecoration(line);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    if(!isLoadingMore){
                        isLoadingMore = true;
                        presenter.getMoreGoldData();
                    }
                }
            }
        });
    }


    @Override
    public void showContent(List<GoldListBean> totalList) {
        if (adapter == null) {
            adapter = new GoldListAdapter(activity, new ArrayList<GoldListBean>(),str);
            recyclerView.setAdapter(adapter);
        }
        adapter.updateData(totalList);
        statusLayoutManager.showContent();
    }


    @Override
    public void setNoMore() {
        ToastUtil.showToast(activity,"没有更多信息");
    }


    @Override
    public void showMoreContent(List<GoldListBean> totalList, int start, int end) {
        adapter.updateData(totalList);
        adapter.notifyItemRangeInserted(start, end);
        isLoadingMore = false;
    }


    @Override
    public void setEmptyView() {
        statusLayoutManager.showEmptyData();
    }


    @Override
    public void setErrorView() {
        statusLayoutManager.showError();
    }


    @Override
    public void setNetworkErrorView() {
        statusLayoutManager.showNetWorkError();
    }


}
