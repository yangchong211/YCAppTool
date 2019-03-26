package com.ycbjie.zhihu.presenter;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.rxUtils.RxUtil;
import com.ycbjie.zhihu.api.ZhiHuModel;
import com.ycbjie.zhihu.contract.ZhiHuCommentContract;
import com.ycbjie.zhihu.model.ZhiHuCommentBean;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/1
 * 描    述：知乎评论
 * 修订历史：
 * ================================================
 */
public class ZhiHuCommentPresenter implements ZhiHuCommentContract.Presenter {

    private ZhiHuCommentContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private static final int SHORT_COMMENT = 0;


    public ZhiHuCommentPresenter(ZhiHuCommentContract.View homeView) {
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
    public void getCommentData(int id, int kind) {
        ZhiHuModel model = ZhiHuModel.getInstance();
        if(kind == SHORT_COMMENT) {
            Subscription rxSubscription = model.getShortCommentInfo(id)
                    .compose(RxUtil.<ZhiHuCommentBean>rxSchedulerHelper())
                    .subscribe(new Subscriber<ZhiHuCommentBean>() {
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
                        public void onNext(ZhiHuCommentBean zhiHuCommentBean) {
                            if(zhiHuCommentBean!=null){
                                mView.showContent(zhiHuCommentBean);
                            }else {
                                mView.setEmptyView();
                            }
                        }
                    });
            mSubscriptions.add(rxSubscription);
        } else {
            Subscription rxSubscription = model.getLongCommentInfo(id)
                    .compose(RxUtil.<ZhiHuCommentBean>rxSchedulerHelper())
                    .subscribe(new Subscriber<ZhiHuCommentBean>() {
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
                        public void onNext(ZhiHuCommentBean zhiHuCommentBean) {
                            if(zhiHuCommentBean!=null){
                                mView.showContent(zhiHuCommentBean);
                            }else {
                                mView.setEmptyView();
                            }
                        }
                    });
            mSubscriptions.add(rxSubscription);
        }
    }
}
