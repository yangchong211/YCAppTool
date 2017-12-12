package com.ns.yc.lifehelper.ui.other.zhihu.presenter;

import android.support.annotation.NonNull;

import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuThemeListContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.api.ZhiHuModel;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuThemeChildBean;
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
public class ZhiHuThemeListPresenter implements ZhiHuThemeListContract.Presenter {

    private ZhiHuThemeListContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;


    public ZhiHuThemeListPresenter(ZhiHuThemeListContract.View homeView) {
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
    public void getThemeChildData(int id) {
        ZhiHuModel model = ZhiHuModel.getInstance();
        Subscription rxSubscription = model.getThemeChildList(id)
                .compose(RxUtil.<ZhiHuThemeChildBean>rxSchedulerHelper())
                .subscribe(new Subscriber<ZhiHuThemeChildBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZhiHuThemeChildBean zhiHuThemeChildBean) {
                        if(zhiHuThemeChildBean!=null){
                            mView.setView(zhiHuThemeChildBean);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }
}
