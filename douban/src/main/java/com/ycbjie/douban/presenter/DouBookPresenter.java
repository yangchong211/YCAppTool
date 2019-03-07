package com.ycbjie.douban.presenter;

import android.support.annotation.NonNull;

import com.ycbjie.douban.contract.DouBookContract;

import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣读书页面
 *     revise:
 * </pre>
 */
public class DouBookPresenter implements DouBookContract.Presenter {

    private DouBookContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;


    public DouBookPresenter(DouBookContract.View homeView) {
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


}
