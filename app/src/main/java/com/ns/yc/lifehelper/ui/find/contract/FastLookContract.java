package com.ns.yc.lifehelper.ui.find.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.model.bean.WxNewsDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/14
 * 描    述：快看页面
 * 修订历史：
 * ================================================
 */
public interface FastLookContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //刷新数据
        void setRefreshData(WxNewsDetailBean wxNewsDetailBean);
        //加载更多数据
        void setLoadMore(WxNewsDetailBean wxNewsDetailBean);
        //设置加载错误
        void setError();
        //读取缓存数据
        void readCacheData(ArrayList<WxNewsDetailBean.ResultBean.ListBean> list);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //获取数据
        void getData(boolean isRefresh);
        //缓存数据
        void cacheData(List<WxNewsDetailBean.ResultBean.ListBean> list);
        //读取缓存
        void readCache();
    }


}
