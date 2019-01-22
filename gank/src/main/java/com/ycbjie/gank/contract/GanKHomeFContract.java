package com.ycbjie.gank.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.gank.bean.bean.CategoryResult;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营首页桥梁
 * 修订历史：
 * ================================================
 */
public interface GanKHomeFContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //开始刷新，loading
        void showSwipeLoading();
        //隐藏刷新，loading
        void hideSwipeLoading();
        //没有数据
        void showNoData();
        //网络错误
        void showNetError();
        //获取干货接口类型
        String getDataType();
        //刷新数据
        void refreshData(CategoryResult categoryResult);
        //加载更多数据
        void moreData(CategoryResult categoryResult);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getData(boolean isRefresh);
    }


}
