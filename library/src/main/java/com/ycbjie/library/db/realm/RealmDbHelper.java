package com.ycbjie.library.db.realm;

import com.ycbjie.library.db.cache.CacheZhLike;
import com.ycbjie.library.db.cache.ReadStateBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2017/5/14
 *     desc         数据库帮助类
 *     revise
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public class RealmDbHelper {


    private static RealmDbHelper mRealmDbHelper;

    private RealmDbHelper() {

    }

    /**
     * 获取单利对象
     * @return          对象
     */
    public static RealmDbHelper getInstance() {
        if (mRealmDbHelper == null) {
            synchronized (RealmDbHelper.class) {
                if (mRealmDbHelper == null) {
                    mRealmDbHelper = new RealmDbHelper();
                }
            }
        }
        return mRealmDbHelper;
    }

    /**
     * 在onDestroy方法中调用该方法，避免静态对象内存泄漏
     */
    public void close() {
        if(mRealmDbHelper !=null){
            mRealmDbHelper = null;
        }
    }

    /*-------------------------------------知乎新闻-----------------------------------------------*/

    /**
     * 增加 阅读记录
     * @param id
     * 使用@PrimaryKey注解后copyToRealm需要替换为copyToRealmOrUpdate
     */
    public void insertNewsId(int id) {
        Realm mRealm = Realm.getDefaultInstance();
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
    public boolean queryNewsId(int id) {
        Realm mRealm = Realm.getDefaultInstance();
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
    public void insertLikeBean(CacheZhLike bean) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }

    /**
     * 删除 收藏记录
     * @param id
     */
    public void deleteLikeBean(String id) {
        Realm mRealm = Realm.getDefaultInstance();
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
    public boolean queryLikeId(String id) {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<CacheZhLike> results = mRealm.where(CacheZhLike.class).findAll();
        for(CacheZhLike item : results) {
            if(item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public List<CacheZhLike> getLikeList() {
        Realm mRealm = Realm.getDefaultInstance();
        //使用findAllSort ,先findAll再result.sort无效
        RealmResults<CacheZhLike> results = mRealm.where(CacheZhLike.class).findAll();
        return mRealm.copyFromRealm(results);
    }

    /**
     * 修改 收藏记录 时间戳以重新排序
     * @param id
     * @param time
     * @param isPlus
     */
    public void changeLikeTime(String id ,long time, boolean isPlus) {
        Realm mRealm = Realm.getDefaultInstance();
        CacheZhLike bean = mRealm.where(CacheZhLike.class)
                .equalTo("id", id)
                .findFirst();
        mRealm.beginTransaction();
        if (isPlus) {
            bean.setTime(++time);
        } else {
            bean.setTime(--time);
        }
        mRealm.commitTransaction();
    }




}
