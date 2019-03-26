package com.ycbjie.zhihu.presenter;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.rxUtils.RxUtil;
import com.ycbjie.zhihu.api.ZhiHuModel;
import com.ycbjie.zhihu.contract.ZhiHuThemeContract;
import com.ycbjie.zhihu.model.ZhiHuThemeBean;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        日报
 * 修订历史：
 * ================================================
 */
public class ZhiHuThemePresenter implements ZhiHuThemeContract.Presenter {

    private ZhiHuThemeContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;


    public ZhiHuThemePresenter(ZhiHuThemeContract.View homeView) {
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
    public void getData() {
        ZhiHuModel model = ZhiHuModel.getInstance();
        Subscription rxSubscription = model.getThemeList()
                .compose(RxUtil.<ZhiHuThemeBean>rxSchedulerHelper())
                .subscribe(new Subscriber<ZhiHuThemeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(NetworkUtils.isConnected()){
                            mView.setErrorView();
                        }else {
                            mView.setNetworkErrorView();
                        }
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onNext(ZhiHuThemeBean zhiHuThemeBean) {
                        if(zhiHuThemeBean!=null){
                            mView.setView(zhiHuThemeBean);
                        }else {
                            mView.setEmptyView();
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }


}
