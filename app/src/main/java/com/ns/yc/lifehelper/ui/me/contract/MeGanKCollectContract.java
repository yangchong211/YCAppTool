package com.ns.yc.lifehelper.ui.me.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货收藏
 * 修订历史：
 * ================================================
 */
public interface MeGanKCollectContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //加载数据为空
        void setEmpty();
        //设置数据
        void setDataList(List<GanKFavorite> favorites);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //获取收藏数据
        void getCollectData(boolean isRefresh);
    }


}
