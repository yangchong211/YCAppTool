package com.ns.yc.lifehelper.ui.other.myNews.wyNews.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.WyNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.adapter.TodayNewsAdapter;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.bean.TodayNewsDetail;
import com.ns.yc.lifehelper.api.http.news.TodayNewsModel;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：头条新闻页面
 * 修订历史：
 * ================================================
 */
public class WyNewsFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private static final String TYPE = "头条";
    private WyNewsActivity activity;
    private String mType;
    private int num = 15;
    private int start = 1;
    private TodayNewsAdapter adapter;

    public static WyNewsFragment newInstance(String param1) {
        WyNewsFragment fragment = new WyNewsFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (WyNewsActivity) context;

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
            mType = "头条";
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
                if(position>-1 && adapter.getAllData().size()>position){
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("name",adapter.getAllData().get(position).getSrc());
                    intent.putExtra("url",adapter.getAllData().get(position).getUrl());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        num = 15;
        start = 1;
        getTodayNews(mType,num,start);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new TodayNewsAdapter(activity);
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
                    getTodayNews(mType,num,start);
                } else {
                    adapter.pauseMore();
                    ToastUtils.showShortSafe("没有网络");
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
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    num = 15;
                    start = 1;
                    getTodayNews(mType,num,start);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getTodayNews(String mType, int num, final int start) {
        TodayNewsModel model = TodayNewsModel.getInstance(activity);
        model.getTodayNewsDetail(ConstantALiYunApi.Key,mType,String.valueOf(num),String.valueOf(start))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TodayNewsDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TodayNewsDetail todayNewsDetail) {
                        if(adapter==null){
                            adapter = new TodayNewsAdapter(activity);
                        }

                        if(todayNewsDetail!=null){
                            if(start==1){
                                if(todayNewsDetail.getResult()!=null && todayNewsDetail.getResult().getList()!=null && todayNewsDetail.getResult().getList().size()>0){
                                    adapter.clear();
                                    adapter.addAll(todayNewsDetail.getResult().getList());
                                    adapter.notifyDataSetChanged();
                                    recyclerView.showRecycler();
                                } else {
                                    recyclerView.showEmpty();
                                    recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                                }
                            } else {
                                if(todayNewsDetail.getResult()!=null && todayNewsDetail.getResult().getList()!=null && todayNewsDetail.getResult().getList().size()>0){
                                    adapter.addAll(todayNewsDetail.getResult().getList());
                                    adapter.notifyDataSetChanged();
                                    recyclerView.showRecycler();
                                } else {
                                    adapter.stopMore();
                                }
                            }
                        }
                    }
                });
    }


}
