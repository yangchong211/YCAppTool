package com.ns.yc.lifehelper.ui.other.zhihu.presenter;

import android.support.annotation.NonNull;

import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuSectionListContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.api.ZhiHuModel;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionChildBean;
import com.ns.yc.lifehelper.utils.RxUtil;

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
public class ZhiHuSectionListPresenter implements ZhiHuSectionListContract.Presenter {

    private ZhiHuSectionListContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;


    public ZhiHuSectionListPresenter(ZhiHuSectionListContract.View homeView) {
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
    public void getSectionChildData(int id) {
        ZhiHuModel model = ZhiHuModel.getInstance();
        Subscription rxSubscription = model.getSectionChildList(id)
                .compose(RxUtil.<ZhiHuSectionChildBean>rxSchedulerHelper())
                .subscribe(new Subscriber<ZhiHuSectionChildBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZhiHuSectionChildBean zhiHuSectionChildBean) {
                        if(zhiHuSectionChildBean!=null){
                            mView.setView(zhiHuSectionChildBean);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }
}
