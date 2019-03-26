package com.ycbjie.news.presenter;

import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.news.api.ConstantTxApi;
import com.ycbjie.news.api.WeChatModel;
import com.ycbjie.news.contract.TxWeChatContract;
import com.ycbjie.news.model.WeChatBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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

    public TxWeChatPresenter(TxWeChatContract.View homeView) {
        this.mView = homeView;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {

    }


    @Override
    public void getNews(int num, final int page) {
        WeChatModel model = WeChatModel.getInstance();
        model.getTxNews(ConstantTxApi.TX_KEY, num, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeChatBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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

                    @Override
                    public void onError(Throwable e) {
                        if (NetworkUtils.isConnected()) {
                            mView.setErrorView();
                        } else {
                            mView.setNetworkErrorView();
                        }
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getSearchWechatData(String query) {
        WeChatModel model = WeChatModel.getInstance();
        model.getWXHotSearch(ConstantTxApi.TX_KEY, 10, 1, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeChatBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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

                    @Override
                    public void onError(Throwable e) {
                        if (NetworkUtils.isConnected()) {
                            mView.setErrorView();
                        } else {
                            mView.setNetworkErrorView();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
