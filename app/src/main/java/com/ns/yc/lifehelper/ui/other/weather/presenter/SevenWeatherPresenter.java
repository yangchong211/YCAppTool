package com.ns.yc.lifehelper.ui.other.weather.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.ui.other.weather.bean.FiftyDayWeather;
import com.ns.yc.lifehelper.ui.other.weather.contract.SevenWeatherContract;
import com.ns.yc.lifehelper.ui.other.weather.model.EventCenter;
import com.ns.yc.lifehelper.ui.other.weather.model.WeathersModel;
import com.ns.yc.lifehelper.ui.other.weather.view.weather.BaseDrawer;
import com.ns.yc.lifehelper.utils.EventBusUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/3
 * 描    述：7日天气页面
 * 修订历史：
 * ================================================
 */
public class SevenWeatherPresenter implements SevenWeatherContract.Presenter {

    private SevenWeatherContract.View mView;
    private CompositeSubscription mSubscriptions;
    private BaseDrawer.Type mDrawerType = BaseDrawer.Type.DEFAULT;
    private Context context;

    public SevenWeatherPresenter(SevenWeatherContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        context = mView.getActivity();
        mView.setWeatherBg(BaseDrawer.Type.DEFAULT);
        mView.startLoading();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void getData() {
        WeathersModel model = WeathersModel.getInstance(ConstantALiYunApi.BASE_WEATHER_URL);
        model.getFiftyWeather(ConstantALiYunApi.Key, "北京", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FiftyDayWeather>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FiftyDayWeather weather) {
                        if (weather.getShowapi_res_body() != null) {
                            FiftyDayWeather.ShowapiResBodyBean body = weather.getShowapi_res_body();
                            List<FiftyDayWeather.ShowapiResBodyBean.DayListBean> dayList = body.getDayList();
                            mView.setWeatherList(dayList);
                        }
                    }
                });
    }

    @Override
    public void changeWeatherBg() {
        //弹出dialog显示天气样式选择
        final BaseDrawer.Type tempDrawer = mDrawerType;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ArrayList<String> strings = new ArrayList<>();
        for (BaseDrawer.Type t : BaseDrawer.Type.values()) {
            strings.add(t.toString());
        }
        int index = 0;
        for (int i = 0; i < BaseDrawer.Type.values().length; i++) {
            if (BaseDrawer.Type.values()[i] == mDrawerType) {
                index = i;
                break;
            }
        }
        builder.setSingleChoiceItems(strings.toArray(new String[]{}), index,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDrawerType = BaseDrawer.Type.values()[which];
                        if (tempDrawer != mDrawerType) {
                            EventBusUtils.post(new EventCenter(100,mDrawerType));
                        }
                        dialog.dismiss();

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
}
