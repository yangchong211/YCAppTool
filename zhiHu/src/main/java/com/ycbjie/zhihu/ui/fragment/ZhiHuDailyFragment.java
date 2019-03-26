package com.ycbjie.zhihu.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ns.yc.ycstatelib.StateLayoutManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.ycbjie.library.base.state.BaseStateFragment;
import com.ycbjie.library.utils.animation.AnimationViewUtil;
import com.ycbjie.library.utils.rxUtils.RxBus;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.contract.ZhiHuDailyContract;
import com.ycbjie.zhihu.model.ZhiHuDailyBeforeListBean;
import com.ycbjie.zhihu.model.ZhiHuDailyListBean;
import com.ycbjie.zhihu.presenter.ZhiHuDailyPresenter;
import com.ycbjie.zhihu.ui.activity.ZhiHuCalendarActivity;
import com.ycbjie.zhihu.ui.activity.ZhiHuNewsActivity;
import com.ycbjie.zhihu.ui.adapter.ZhiHuDailyAdapter;
import com.ycbjie.zhihu.web.activity.WebViewAnimActivity;

import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/29
 *     desc  : 知乎日报模块           日报
 *     revise:
 * </pre>
 */
public class ZhiHuDailyFragment extends BaseStateFragment implements
        ZhiHuDailyContract.View, View.OnClickListener {


    RecyclerView recyclerView;
    SwipeRefreshLayout refresher;
    FloatingActionButton fab;

    private ZhiHuNewsActivity activity;
    private ZhiHuDailyContract.Presenter presenter = new ZhiHuDailyPresenter(this);
    private List<ZhiHuDailyListBean.StoriesBean> mList = new ArrayList<>();
    private ZhiHuDailyAdapter mAdapter;
    private String currentDate;


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


    @SuppressLint("RestrictedApi")
    @Override
    public void initView(View view) {
        refresher = view.findViewById(R.id.refresher);
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        initTime();
        initRecycleView();
    }


    @Override
    public void initListener() {
        fab.setOnClickListener(this);
        mAdapter.setOnItemClickListener((view, position) -> {
            presenter.insertReadToDB(mList.get(position).getId());
            mAdapter.setReadState(position,true);
            if(mAdapter.getIsBefore()) {
                mAdapter.notifyItemChanged(position + 1);
            } else {
                mAdapter.notifyItemChanged(position + 2);
            }

            Intent intent = new Intent();
            intent.setClass(activity, WebViewAnimActivity.class);
            intent.putExtra("id",mList.get(position).getId());
            ActivityOptions options;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(
                        activity, view, "view");
                activity.startActivity(intent,options.toBundle());
            }else {
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
        statusLayoutManager.showLoading();
        presenter.getData();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fab) {
            Intent it = new Intent();
            it.setClass(activity, ZhiHuCalendarActivity.class);
            AnimationViewUtil.startActivity(activity, it, fab, R.color.redTab);
        }
    }

    private void initTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        currentDate = TimeUtils.getNowString(format);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new ZhiHuDailyAdapter(activity, mList);
        recyclerView.setAdapter(mAdapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        refresher.setOnRefreshListener(() -> {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            if(currentDate.equals(TimeUtils.getNowString(yyyyMMdd))) {
                presenter.getData();
            } else {
                int year = Integer.valueOf(currentDate.substring(0,4));
                int month = Integer.valueOf(currentDate.substring(4,6));
                int day = Integer.valueOf(currentDate.substring(6,8));
                CalendarDay date = CalendarDay.from(year, month - 1, day);
                RxBus.getDefault().post(date);
            }
        });
    }


    @Override
    public void setView(ZhiHuDailyListBean zhiHuDailyBean) {
        if (refresher.isRefreshing()) {
            refresher.setRefreshing(false);
        }
        mList = zhiHuDailyBean.getStories();
        currentDate = String.valueOf(Integer.valueOf(zhiHuDailyBean.getDate()) + 1);
        mAdapter.addDailyDate(zhiHuDailyBean);
        statusLayoutManager.showContent();
        presenter.stopInterval();
        presenter.startInterval();
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

    @Override
    public void doInterval(int currentCount) {
        mAdapter.changeTopPager(currentCount);
    }

    @Override
    public void showMoreContent(String format, ZhiHuDailyBeforeListBean zhiHuDailyBeforeListBean) {
        if(refresher.isRefreshing()) {
            refresher.setRefreshing(false);
        }
        presenter.stopInterval();
        mList = zhiHuDailyBeforeListBean.getStories();
        currentDate = String.valueOf(Integer.valueOf(zhiHuDailyBeforeListBean.getDate()));
        mAdapter.addDailyBeforeDate(zhiHuDailyBeforeListBean);
    }

}
