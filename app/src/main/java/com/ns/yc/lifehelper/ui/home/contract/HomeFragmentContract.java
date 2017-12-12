package com.ns.yc.lifehelper.ui.home.contract;


import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;
import com.ns.yc.lifehelper.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.ns.yc.lifehelper.ui.weight.pileCard.ItemEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/21
 * 描    述：Home主页面
 * 修订历史：
 *
 *       v1.5 修改于11月3日，改写代码为MVP架构
 * ================================================
 */
public interface HomeFragmentContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void setNewsData(List<HomeBlogEntity> list);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getHomeNewsData();
        List<CharSequence> getMarqueeTitle();
        void bindView(MainActivity activity);
        ArrayList<ItemEntity> getHomePileData();
        List<Integer> getBannerData();
    }


}
