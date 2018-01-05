package com.ns.yc.lifehelper.ui.other.myNews.wxNews.presenter;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.contract.WxFragmentContract;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.api.WxNewsModel;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsDetailBean;

import io.realm.Realm;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：微信新闻页面
 * 修订历史：
 *      v1.5 修改于2017年10月9日
 * ================================================
 */
public class WxFragmentPresenter implements WxFragmentContract.Presenter {

    private WxFragmentContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private Realm realm;

    public WxFragmentPresenter(WxFragmentContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        initRealm();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void getWxNews(String mType, int num, final int start) {
        WxNewsModel model = WxNewsModel.getInstance();
        Subscription subscribe = model.getWxNewsDetail(ConstantALiYunApi.Key, mType, String.valueOf(num), String.valueOf(start))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WxNewsDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (NetworkUtils.isConnected()) {
                            mView.setErrorView();
                        } else {
                            mView.setNetworkErrorView();
                        }
                    }

                    @Override
                    public void onNext(WxNewsDetailBean wxNewsDetailBean) {
                        if (wxNewsDetailBean != null) {
                            if (start == 1) {
                                if (wxNewsDetailBean.getResult() != null && wxNewsDetailBean.getResult().getList() != null && wxNewsDetailBean.getResult().getList().size() > 0) {
                                    mView.setAdapterData(wxNewsDetailBean.getResult().getList());
                                } else {
                                    mView.setEmptyView();
                                }
                            } else {
                                if (wxNewsDetailBean.getResult() != null && wxNewsDetailBean.getResult().getList() != null && wxNewsDetailBean.getResult().getList().size() > 0) {
                                    mView.setAdapterDataMore(wxNewsDetailBean.getResult().getList());
                                } else {
                                    mView.stopMore();
                                }
                            }
                        }
                    }
                });
        mSubscriptions.add(subscribe);
    }
}
