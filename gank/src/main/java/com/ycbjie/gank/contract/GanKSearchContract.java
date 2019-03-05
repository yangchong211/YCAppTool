package com.ycbjie.gank.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.gank.bean.cache.CacheSearchHistory;
import com.ycbjie.gank.bean.bean.SearchResult;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营搜索桥梁
 * 修订历史：
 * ================================================
 */
public interface GanKSearchContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //设置指针颜色
        void setEditTextCursorColor(int cursorColor);
        //展示搜索结果
        void showSearchResult();
        //展示搜索历史记录
        void showSearchHistory();
        //设置搜索历史
        void setHistory(List<CacheSearchHistory> history);
        //展示消息
        void showTip(String msg);
        //隐藏加载
        void hideLoading();
        //设置为空
        void setEmpty();
        //设置加载
        void setLoading();
        //设置数据
        void setSearchItems(SearchResult searchResult);
        //添加数据
        void addSearchItems(SearchResult searchResult);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //开始搜索
        void search(String searchText, boolean isLoadMore);
        //清除所有历史记录
        void deleteAllHistory();
    }


}
