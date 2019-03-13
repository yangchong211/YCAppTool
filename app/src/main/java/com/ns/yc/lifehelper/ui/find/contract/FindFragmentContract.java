package com.ns.yc.lifehelper.ui.find.contract;


import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.ns.yc.lifehelper.base.adapter.BaseDelegateAdapter;
import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.yc.cn.ycbannerlib.banner.BannerView;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 数据页面，使用阿里巴巴Vlayout框架
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
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
