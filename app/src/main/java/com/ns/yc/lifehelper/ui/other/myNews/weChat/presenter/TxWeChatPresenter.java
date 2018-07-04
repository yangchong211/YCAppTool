package com.ns.yc.lifehelper.ui.other.myNews.weChat.presenter;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.api.constantApi.ConstantTxApi;
import com.ns.yc.lifehelper.model.event.SearchDataEvent;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.contract.TxWeChatContract;
import com.ns.yc.lifehelper.api.http.news.WeChatModel;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.model.bean.WeChatBean;
import com.ns.yc.lifehelper.utils.rxUtils.RxBus;
import com.ns.yc.lifehelper.utils.rxUtils.RxUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：微信新闻
 * 修订历史：
 * ================================================
 */
public class TxWeChatPresenter implements TxWeChatContract.Presenter {

    private TxWeChatContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public TxWeChatPresenter(TxWeChatContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        registerEvent();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void getNews(int num, final int page) {
        WeChatModel model = WeChatModel.getInstance();
        Subscription subscribe = model.getTxNews(ConstantTxApi.TX_KEY, num, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeChatBean>() {
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
                    public void onNext(WeChatBean weChatBean) {
                        if (page == 1) {
                            if (weChatBean != null && weChatBean.getNewslist() != null
                                    && weChatBean.getNewslist().size() > 0) {
                                mView.setView(weChatBean.getNewslist());
                            } else {
                                mView.setEmptyView();
                            }
                        } else {
                            if (weChatBean != null && weChatBean.getNewslist() != null
                                    && weChatBean.getNewslist().size() > 0) {
                                mView.setViewMore(weChatBean.getNewslist());
                            } else {
                                mView.stopMore();
                            }
                        }
                    }
                });
        mSubscriptions.add(subscribe);
    }


    private void registerEvent() {
        Subscription rxSubscription = RxBus.getDefault().toObservable(SearchDataEvent.class)
                .compose(RxUtil.<SearchDataEvent>rxSchedulerHelper())
                .filter(new Func1<SearchDataEvent, Boolean>() {
                    @Override
                    public Boolean call(SearchDataEvent searchEvent) {
                        return searchEvent.getType() == Constant.LikeType.TYPE_WE_CHAT;
                    }
                })
                .map(new Func1<SearchDataEvent, String>() {
                    @Override
                    public String call(SearchDataEvent searchEvent) {
                        return searchEvent.getQuery();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("出错了");
                    }

                    @Override
                    public void onNext(String s) {
                        getSearchWechatData(s);
                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    private void getSearchWechatData(String query) {
        WeChatModel model = WeChatModel.getInstance();
        Subscription subscribe = model.getWXHotSearch(ConstantTxApi.TX_KEY, 10, 1, query)
                .compose(RxUtil.<WeChatBean>rxSchedulerHelper())
                .subscribe(new Subscriber<WeChatBean>() {
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
                    public void onNext(WeChatBean weChatBean) {
                        if (weChatBean != null && weChatBean.getNewslist() != null
                                && weChatBean.getNewslist().size() > 0) {
                            mView.setView(weChatBean.getNewslist());
                        } else {
                            mView.setEmptyView();
                        }
                    }
                });
        mSubscriptions.add(subscribe);
    }


}
