package com.ns.yc.lifehelper.ui.other.bookReader.manager;

import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderRecommendBean;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.RefreshCollectionListEvent;
import com.ns.yc.lifehelper.utils.cache.ACache;
import com.ns.yc.lifehelper.utils.EventBusUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：22017/4/29
 * 描    述：收藏列表管理
 * 修订历史：
 * ================================================
 */
public class CollectionsManager {

    private volatile static CollectionsManager singleton;
    private CollectionsManager() {}
    public static CollectionsManager getInstance() {
        if (singleton == null) {
            synchronized (CollectionsManager.class) {
                if (singleton == null) {
                    singleton = new CollectionsManager();
                }
            }
        }
        return singleton;
    }


    /**
     * 加入收藏
     *
     * @param bean
     */
    public boolean add(ReaderRecommendBean.RecommendBooks bean) {
        if (isCollected(bean._id)) {
            return false;
        }
        List<ReaderRecommendBean.RecommendBooks> list = getCollectionList();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(bean);
        putCollectionList(list);
        RefreshCollectionListEvent refreshCollectionListEvent = new RefreshCollectionListEvent();
        EventBusUtils.post(refreshCollectionListEvent);
        return true;
    }

    /**
     * 是否已收藏
     * @param bookId
     * @return
     */
    public boolean isCollected(String bookId) {
        List<ReaderRecommendBean.RecommendBooks> list = getCollectionList();
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (ReaderRecommendBean.RecommendBooks bean : list) {
            if (bean._id.equals(bookId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取收藏列表
     *
     * @return
     */
    public List<ReaderRecommendBean.RecommendBooks> getCollectionList() {
        Object collection = ACache.get(new File(ConstantZssqApi.PATH_COLLECT)).getAsObject("collection");
        List<ReaderRecommendBean.RecommendBooks> list = (ArrayList<ReaderRecommendBean.RecommendBooks>) collection;
        return list == null ? null : list;
    }

    /**
     * 缓存收藏列表
     * @param list
     */
    public void putCollectionList(List<ReaderRecommendBean.RecommendBooks> list) {
        ACache.get(new File(ConstantZssqApi.PATH_COLLECT)).put("collection", (Serializable) list);
    }

}
