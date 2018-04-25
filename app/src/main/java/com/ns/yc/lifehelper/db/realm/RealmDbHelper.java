package com.ns.yc.lifehelper.db.realm;

import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.db.cache.CacheZhLike;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ReadStateBean;

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


    private static volatile RealmDbHelper mRealmDbHelper;
    private Realm mRealm;

    private RealmDbHelper() {
        initRealm();
    }

    private void initRealm() {
        if(mRealm ==null){
            mRealm = BaseApplication.getInstance().getRealmHelper();
        }
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
        if(mRealm == null){
            initRealm();
        }
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
        if(mRealm == null){
            initRealm();
        }
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
        if(mRealm == null){
            initRealm();
        }
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }

    /**
     * 删除 收藏记录
     * @param id
     */
    public void deleteLikeBean(String id) {
        if(mRealm == null){
            initRealm();
        }
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
        if(mRealm == null){
            initRealm();
        }
        RealmResults<CacheZhLike> results = mRealm.where(CacheZhLike.class).findAll();
        for(CacheZhLike item : results) {
            if(item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public List<CacheZhLike> getLikeList() {
        if(mRealm == null){
            initRealm();
        }
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
    public void changeLikeTime(String id ,long time, boolean isPlus) {
        if(mRealm == null){
            initRealm();
        }
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


    /**
     * 更新 掘金首页管理列表
     * @param bean
     */
    public void updateGoldManagerList(GoldManagerBean bean) {
        if(mRealm == null){
            initRealm();
        }
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
    public GoldManagerBean getGoldManagerList() {
        if(mRealm == null){
            initRealm();
        }
        GoldManagerBean bean = mRealm.where(GoldManagerBean.class).findFirst();
        if (bean == null){
            return null;
        }
        return mRealm.copyFromRealm(bean);
    }


}
