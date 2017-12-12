package com.ns.yc.lifehelper.ui.other.gold.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.gold.contract.GoldPagerContract;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;
import com.ns.yc.lifehelper.ui.other.gold.presenter.GoldPagerPresenter;
import com.ns.yc.lifehelper.ui.other.gold.view.activity.GoldMainActivity;
import com.ns.yc.lifehelper.ui.other.gold.view.adapter.GoldPagerAdapter;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;

import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/5
 * 描    述：稀土掘金模块
 * 修订历史：
 * ================================================
 */
public class GoldPagerFragment extends BaseFragment implements GoldPagerContract.View{

    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    private GoldPagerContract.Presenter presenter = new GoldPagerPresenter(this);
    private GoldMainActivity activity;
    private String mType;
    private String str;
    private GoldPagerAdapter adapter;

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
        if(getArguments()!=null){
            mType = getArguments().getString(Constant.DetailKeys.IT_GOLD_TYPE);
            str = getArguments().getString(Constant.DetailKeys.IT_GOLD_TYPE_STR);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView() {
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    @Override
    public void initData() {
        //recyclerView.showProgress();
        presenter.getGoldData(mType);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GoldPagerAdapter(activity);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    presenter.getGoldData(mType);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void showContent(List<GoldListBean> totalList) {
        if(adapter!=null){
            adapter.clear();
        }else {
            adapter = new GoldPagerAdapter(activity);
            recyclerView.setAdapter(adapter);
        }
        adapter.addAll(totalList);
        adapter.notifyDataSetChanged();
        recyclerView.showRecycler();
    }
}
