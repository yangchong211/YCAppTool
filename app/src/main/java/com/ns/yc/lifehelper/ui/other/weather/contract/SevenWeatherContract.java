package com.ns.yc.lifehelper.ui.other.weather.contract;


import android.content.Context;

import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;
import com.ns.yc.lifehelper.ui.other.weather.bean.FiftyDayWeather;
import com.ns.yc.lifehelper.ui.other.weather.view.weather.BaseDrawer;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/3
 * 描    述：7日天气页面
 * 修订历史：
 * ================================================
 */
public interface SevenWeatherContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        //设置七日天气数据
        void setWeatherList(List<FiftyDayWeather.ShowapiResBodyBean.DayListBean> dayList);
        //获取上下文
        Context getActivity();
        //设置背景
        void setWeatherBg(BaseDrawer.Type aDefault);
        void startLoading();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //获取数据
        void getData();
        //改变天气背景
        void changeWeatherBg();
    }


}
