package com.ns.yc.lifehelper.ui.other.vtex.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.vtex.contract.WTexPagerContract;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.TopicListBean;
import com.ns.yc.lifehelper.ui.other.vtex.presenter.WTexPagerPresenter;
import com.ns.yc.lifehelper.ui.other.vtex.view.activity.WTexRepliesActivity;
import com.ns.yc.lifehelper.ui.other.vtex.view.adapter.WTexPagerAdapter;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;

import java.util.List;

import butterknife.Bind;

public class VTexPagerFragment extends BaseFragment implements WTexPagerContract.View {

    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private WTexNewsActivity activity;
    private static final String TYPE = "type";
    private String mType;

    private WTexPagerContract.Presenter presenter = new WTexPagerPresenter(this);
    private WTexPagerAdapter mAdapter;

    public static VTexPagerFragment getInstance(String param) {
        VTexPagerFragment fragment = new VTexPagerFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (WTexNewsActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
        presenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
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
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.setClass(activity, WTexRepliesActivity.class);
                intent.putExtra("id", mAdapter.getAllData().get(position).getTopicId());
                intent.putExtra("name","");
                activity.startActivity(intent);
            }
        });
    }

    
    @Override
    public void initData() {
        recyclerView.showProgress();
        presenter.getData(mType);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new WTexPagerAdapter(activity);
        recyclerView.setAdapter(mAdapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
    }


    @Override
    public void showContent(List<TopicListBean> topicListBeen) {
        if(mAdapter!=null){
            mAdapter.clear();
            mAdapter.addAll(topicListBeen);
            mAdapter.notifyDataSetChanged();
            recyclerView.showRecycler();
        }
    }
}
