package com.ns.yc.lifehelper.ui.other.myKnowledge.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.myKnowledge.MyKnowledgeActivity;
import com.ns.yc.lifehelper.ui.other.myKnowledge.adapter.GanKEveryDayAdapter;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKEveryDay;
import com.ns.yc.lifehelper.ui.other.myKnowledge.model.GanKIoDataModel;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货页面  每日推荐
 * 修订历史：
 * <p>
 * 更新逻辑：判断是否是第二天(相对于2017-08-23)
 * 是：判断是否是大于12：30
 * *****     |是：刷新当天数据
 * *****     |否：使用缓存：|无：请求前一天数据,直到请求到数据为止
 * **********             |有：使用缓存
 * 否：使用缓存 ： |无：请求今天数据
 * **********    |有：使用缓存
 * ================================================
 */
public class KnowledgeEveryFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    YCRefreshView recyclerView;
    private MyKnowledgeActivity activity;
    private GanKEveryDayAdapter adapter;

    private String year = "2017";
    private String month = "08";
    private String day = "23";

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        getData(year,month,day);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GanKEveryDayAdapter(activity);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.px2dp(2), getResources().getColor(R.color.grayLine));
        recyclerView.addItemDecoration(line);
        AddHeader();
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
                    getData(year,month,day);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddHeader() {


    }

    private void getData(String year , String month , String day) {
        GanKIoDataModel model = GanKIoDataModel.getInstance(activity);
        model.getGanKIoDay(year,month,day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GanKEveryDay>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GanKEveryDay ganKEveryDay) {
                        if(ganKEveryDay!=null){
                            GanKEveryDay.ResultsBean results = ganKEveryDay.getResults();
                            List<GanKEveryDay.ResultsBean.AndroidBean> android = results.getAndroid();
                            List<GanKEveryDay.ResultsBean.AndroidBean> androidBeen = results.getiOS();
                            List<GanKEveryDay.ResultsBean.AndroidBean> resource = results.getResource();
                            List<GanKEveryDay.ResultsBean.AndroidBean> restMovie = results.getRestMovie();
                            List<GanKEveryDay.ResultsBean.AndroidBean> welfare = results.getWelfare();
                            List<GanKEveryDay.ResultsBean.AndroidBean> app = results.getApp();
                            List<GanKEveryDay.ResultsBean.AndroidBean> front = results.getFront();
                            List<GanKEveryDay.ResultsBean.AndroidBean> recommend = results.getRecommend();
                            if(androidBeen!=null){
                                android.addAll(androidBeen);
                            }
                            if(resource!=null){
                                android.addAll(resource);
                            }
                            if(restMovie!=null){
                                android.addAll(restMovie);
                            }
                            if(welfare!=null){
                                android.addAll(welfare);
                            }
                            if(app!=null){
                                android.addAll(app);
                            }
                            if(front!=null){
                                android.addAll(front);
                            }
                            if(recommend!=null){
                                android.addAll(recommend);
                            }
                            adapter.clear();
                            adapter.addAll(android);
                            adapter.notifyDataSetChanged();
                            recyclerView.showRecycler();
                        }
                    }
                });
    }


}
