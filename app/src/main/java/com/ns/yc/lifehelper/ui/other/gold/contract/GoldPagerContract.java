package com.ns.yc.lifehelper.ui.other.gold.contract;


import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface GoldPagerContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void showContent(List<GoldListBean> totalList);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getGoldData(String mType);
    }


}
