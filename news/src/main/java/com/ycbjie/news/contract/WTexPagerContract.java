package com.ycbjie.news.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.news.model.TopicListBean;

import java.util.List;


public interface WTexPagerContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void showContent(List<TopicListBean> topicListBeen);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getData(String mType);
    }


}
