package com.ycbjie.video.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.video.model.MultiNewsArticleDataBean;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : VideoArticleContract
 *     revise:
 * </pre>
 */
public interface VideoArticleContract {


    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        void showRecyclerView();
        void setDataView(List<MultiNewsArticleDataBean> dataList);
        void showErrorView();
    }


    //Presenter控制器
    interface Presenter extends BasePresenter {
        void getVideoData(String... category);
        void doRefresh();
        void doLoadMoreData();
    }


}
