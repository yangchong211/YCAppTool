package com.ycbjie.news.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.news.model.NodeBean;
import com.ycbjie.news.model.NodeListBean;

import java.util.List;


public interface WTNodeListContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void showContent(List<NodeListBean> nodeListBeen);
        void showTopInfo(NodeBean nodeBeen);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getContent(String nodeName);
        void getTopInfo(String nodeName);
    }


}
