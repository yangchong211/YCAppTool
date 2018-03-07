package com.ns.yc.lifehelper.ui.other.gank.presenter;

import com.ns.yc.lifehelper.ui.other.gank.bean.CategoryResult;
import com.ns.yc.lifehelper.ui.other.gank.contract.GanKHomeFContract;
import com.ns.yc.lifehelper.api.http.gank.GanKModel;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
public class GanKHomeFPresenter implements GanKHomeFContract.Presenter {

    private GanKHomeFContract.View fragmentView;
    private CompositeSubscription mSubscriptions;

    private final int number = 10;
    private int mPage = 1;

    public GanKHomeFPresenter(GanKHomeFContract.View androidView) {
        this.fragmentView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        getData(true);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getData(final boolean isRefresh) {
        if(isRefresh){
            mPage = 1;
        }else {
            mPage += 1;
        }
        GanKModel model = GanKModel.getInstance();
        Subscription subscribe = model.getCategoryDate(fragmentView.getDataType(), number, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CategoryResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fragmentView.showNetError();
                    }

                    @Override
                    public void onNext(CategoryResult categoryResult) {
                        if (categoryResult.results != null) {
                            fragmentView.hideSwipeLoading();
                            if (isRefresh) {
                                fragmentView.refreshData(categoryResult);
                            } else {
                                fragmentView.moreData(categoryResult);
                            }
                        } else {
                            fragmentView.showNoData();
                        }
                    }
                });
        mSubscriptions.add(subscribe);
    }


}
