package com.ycbjie.gank.contract;


import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营首页桥梁
 * 修订历史：
 * ================================================
 */
public interface GanKHomeAContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //设置图片
        void setBanner(String imgUrl);
        //开始加载动画
        void startBannerLoadingAnim();
        //停止加载动画
        void stopBannerLoadingAnim();
        //设置可以点击
        void enableFabButton();
        //设置不可点击
        void disEnableFabButton();
        //设置图片加载失败
        void errorImage();
        //设置FabButton 的背景色
        void setFabButtonColor(int color);
        //缓存图片
        void cacheImg(String imgUrl);
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //处理获取随机的图片
        void getRandomBanner();
        //保存缓存图片
        void saveCacheImgUrl(String url);
    }


}
