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
import com.ns.yc.lifehelper.base.mvp1.BaseLazyFragment;
import com.ns.yc.lifehelper.ui.me.contract.MeGanKCollectContract;
import com.ns.yc.lifehelper.ui.me.presenter.MeGanKCollectPresenter;
import com.ns.yc.lifehelper.ui.me.view.activity.MeCollectActivity;
import com.ns.yc.lifehelper.ui.me.view.adapter.MeGanKCollectAdapter;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.List;

import butterknife.BindView;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/12
 *     desc  : 我的收藏页面，干货集中营
 *     revise:
 * </pre>
 */
public class MeGanKCollectFragment extends BaseLazyFragment implements MeGanKCollectContract.View{

    @BindView(R.id.recyclerView)
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

    }


    @Override
    public void onLazyLoad() {
        recyclerView.showProgress();
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
        recyclerView.setEmptyView(R.layout.view_custom_data_error);
        recyclerView.showEmpty();
    }

    @Override
    public void setDataList(List<GanKFavorite> favorites) {
        adapter.clear();
        adapter.addAll(favorites);
        adapter.notifyDataSetChanged();
        recyclerView.showRecycler();
    }

}
