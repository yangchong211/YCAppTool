package com.ns.yc.lifehelper.ui.other.weather.model;

import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.api.WeatherApi;
import com.ns.yc.lifehelper.ui.other.weather.bean.FiftyDayWeather;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherAirLive;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherForecast;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherLive;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class WeathersModel {

    private static WeathersModel bookModel;
    private WeatherApi mApiService;

    public WeathersModel(String url) {
        mApiService = RetrofitWrapper
                .getInstance(url)
                .create(WeatherApi.class);
    }

    public static WeathersModel getInstance(String url){
        if(bookModel == null) {
            bookModel = new WeathersModel(url);
        }
        return bookModel;
    }

    public Observable<WeatherLive> getWeatherLive(String id) {
        Observable<WeatherLive> weatherLive = mApiService.getWeatherLive(id);
        return weatherLive;
    }

    public Observable<WeatherForecast> getWeatherForecast(String id) {
        Observable<WeatherForecast> weatherForecast = mApiService.getWeatherForecast(id);
        return weatherForecast;
    }

    public Observable<WeatherAirLive> getWeatherAir(String id) {
        Observable<WeatherAirLive> weatherAir = mApiService.getWeatherAir(id);
        return weatherAir;
    }

    /**
     * id或地名查询7天预报详情
     */
    public Observable<FiftyDayWeather> getFiftyWeather(String token , String area , String areaid ) {
        Observable<FiftyDayWeather> weather = mApiService.getFiftyWeather(token, area, areaid);
        return weather;
    }


}
