package com.ns.yc.lifehelper.ui.other.gank.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营详情页面桥梁
 * 修订历史：
 * ================================================
 */
public interface GanKWebContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //设置fab按钮的颜色
        void setFabColor();
        //获取收藏序列化内存
        GanKFavorite getFavoriteData();
        //获取加载url
        String getLoadUrl();
        //加载内容
        void loadURL(String url);
        //设置收藏的状态
        void setFavoriteState(boolean isFavorite);
        //隐藏fab按钮
        void hideFavoriteFab();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //处理收藏的逻辑
        void meFavorite();
    }


}
