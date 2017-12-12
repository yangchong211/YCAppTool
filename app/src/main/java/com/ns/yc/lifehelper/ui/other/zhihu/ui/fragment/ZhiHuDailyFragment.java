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
import com.blankj.utilcode.util.TimeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewAnimActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuDailyContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyBean;
import com.ns.yc.lifehelper.ui.other.zhihu.presenter.ZhiHuDailyPresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.ZhiHuNewsActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter.ZhiHuDailyAdapter;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        日报
 * 修订历史：
 * ================================================
 */
public class ZhiHuDailyFragment extends BaseFragment implements ZhiHuDailyContract.View {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;

    private ZhiHuNewsActivity activity;

    private ZhiHuDailyContract.Presenter presenter = new ZhiHuDailyPresenter(this);
    private List<ZhiHuDailyBean.StoriesBean> mList = new ArrayList<>();
    private String nowTime;
    private ZhiHuDailyAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
        if(mAdapter!=null){
            mAdapter = null;
        }
        mList = null;
        recyclerView = null;
        refresher = null;
        nowTime = null;
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
        initTime();
        initRecycleView();
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(new ZhiHuDailyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent();
                intent.setClass(activity, WebViewAnimActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                ActivityOptions options;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(activity, view, "view");
                    activity.startActivity(intent,options.toBundle());
                }else {
                    activity.startActivity(intent);
                }

            }
        });
    }

    @Override
    public void initData() {
        presenter.getData();
    }


    private void initTime() {
        nowTime = TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd", Locale.getDefault()));
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new ZhiHuDailyAdapter(activity, mList);
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
    public void setView(ZhiHuDailyBean zhiHuDailyBean) {
        if (refresher.isRefreshing()) {
            refresher.setRefreshing(false);
        }
        mList = zhiHuDailyBean.getStories();
        nowTime = String.valueOf(Integer.valueOf(zhiHuDailyBean.getDate()) + 1);
        mAdapter.addDailyDate(zhiHuDailyBean);
    }


}
