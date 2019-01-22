package com.ycbjie.news.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.news.model.WeChatBean;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：微信新闻
 * 修订历史：
 * ================================================
 */
public interface TxWeChatContract {


    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void setErrorView();
        void setNetworkErrorView();
        void setView(List<WeChatBean.NewslistBean> newslist);
        void setEmptyView();
        void setViewMore(List<WeChatBean.NewslistBean> newslist);
        void stopMore();
    }


    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getNews(int num, int page);
    }


}
