package com.ycbjie.news.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseLazyFragment;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.news.R;
import com.ycbjie.news.api.TodayNewsModel;
import com.ycbjie.news.model.TodayNewsDetail;
import com.ycbjie.news.ui.activity.WyNewsActivity;
import com.ycbjie.news.ui.adapter.TodayNewsAdapter;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/3/28
 *     desc  : 头条新闻页面
 *     revise:
 * </pre>
 */
public class WyNewsFragment extends BaseLazyFragment {

    private YCRefreshView recyclerView;
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
    public void onLazyLoad() {
        recyclerView.showProgress();
        num = 15;
        start = 1;
        getTodayNews(mType,num,start);
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
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position>-1 && adapter.getAllData().size()>position){
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(Constant.URL,adapter.getAllData().get(position).getUrl());
                    bundle1.putString(Constant.TITLE,adapter.getAllData().get(position).getSrc());
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle1);
                }
            }
        });
    }

    @Override
    public void initData() {

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
                    ToastUtils.showShort("没有网络");
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
        model.getTodayNewsDetail(WyNewsActivity.Key,mType,String.valueOf(num),String.valueOf(start))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TodayNewsDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
