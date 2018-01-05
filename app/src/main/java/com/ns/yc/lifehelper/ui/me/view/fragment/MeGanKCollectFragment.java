package com.ns.yc.lifehelper.ui.me.view.fragment;

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
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.me.contract.MeGanKCollectContract;
import com.ns.yc.lifehelper.ui.me.presenter.MeGanKCollectPresenter;
import com.ns.yc.lifehelper.ui.me.view.activity.MeCollectActivity;
import com.ns.yc.lifehelper.ui.me.view.adapter.MeGanKCollectAdapter;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/12
 * 描    述：我的收藏页面
 * 修订历史：
 * ================================================
 */
public class MeGanKCollectFragment extends BaseFragment implements MeGanKCollectContract.View{

    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;

    private MeGanKCollectContract.Presenter presenter = new MeGanKCollectPresenter(this);

    private MeCollectActivity activity;
    private MeGanKCollectAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MeCollectActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

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
        presenter.getCollectData(false);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MeGanKCollectAdapter(activity);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);
        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    presenter.getCollectData(false);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void setEmpty() {
        recyclerView.showError();
    }

    @Override
    public void setFavoriteItems(List<GanKFavorite> favorites) {
        adapter.clear();
        adapter.addAll(favorites);
        adapter.notifyDataSetChanged();
    }
}
