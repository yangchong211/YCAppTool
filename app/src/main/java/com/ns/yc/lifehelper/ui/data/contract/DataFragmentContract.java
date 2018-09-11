package com.ns.yc.lifehelper.ui.data.contract;


import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.model.bean.ImageIconBean;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/18
 * 描    述：工具页面
 * 修订历史：
 *         v1.5 17年10月3日修改
 * ================================================
 */
public interface DataFragmentContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void setGridView(String[] toolName, ArrayList<Integer> logoList);
        void setRecycleView(ArrayList<Integer> list);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        List<ImageIconBean> getVpData();
        void initGridViewData();
        void initRecycleViewData();
        void bindActivity(MainActivity activity);
    }


}
