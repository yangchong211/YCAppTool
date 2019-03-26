package com.ycbjie.zhihu.presenter;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.library.db.realm.RealmDbHelper;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.rxUtils.RxUtil;
import com.ycbjie.zhihu.api.ZhiHuModel;
import com.ycbjie.zhihu.contract.ZhiHuThemeListContract;
import com.ycbjie.zhihu.model.ZhiHuThemeChildBean;

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
                .map(new Func1<ZhiHuThemeChildBean, ZhiHuThemeChildBean>() {
                    @Override
                    public ZhiHuThemeChildBean call(ZhiHuThemeChildBean zhiHuThemeChildBean) {
                        List<ZhiHuThemeChildBean.StoriesBean> list = zhiHuThemeChildBean.getStories();
                        for(ZhiHuThemeChildBean.StoriesBean item : list) {
                            boolean b = RealmDbHelper.getInstance().queryNewsId(item.getId());
                            item.setReadState(b);
                        }
                        return zhiHuThemeChildBean;
                    }
                })
                .subscribe(new Subscriber<ZhiHuThemeChildBean>() {
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
                    public void onNext(ZhiHuThemeChildBean zhiHuThemeChildBean) {
                        if(zhiHuThemeChildBean!=null){
                            mView.setView(zhiHuThemeChildBean);
                        }else {
                            mView.setEmptyView();
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
