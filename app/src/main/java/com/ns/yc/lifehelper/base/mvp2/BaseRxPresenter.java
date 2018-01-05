package com.ns.yc.lifehelper.base.mvp2;

import com.ns.yc.lifehelper.utils.RxBus;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/18
 * 描    述：基于Rx的Presenter封装，控制订阅的生命周期
 * 修订历史：
 * ================================================
 */
public class BaseRxPresenter<T extends BaseMvpView> implements BaseMvpPresenter<T> {

    protected T mView;
    protected CompositeSubscription mCompositeSubscription;

    /**
     * 开始绑定数据
     */
    @Override
    public void subscribe(T view) {
        this.mView = view;
    }

    /**
     * 解除绑定数据
     */
    @Override
    public void unSubscribe() {
        this.mView = null;
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 添加到CompositeSubscription集合中
     */
    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     * 添加订阅事件
     */
    protected <U> void addRxBusSubscribe(Class<U> eventType, Action1<U> act) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(RxBus.getDefault().toDefaultObservable(eventType, act));
    }


}
