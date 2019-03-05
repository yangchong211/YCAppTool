package com.ns.yc.lifehelper.ui.me.presenter;

import com.ns.yc.lifehelper.ui.me.contract.MeGanKCollectContract;
import com.ycbjie.gank.bean.cache.CacheGanKFavorite;
import com.ycbjie.library.base.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/12
 *     desc  : 干货集中营
 *     revise:
 * </pre>
 */
public class MeGanKCollectPresenter implements MeGanKCollectContract.Presenter {

    private MeGanKCollectContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Realm realm;

    public MeGanKCollectPresenter(MeGanKCollectContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        initRealm();
    }

    private void initRealm() {
        if(realm==null){
            realm = AppConfig.INSTANCE.getRealmHelper();
        }
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    /**
     * 获取收藏数据
     */
    @Override
    public void getCollectData(boolean isRefresh) {
        initRealm();
        RealmResults<CacheGanKFavorite> ganKFavorites;
        if(realm!=null && realm.where(CacheGanKFavorite.class).findAll()!=null){
            ganKFavorites = realm.where(CacheGanKFavorite.class).findAll();
        }else {
            return;
        }
        List<CacheGanKFavorite> list = new ArrayList<>();
        for(int a=0 ; a<ganKFavorites.size() ; a++){
            CacheGanKFavorite favorite = new CacheGanKFavorite();
            favorite.setTitle(ganKFavorites.get(a).getTitle());
            favorite.setAuthor(ganKFavorites.get(a).getAuthor());
            favorite.setCreatetime(ganKFavorites.get(a).getCreatetime());
            favorite.setData(ganKFavorites.get(a).getData());
            favorite.setGankID(ganKFavorites.get(a).getGankID());
            favorite.setType(ganKFavorites.get(a).getType());
            favorite.setUrl(ganKFavorites.get(a).getUrl());
            list.add(favorite);
        }
        if(list.size()>0){
            mView.setDataList(list);
        }else {
            mView.setEmpty();
        }
    }
}
