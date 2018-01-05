package com.ns.yc.lifehelper.ui.other.myNews.wxNews.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.WxNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.contract.WxFragmentContract;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.presenter.WxFragmentPresenter;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.view.adapter.WxNewsAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：微信新闻页面
 * 修订历史：
 *      v1.5 修改于2017年10月9日
 * ================================================
 */
public class WxNewsFragment extends BaseFragment implements WxFragmentContract.View{

    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private static String TYPE = "wx";
    private String mType;
    private WxNewsActivity activity;
    private int num = 11;
    private int start = 1;
    private WxNewsAdapter adapter;

    private WxFragmentContract.Presenter presenter = new WxFragmentPresenter(this);

    public static WxNewsFragment newInstance(String param) {
        WxNewsFragment fragment = new WxNewsFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (WxNewsActivity) context;

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
        if(mType==null || mType.length()==0){
            mType = "1";
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
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position>-1 && adapter.getAllData().size()>position){
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("name",adapter.getAllData().get(position).getWeixinname());
                    intent.putExtra("url",adapter.getAllData().get(position).getUrl());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        presenter.getWxNews(mType,num,start);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new WxNewsAdapter(activity);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(5), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if(NetworkUtils.isConnected()){
                    start = num + 1;
                    num = start + 10;
                    presenter.getWxNews(mType,num,start);
                } else {
                    adapter.pauseMore();
                    ToastUtil.showToast(activity,"没有网络");
                }
            }

            @Override
            public void onMoreClick() {

            }
        });

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtil.showToast(activity,"没有网络");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtil.showToast(activity,"没有网络");
                }
            }
        });

        //设置错误
        adapter.setError(R.layout.view_recycle_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    num = 11;
                    start = 1;
                    presenter.getWxNews(mType,num,start);
                    recyclerView.setRefreshing(false);
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtil.showToast(activity,"没有网络");
                }
            }
        });
    }

    @Override
    public void setAdapterData(List<WxNewsDetailBean.ResultBean.ListBean> list) {
        if(adapter!=null){
            adapter.clear();
        }else {
            adapter = new WxNewsAdapter(activity);
            recyclerView.setAdapter(adapter);
        }
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
        recyclerView.showRecycler();
    }

    @Override
    public void setEmptyView() {
        recyclerView.showEmpty();
        recyclerView.setEmptyView(R.layout.view_custom_empty_data);
    }

    @Override
    public void setAdapterDataMore(List<WxNewsDetailBean.ResultBean.ListBean> list) {
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
        recyclerView.showRecycler();
    }

    @Override
    public void stopMore() {
        adapter.stopMore();
    }

    @Override
    public void setErrorView() {
        recyclerView.setErrorView(R.layout.view_custom_data_error);
        recyclerView.showError();
        LinearLayout ll_error_view = (LinearLayout) recyclerView.findViewById(R.id.ll_error_view);
        ll_error_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
    }

    @Override
    public void setNetworkErrorView() {
        recyclerView.setErrorView(R.layout.view_custom_network_error);
        recyclerView.showError();
        LinearLayout ll_set_network = (LinearLayout) recyclerView.findViewById(R.id.ll_set_network);
        ll_set_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkUtils.isConnected()){
                    initData();
                }else {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            }
        });
    }
}
