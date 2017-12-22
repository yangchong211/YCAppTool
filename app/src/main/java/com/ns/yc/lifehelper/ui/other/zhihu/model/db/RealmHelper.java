package com.ns.yc.lifehelper.ui.other.zhihu.model.db;

import com.ns.yc.lifehelper.cache.CacheZhLike;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ReadStateBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {


    /**
     * 增加 阅读记录
     * @param id
     * 使用@PrimaryKey注解后copyToRealm需要替换为copyToRealmOrUpdate
     */
    public static void insertNewsId(Realm mRealm , int id) {
        ReadStateBean bean = new ReadStateBean();
        bean.setId(id);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }

    /**
     * 查询 阅读记录
     * @param id
     * @return
     */
    public static boolean queryNewsId(Realm mRealm ,int id) {
        RealmResults<ReadStateBean> results = mRealm.where(ReadStateBean.class).findAll();
        for(ReadStateBean item : results) {
            if(item.getId() == id) {
                return true;
            }
        }
        return false;
    }


    /**
     * 增加 收藏记录
     * @param bean
     */
    public static void insertLikeBean(Realm mRealm , CacheZhLike bean) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }

    /**
     * 删除 收藏记录
     * @param id
     */
    public static void deleteLikeBean(Realm mRealm , String id) {
        CacheZhLike data = mRealm.where(CacheZhLike.class)
                .equalTo("id",id)
                .findFirst();
        mRealm.beginTransaction();
        if (data != null) {
            data.deleteFromRealm();
        }
        mRealm.commitTransaction();
    }

    /**
     * 查询 收藏记录
     * @param id
     * @return
     */
    public static boolean queryLikeId(Realm mRealm , String id) {
        RealmResults<CacheZhLike> results = mRealm.where(CacheZhLike.class).findAll();
        for(CacheZhLike item : results) {
            if(item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static List<CacheZhLike> getLikeList(Realm mRealm) {
        //使用findAllSort ,先findAll再result.sort无效
        RealmResults<CacheZhLike> results = mRealm.where(CacheZhLike.class).findAllSorted("time");
        return mRealm.copyFromRealm(results);
    }

    /**
     * 修改 收藏记录 时间戳以重新排序
     * @param id
     * @param time
     * @param isPlus
     */
    public static void changeLikeTime(Realm mRealm , String id ,long time, boolean isPlus) {
        CacheZhLike bean = mRealm.where(CacheZhLike.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        if (isPlus) {
            bean.setTime(++time);
        } else {
            bean.setTime(--time);
        }
        mRealm.commitTransaction();
    }


    /**
     * 更新 掘金首页管理列表
     * @param bean
     */
    public static void updateGoldManagerList(Realm mRealm , GoldManagerBean bean) {
        GoldManagerBean data = mRealm.where(GoldManagerBean.class).findFirst();
        mRealm.beginTransaction();
        if (data != null) {
            data.deleteFromRealm();
        }
        mRealm.copyToRealm(bean);
        mRealm.commitTransaction();
    }

    /**
     * 获取 掘金首页管理列表
     * @return
     */
    public static GoldManagerBean getGoldManagerList(Realm mRealm) {
        GoldManagerBean bean = mRealm.where(GoldManagerBean.class).findFirst();
        if (bean == null){
            return null;
        }
        return mRealm.copyFromRealm(bean);
    }

}
