package com.yc.yc.lifehelper.ui.home.contract;


import android.app.Activity;
import android.graphics.Bitmap;
import com.yc.configlayer.bean.HomeBlogEntity;
import com.yc.library.base.mvp.BasePresenter;
import com.yc.library.base.mvp.BaseView;

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

    interface View extends BaseView {
        void setNewsData(List<HomeBlogEntity> list);
        void downloadBitmapSuccess(ArrayList<Bitmap> bitmapList);
    }

    interface Presenter extends BasePresenter {
        void getHomeNewsData();
        ArrayList<String> getMarqueeTitle();
        List<Object> getBannerData();
        void bindActivity(Activity activity);
        void getGalleryData();
    }


}
