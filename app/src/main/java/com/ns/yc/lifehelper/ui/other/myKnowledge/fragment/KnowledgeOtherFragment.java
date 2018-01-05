package com.ns.yc.lifehelper.ui.other.myKnowledge.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.myKnowledge.MyKnowledgeActivity;
import com.ns.yc.lifehelper.ui.other.myKnowledge.activity.KnowledgeImageActivity;
import com.ns.yc.lifehelper.ui.other.myKnowledge.adapter.GanKOtherAdapter;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKIoDataBean;
import com.ns.yc.lifehelper.ui.other.myKnowledge.model.GanKIoDataModel;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货页面  生活福利
 * 修订历史：
 * ================================================
 */
public class KnowledgeOtherFragment  extends BaseFragment {

    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private MyKnowledgeActivity activity;
    private GanKOtherAdapter adapter;

    private String type = "福利";
    private int mPage = 1;
    private int per_page_more = 20;
    private ArrayList<String> imageList = new ArrayList<>();         //存放图片地址


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyKnowledgeActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
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

    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        getData(type,mPage,per_page_more);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new GanKOtherAdapter(activity);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("selector", 2);          // 2,大图显示当前页数，1,头像，不显示页数
                bundle.putInt("code", position);    //第几张
                bundle.putStringArrayList("imageUri", imageList);
                Intent intent = new Intent(activity, KnowledgeImageActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    mPage++;
                    getData(type,mPage,per_page_more);
                } else {
                    adapter.pauseMore();
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    mPage = 1;
                    getData(type,mPage,per_page_more);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData(String type, final int mPage, int per_page_more) {
        GanKIoDataModel model = GanKIoDataModel.getInstance(activity);
        model.getGanKData(type,mPage,per_page_more)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GanKIoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GanKIoDataBean ganKIoDataBean) {
                        if(mPage==1){
                            if(ganKIoDataBean!=null && ganKIoDataBean.getResults()!=null && ganKIoDataBean.getResults().size()>0){
                                List<GanKIoDataBean.ResultsBean> results = ganKIoDataBean.getResults();
                                adapter.clear();
                                adapter.addAll(results);
                                adapter.notifyDataSetChanged();

                                imageList.clear();
                                for(int a=0 ; a<results.size() ; a++){
                                    imageList.add(results.get(a).getUrl());
                                }
                            } else {
                                recyclerView.showEmpty();
                                recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                            }
                        }else {
                            if(ganKIoDataBean!=null && ganKIoDataBean.getResults()!=null && ganKIoDataBean.getResults().size()>0){
                                List<GanKIoDataBean.ResultsBean> results = ganKIoDataBean.getResults();
                                adapter.addAll(results);
                                adapter.notifyDataSetChanged();
                                recyclerView.showRecycler();

                                for(int a=0 ; a<results.size() ; a++){
                                    imageList.add(results.get(a).getUrl());
                                }
                            } else {
                                adapter.stopMore();
                            }
                        }
                    }
                });
    }



}
