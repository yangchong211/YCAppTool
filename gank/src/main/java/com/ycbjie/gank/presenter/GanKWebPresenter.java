package com.ycbjie.gank.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.ycbjie.gank.bean.cache.CacheGanKFavorite;
import com.ycbjie.gank.contract.GanKWebContract;
import com.ycbjie.library.db.realm.RealmUtils;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 干货集中营详情页面
 *     revise:
 * </pre>
 */
public class GanKWebPresenter implements GanKWebContract.Presenter {

    private GanKWebContract.View mView;
    private CompositeSubscription mSubscriptions;
    private String mUrl;
    private boolean mIsFavorite;
    private CacheGanKFavorite favoriteData;
    private Realm realm;
    private RealmResults<CacheGanKFavorite> ganKFavorites;


    public GanKWebPresenter(GanKWebContract.View view) {
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        initView();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    private void initView() {
        // 设置Fab的背景色
        mView.setFabColor();
        //获取收藏序列化内存
        favoriteData = mView.getFavoriteData();
        //查看数据库是否是收藏内容
        findHasFavorite();
        //加载内容
        loadData();
        //加载数据库
        initRealm();
    }

    private void initRealm() {
        if(realm==null){
            realm = RealmUtils.getRealmHelper();
        }
    }


    private void loadData() {
        mUrl = mView.getLoadUrl();
        mView.loadURL(mUrl);
    }


    /**
     * 处理收藏逻辑
     */
    @Override
    public void meFavorite() {
        if (mIsFavorite) {          // 已经收藏
            unFavorite();
        } else {                    // 未收藏
            favorite();
        }
    }


    /**
     * 收藏
     */
    private void favorite() {
        favoriteData.setCreatetime(System.currentTimeMillis());
        initRealm();
        if(realm!=null && realm.where(CacheGanKFavorite.class)!=null){
            ganKFavorites = realm.where(CacheGanKFavorite.class).findAll();
        }else {
            ToastUtils.showShort("收藏失败,请重试");
            return;
        }
        realm.beginTransaction();
        //RealmException: 'GanKFavorite' has a primary key, use 'createObject(Class<E>, Object)' instead.
        //GanKFavorite favorite = realm.createObject(GanKFavorite.class);
        //Primary key field 'gankID' cannot be changed after object was created.
        //GanKFavorite favorite = realm.createObject(GanKFavorite.class , UUID.randomUUID().toString());
        //GanKFavorite favorite = realm.createObject(GanKFavorite.class,"gankID");
        CacheGanKFavorite favorite = realm.createObject(CacheGanKFavorite.class);
        favorite.setTitle(favoriteData.getTitle());
        favorite.setUrl(favoriteData.getUrl());
        favorite.setType(favoriteData.getType());
        favorite.setGankID(favoriteData.getGankID());
        favorite.setAuthor(favoriteData.getAuthor());
        favorite.setCreatetime(favoriteData.getCreatetime());
        favorite.setData(favoriteData.getData());
        realm.copyToRealm(favorite);
        realm.commitTransaction();
        //mIsFavorite = favoriteData.isManaged();
        //判断数据库是否加载数据成功
        mIsFavorite = favoriteData.isLoaded();
        mView.setFavoriteState(mIsFavorite);
        if (!mIsFavorite) {
            ToastUtils.showShort("收藏失败,请重试");
        }else {
            ToastUtils.showShort("收藏成功");
        }
    }


    /**
     * 取消收藏
     */
    private void unFavorite() {
        initRealm();
        if(realm!=null && realm.where(CacheGanKFavorite.class).findAll()!=null){
            mIsFavorite = realm.where(CacheGanKFavorite.class)
                    .equalTo("gankID", favoriteData.getGankID())
                    .findAll()
                    .deleteAllFromRealm();
        }else {
            ToastUtils.showShort("取消收藏失败,请重试");
            return;
        }
        // 不调用这句保存 在保存会失败，并且返回的是true
        favoriteData.deleteFromRealm();
        mIsFavorite = favoriteData.isLoaded();
        mView.setFavoriteState(mIsFavorite);
        if (mIsFavorite) {
            ToastUtils.showShort("取消收藏失败,请重试");
        }
    }


    /**
     * 是否有收藏
     */
    private void findHasFavorite() {
        if (favoriteData == null) {
            // 隐藏收藏 fab
            mView.hideFavoriteFab();
            return;
        }
        initRealm();
        if(realm!=null && realm.where(CacheGanKFavorite.class)!=null){
            RealmResults<CacheGanKFavorite> favorites = realm.where(CacheGanKFavorite.class)
                    .equalTo("gankID", favoriteData.getGankID())
                    .findAll();
            mIsFavorite = favorites.size() > 0;
            mView.setFavoriteState(mIsFavorite);
        }
    }


}
