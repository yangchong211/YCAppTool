package com.ns.yc.lifehelper.ui.me.contract;


import android.app.Activity;

import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ns.yc.lifehelper.model.bean.UpdateBean;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：设置中心页桥梁
 * 修订历史：
 * ================================================
 */
public interface MeSettingContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //开始loading加载
        void startLoading();
        //初始化是否显示缩略图开关的状态
        void changeSwitchState(boolean isChecked);
        //启动页是否显示妹子图
        void changeIsShowLauncherImgSwitchState(boolean isChecked);
        //启动页是否概率出现
        void changeIsAlwaysShowLauncherImgSwitchState(boolean isChecked);
        //设置缩略图不可点击
        void setImageQualityChooseUnEnable();
        //设置缩略图可点击
        void setImageQualityChooseEnable();
        //设置随机选项不可点击
        void setLauncherImgProbabilityUnEnable();
        //设置随机选项可点击
        void setLauncherImgProbabilityEnable();
        //设置缩略图质量
        void setThumbnailQualityInfo(int quality);
        //设置缓存的大小
        void showCacheSize();
        //设置清理缓存后的文本
        void setClearText();
        void setErrorView(Throwable e);
        void showUpdateDialog(UpdateBean updateBean);
        void finishActivity();
        void exitLogout();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //跳转应用市场
        void goToStar(Activity activity);
        //弹出缩略图弹窗
        void showPicQualityDialog(Activity activity);
        //设置是否展示list图片
        void saveIsListShowImg(boolean isListShowImg);
        //设置启动页是否显示妹子图
        void saveIsLauncherShowImg(boolean isLauncherShowImg);
        //设置启动页是否概率出现
        void saveIsLauncherAlwaysShowImg(boolean isLauncherAlwaysShowImg);
        //设置清理缓存
        void cleanAppCache(Activity activity);
        //版本更新
        void checkVersion(String appVersionName);
        void exitLogout();
    }


}
