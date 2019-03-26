package com.ycbjie.gank.presenter;

import android.annotation.SuppressLint;

import com.ycbjie.gank.api.GanKModel;
import com.ycbjie.gank.bean.bean.CategoryResult;
import com.ycbjie.gank.contract.GanKHomeFContract;
import com.ycbjie.library.http.ExceptionUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 干货集中营
 *     revise:
 * </pre>
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

    @SuppressLint("CheckResult")
    @Override
    public void getData(final boolean isRefresh) {
        if(isRefresh){
            mPage = 1;
        }else {
            mPage += 1;
        }
        GanKModel model = GanKModel.getInstance();
        model.getCategoryDate(fragmentView.getDataType(), number, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResult>() {
                    @Override
                    public void accept(CategoryResult categoryResult) throws Exception {
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ExceptionUtils.handleException(throwable);
                    }
                });
    }


}
