package com.ycbjie.zhihu.presenter;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.library.db.realm.RealmDbHelper;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.rxUtils.RxUtil;
import com.ycbjie.zhihu.api.ZhiHuModel;
import com.ycbjie.zhihu.contract.ZhiHuHotContract;
import com.ycbjie.zhihu.model.ZhiHuHotBean;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
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
public class ZhiHuHotPresenter implements ZhiHuHotContract.Presenter {

    private ZhiHuHotContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;


    public ZhiHuHotPresenter(ZhiHuHotContract.View homeView) {
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
        Subscription rxSubscription = model.getHotList()
                .compose(RxUtil.<ZhiHuHotBean>rxSchedulerHelper())
                .map(new Func1<ZhiHuHotBean, ZhiHuHotBean>() {
                    @Override
                    public ZhiHuHotBean call(ZhiHuHotBean zhiHuHotBean) {
                        List<ZhiHuHotBean.RecentBean> list = zhiHuHotBean.getRecent();
                        for(ZhiHuHotBean.RecentBean item : list) {
                            item.setReadState(RealmDbHelper.getInstance().queryNewsId(item.getNews_id()));
                        }
                        return zhiHuHotBean;
                    }
                })
                .subscribe(new Subscriber<ZhiHuHotBean>() {
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
                    public void onNext(ZhiHuHotBean zhiHuHotBean) {
                        if(zhiHuHotBean!=null){
                            mView.setView(zhiHuHotBean);
                        }else {
                            mView.setEmptyView();
                            int code = ExceptionUtils.CODE_NO_OTHER;
                            ExceptionUtils.serviceException(code);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void insertReadToDB(int id) {
        RealmDbHelper.getInstance().insertNewsId(id);
    }
}
