package com.ns.yc.lifehelper.ui.other.vtex.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeBean;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeListBean;

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
