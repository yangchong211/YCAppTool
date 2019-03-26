package com.ycbjie.gank.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.NetworkUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.gank.R;
import com.ycbjie.gank.api.GanKModel;
import com.ycbjie.gank.bean.bean.GanKIoDataBean;
import com.ycbjie.gank.view.activity.KnowledgeImageActivity;
import com.ycbjie.gank.view.activity.MyKnowledgeActivity;
import com.ycbjie.gank.view.adapter.GanKOtherAdapter;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.library.http.ExceptionUtils;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/3/28
 *     desc  : 生活福利
 *     revise:
 * </pre>
 */
public class KnowledgeOtherFragment  extends BaseFragment {

    YCRefreshView recyclerView;
    private MyKnowledgeActivity activity;
    private GanKOtherAdapter adapter;
    private String type = "福利";
    private int mPage = 1;
    /**
     * 存放图片地址
     */
    private ArrayList<String> imageList = new ArrayList<>();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(position -> {
            Bundle bundle = new Bundle();
            // 2,大图显示当前页数，1,头像，不显示页数
            bundle.putInt("selector", 2);
            //第几张
            bundle.putInt("code", position);
            bundle.putStringArrayList("imageUri", imageList);
            Intent intent = new Intent(activity, KnowledgeImageActivity.class);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        getData(type,mPage);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new GanKOtherAdapter(activity);
        recyclerView.setAdapter(adapter);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    mPage++;
                    getData(type,mPage);
                } else {
                    adapter.pauseMore();
                    ToastUtils.showRoundRectToast("网络不可用");
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
                    ToastUtils.showRoundRectToast("网络不可用");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showRoundRectToast("网络不可用");
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
        recyclerView.setRefreshListener(() -> {
            if (NetworkUtils.isConnected()) {
                mPage = 1;
                getData(type,mPage);
            } else {
                recyclerView.setRefreshing(false);
                ToastUtils.showRoundRectToast("网络不可用");
            }
        });
    }

    private void getData(String type, final int mPage) {
        GanKModel model = GanKModel.getInstance();
        model.getGanKData(type,mPage,KnowledgeCustomFragment.PER_PAGE_MORE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GanKIoDataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
