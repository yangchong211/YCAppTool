package com.ycbjie.todo.presenter;

import android.app.Activity;

import com.ycbjie.todo.contract.PageFragmentContract;

import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营
 * 修订历史：
 * ================================================
 */
public class PageFragmentPresenter implements PageFragmentContract.Presenter {

    private PageFragmentContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Activity mContext;


    public PageFragmentPresenter(PageFragmentContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void bindView(Activity activity) {
        this.mContext = activity;
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
        mContext = null;
    }


}
