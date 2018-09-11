package com.ns.yc.lifehelper.ui.other.zhihu.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionChildBean;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        日报
 * 修订历史：
 * ================================================
 */
public interface ZhiHuSectionListContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void setView(ZhiHuSectionChildBean zhiHuSectionChildBean);
        void setEmptyView();
        void setNetworkErrorView();
        void setErrorView();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getSectionChildData(int id);
        void insertReadToDB(int id);
    }


}
