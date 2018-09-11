package com.ns.yc.lifehelper.ui.other.vtex.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.TopicListBean;

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
