package com.ns.yc.lifehelper.ui.other.vtex.presenter;

import com.ns.yc.lifehelper.ui.other.vtex.contract.WTNodeListContract;
import com.ns.yc.lifehelper.api.http.vtex.VTexModel;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeBean;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeListBean;
import com.ns.yc.lifehelper.utils.rxUtils.RxUtil;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class WTNodeListPresenter implements WTNodeListContract.Presenter {

    private WTNodeListContract.View mView;
    private CompositeSubscription mSubscriptions;


    public WTNodeListPresenter(WTNodeListContract.View androidView) {
        this.mView = androidView;
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
    public void getContent(String nodeName) {
        VTexModel model = VTexModel.getInstance();
        Subscription rxSubscription = model.fetchTopicList(nodeName)
                .compose(RxUtil.<List<NodeListBean>>rxSchedulerHelper())
                .subscribe(new Subscriber<List<NodeListBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<NodeListBean> nodeListBeen) {
                        mView.showContent(nodeListBeen);
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getTopInfo(String nodeName) {
        VTexModel model = VTexModel.getInstance();
        Subscription rxSubscription = model.fetchNodeInfo(nodeName)
                .compose(RxUtil.<NodeBean>rxSchedulerHelper())
                .subscribe(new Subscriber<NodeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NodeBean nodeBeen) {
                        mView.showTopInfo(nodeBeen);
                    }
                });
        mSubscriptions.add(rxSubscription);
    }
}
