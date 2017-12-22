package com.ns.yc.lifehelper.base;


import com.ns.yc.lifehelper.utils.RxBus;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/18
 * 描    述：基于Rx的Presenter封装,控制订阅的生命周期
 * 修订历史：
 * ================================================
 */
public class RxPresenter<T extends BaseView> implements BasePresenter{

    protected T mView;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void subscribe() {

    }

    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }


    protected <U> void addRxBusSubscribe(Class<U> eventType, Action1<U> act) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(RxBus.getDefault().toDefaultObservable(eventType, act));
    }


}
