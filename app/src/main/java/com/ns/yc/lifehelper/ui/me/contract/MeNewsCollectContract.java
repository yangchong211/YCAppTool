package com.ns.yc.lifehelper.ui.me.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.db.cache.CacheZhLike;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface MeNewsCollectContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void showContent(List<CacheZhLike> likeList);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getLikeData();
        void initRealm();
        void deleteLikeData(String id);
        void changeLikeTime(String id, long time, boolean isPlus);
    }


}
