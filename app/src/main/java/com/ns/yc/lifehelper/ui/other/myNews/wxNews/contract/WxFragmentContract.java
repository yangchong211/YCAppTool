package com.ns.yc.lifehelper.ui.other.myNews.wxNews.contract;


import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsDetailBean;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：微信新闻页面
 * 修订历史：
 *      v1.5 修改于2017年10月9日
 * ================================================
 */
public interface WxFragmentContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void setAdapterData(List<WxNewsDetailBean.ResultBean.ListBean> list);
        void setEmptyView();
        void setAdapterDataMore(List<WxNewsDetailBean.ResultBean.ListBean> list);
        void stopMore();
        void setErrorView();
        void setNetworkErrorView();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getWxNews(String mType, int num, int start);
    }


}
