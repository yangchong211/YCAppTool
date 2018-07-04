package com.ns.yc.lifehelper.ui.other.gold.presenter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.ns.yc.lifehelper.api.http.gold.GoldModel;
import com.ns.yc.lifehelper.ui.other.gold.contract.GoldPagerContract;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;
import com.ns.yc.lifehelper.utils.rxUtils.RxUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GoldPagerPresenter implements GoldPagerContract.Presenter {

    private GoldPagerContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private String mType;
    private List<GoldListBean> totalList = new ArrayList<>();
    private boolean isHotList = true;
    private int currentPage = 0;
    private static final int NUM_EACH_PAGE = 20;
    private static final int NUM_HOT_LIMIT = 3;

    public
    GoldPagerPresenter(GoldPagerContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getGoldData(String mType) {
        this.mType = mType;
        currentPage = 0;
        totalList.clear();
        GoldModel model = GoldModel.getInstance();
        Observable<List<GoldListBean>> list = model.fetchGoldList(mType, NUM_EACH_PAGE, currentPage++)
                .compose(RxUtil.<List<GoldListBean>>rxSchedulerHelper())
                .compose(RxUtil.<List<GoldListBean>>handleGoldResult());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);

        @SuppressLint("SimpleDateFormat")
        Observable<List<GoldListBean>> hotList = model.fetchGoldHotList(mType,
                new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()), NUM_HOT_LIMIT)
                .compose(RxUtil.<List<GoldListBean>>rxSchedulerHelper())
                .compose(RxUtil.<List<GoldListBean>>handleGoldResult());

        Subscription rxSubscription = Observable.concat(hotList, list)
                .subscribe(new Subscriber<List<GoldListBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("Throwable---------"+e.getMessage());
                        if(NetworkUtils.isConnected()){
                            mView.setErrorView();
                        }else {
                            mView.setNetworkErrorView();
                        }
                    }

                    @Override
                    public void onNext(List<GoldListBean> goldListBeen) {
                        if(goldListBeen!=null && goldListBeen.size()>0){
                            if (isHotList) {
                                isHotList = false;
                                totalList.addAll(goldListBeen);
                            } else {
                                isHotList = true;
                                totalList.addAll(goldListBeen);
                                mView.showContent(totalList);
                            }
                        }else {
                            mView.setEmptyView();
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getMoreGoldData() {
        GoldModel model = GoldModel.getInstance();
        Subscription rxSubscription = model.fetchGoldList(mType, NUM_EACH_PAGE, currentPage++)
                .compose(RxUtil.<List<GoldListBean>>rxSchedulerHelper())
                .compose(RxUtil.<List<GoldListBean>>handleGoldResult())
                .subscribe(new Subscriber<List<GoldListBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<GoldListBean> goldListBeen) {
                        if(goldListBeen!=null && goldListBeen.size()>0){
                            totalList.addAll(goldListBeen);
                            mView.showMoreContent(totalList, totalList.size(), totalList.size() + NUM_EACH_PAGE);
                        }else {
                            mView.setNoMore();
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }
}
