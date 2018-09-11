package com.ns.yc.lifehelper.ui.find.contract;


import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.ns.yc.lifehelper.base.adapter.BaseDelegateAdapter;
import com.ns.yc.lifehelper.base.mvp.BasePresenter;
import com.ns.yc.lifehelper.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.yc.cn.ycbannerlib.BannerView;

import java.util.List;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/21
 * 描    述：数据页面
 * 修订历史：
 * ================================================
 */
public interface FindFragmentContract {


    interface View extends BaseView {
        void setBanner(BannerView mBanner, List<Object> arrayList);
        void setOnclick(int position);
        void setMarqueeClick(int position);
        void setGridClick(int position);
        void setGridClickThird(int position);
        void setGridClickFour(int position);
        void setNewsList2Click(int position, String url);
        void setNewsList5Click(int position , String url);
    }


    interface Presenter extends BasePresenter {
        DelegateAdapter initRecyclerView(RecyclerView recyclerView);
        BaseDelegateAdapter initBannerAdapter();
        BaseDelegateAdapter initGvMenu();
        BaseDelegateAdapter initMarqueeView();
        BaseDelegateAdapter initTitle(String title);
        BaseDelegateAdapter initList1();
        BaseDelegateAdapter initList2();
        BaseDelegateAdapter initList3();
        BaseDelegateAdapter initList4();
        BaseDelegateAdapter initList5();
        void bindActivity(MainActivity activity);
    }


}
