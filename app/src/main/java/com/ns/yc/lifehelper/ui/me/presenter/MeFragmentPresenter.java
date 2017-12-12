package com.ns.yc.lifehelper.ui.me.presenter;

import com.ns.yc.lifehelper.ui.me.contract.MeFragmentContract;

import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/21
 * 描    述：我的页面
 * 修订历史：
 *          v1.5 17年9月8日修改
 * ================================================
 */
public class MeFragmentPresenter implements MeFragmentContract.Presenter {

    private MeFragmentContract.View mView;
    private CompositeSubscription mSubscriptions;


    public MeFragmentPresenter(MeFragmentContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    /**
     * 获取消息数据，我的收藏，问题，控件，如果有消息则显示红点
     */
    @Override
    public void getRedHotMessageData() {

    }
}
