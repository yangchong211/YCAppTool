package com.ns.yc.lifehelper.ui.other.myNews.wxNews.presenter;

import android.support.annotation.NonNull;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.cache.CacheWxChannel;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.contract.WxNewsContract;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.api.WxNewsModel;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsTypeBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/4/30
 * 描    述：微信新闻
 * 修订历史：
 *          v1.3 修改于2017年8月6日
 * ================================================
 */
public class WxNewsPresenter implements WxNewsContract.Presenter {

    private WxNewsContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private Realm realm;
    private RealmResults<CacheWxChannel> cacheWxChannels;

    public WxNewsPresenter(WxNewsContract.View homeView) {
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
    public void readCacheChannel() {
        initRealm();
        if(realm.where(CacheWxChannel.class).findAll()!=null){
            cacheWxChannels = realm.where(CacheWxChannel.class).findAll();
        } else {
            getWxDataChanel();
            return;
        }
        for(int a = 0; a< cacheWxChannels.size() ; a++){
            CacheWxChannel cacheWxChannel = new CacheWxChannel();
            cacheWxChannel.setChannel(cacheWxChannel.getChannel());
            cacheWxChannel.setChannelid(cacheWxChannel.getChannelid());
        }
    }

    private void getWxDataChanel() {
        WxNewsModel model = WxNewsModel.getInstance();
        Subscription subscribe = model.getWxNewsChannel(ConstantALiYunApi.Key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WxNewsTypeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        readCacheChannel();
                    }

                    @Override
                    public void onNext(WxNewsTypeBean wxNewsTypeBean) {
                        if (wxNewsTypeBean != null && wxNewsTypeBean.getResult() != null && wxNewsTypeBean.getResult().size() > 0) {
                            List<WxNewsTypeBean.ResultBean> result = wxNewsTypeBean.getResult();
                            for (int a = 0; a < result.size(); a++) {
                                startCacheChannel(result);
                            }
                        }
                    }
                });
        mSubscriptions.add(subscribe);
    }


    private void startCacheChannel(List<WxNewsTypeBean.ResultBean> result) {
        initRealm();
        if(realm.where(CacheWxChannel.class).findAll()!=null){
            cacheWxChannels = realm.where(CacheWxChannel.class).findAll();
        } else {
            return;
        }
        realm.beginTransaction();
        cacheWxChannels.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<result.size() ; a++){
            CacheWxChannel wxNews = realm.createObject(CacheWxChannel.class);
            wxNews.setChannelid(cacheWxChannels.get(a).getChannelid());
            wxNews.setChannel(cacheWxChannels.get(a).getChannel());
        }
        realm.commitTransaction();
    }


}
