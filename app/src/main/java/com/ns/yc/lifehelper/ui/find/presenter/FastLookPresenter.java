package com.ns.yc.lifehelper.ui.find.presenter;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.cache.CacheFastLook;
import com.ns.yc.lifehelper.ui.find.contract.FastLookContract;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.api.WxNewsModel;

import java.util.ArrayList;
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
 * 创建日期：2017/11/14
 * 描    述：快看页面
 * 修订历史：
 * ================================================
 */
public class FastLookPresenter implements FastLookContract.Presenter {

    private FastLookContract.View mView;
    private CompositeSubscription mSubscriptions;
    private int num = 11;
    private int start = 1;
    private Realm realm;
    private RealmResults<CacheFastLook> cacheFastLooks;

    public FastLookPresenter(FastLookContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        initRealm();
        getData(false);
    }

    private void initRealm() {
        if(realm==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void getData(boolean isRefresh) {
        if(isRefresh){
            num = 11;
            start = 1;
        }else {
            start = num + 1;
            num = start + 10;
        }
        WxNewsModel model = WxNewsModel.getInstance();
        Subscription subscribe = model.getWxNewsDetail(ConstantALiYunApi.Key, "1", String.valueOf(num), String.valueOf(start))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WxNewsDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setError();
                    }

                    @Override
                    public void onNext(WxNewsDetailBean wxNewsDetailBean) {
                        if (wxNewsDetailBean != null) {
                            if (start == 1) {
                                mView.setRefreshData(wxNewsDetailBean);
                            } else {
                                mView.setLoadMore(wxNewsDetailBean);
                            }
                        }
                    }
                });
        mSubscriptions.add(subscribe);
    }

    /**
     * 开始缓存信息
     */
    @Override
    public void cacheData(List<WxNewsDetailBean.ResultBean.ListBean> list) {
        initRealm();
        if(realm!=null && realm.where(CacheFastLook.class).findAll()!=null){
            cacheFastLooks = realm.where(CacheFastLook.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheFastLooks.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for (int a=0 ; a<list.size() ; a++){
            CacheFastLook fastLook = realm.createObject(CacheFastLook.class);
            fastLook.setContent(fastLook.getContent());
            fastLook.setTitle(fastLook.getTitle());
            fastLook.setChannelid(fastLook.getChannelid());
            fastLook.setLikenum(fastLook.getLikenum());
            fastLook.setPic(fastLook.getPic());
            fastLook.setReadnum(fastLook.getReadnum());
            fastLook.setTime(fastLook.getTime());
            fastLook.setUrl(fastLook.getUrl());
            fastLook.setWeixinaccount(fastLook.getWeixinaccount());
            fastLook.setWeixinsummary(fastLook.getWeixinsummary());
            fastLook.setWeixinname(fastLook.getWeixinname());
        }
        realm.commitTransaction();
    }

    /**
     * 读取缓存信息
     */
    @Override
    public void readCache() {
        initRealm();
        if(realm!=null && realm.where(CacheFastLook.class).findAll()!=null){
            cacheFastLooks = realm.where(CacheFastLook.class).findAll();
        }else {
            return;
        }
        ArrayList<WxNewsDetailBean.ResultBean.ListBean> list = new ArrayList<>();
        for(int a=0 ; a<cacheFastLooks.size() ; a++){
            WxNewsDetailBean.ResultBean.ListBean listBean = new WxNewsDetailBean.ResultBean.ListBean();
            listBean.setContent(cacheFastLooks.get(a).getContent());
            listBean.setTitle(cacheFastLooks.get(a).getTitle());
            listBean.setChannelid(cacheFastLooks.get(a).getChannelid());
            listBean.setLikenum(cacheFastLooks.get(a).getLikenum());
            listBean.setPic(cacheFastLooks.get(a).getPic());
            listBean.setReadnum(cacheFastLooks.get(a).getReadnum());
            listBean.setTime(cacheFastLooks.get(a).getTime());
            listBean.setUrl(cacheFastLooks.get(a).getUrl());
            listBean.setWeixinaccount(cacheFastLooks.get(a).getWeixinaccount());
            listBean.setWeixinsummary(cacheFastLooks.get(a).getWeixinsummary());
            listBean.setWeixinname(cacheFastLooks.get(a).getWeixinname());
            list.add(listBean);
        }
        if(list.size()>0){
            mView.readCacheData(list);
        }
    }


}
