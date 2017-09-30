package com.ns.yc.lifehelper.ui.other.weather.api;

import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherAirLive;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherForecast;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherLive;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by PC on 2017/9/14.
 * 作者：PC
 */

public interface WeatherApi {

    /**
     * 获取指定城市的实时天气
     * <p>
     * API地址：http://service.envicloud.cn:8082/v2/weatherlive/YMFYB256AGFUZZE0ODQ3MZM1MZE2NTU=/101020100
     *
     * @param cityId 城市id
     * @return Observable
     */
    @GET("/v2/weatherlive/YMFYB256AGFUZZE0ODQ3MZM1MZE2NTU=/{cityId}")
    Observable<WeatherLive> getWeatherLive(@Path("cityId") String cityId);

    /**
     * 获取指定城市7日天气预报
     * <p>
     * API地址：http://service.envicloud.cn:8082/v2/weatherforecast/YMFYB256AGFUZZE0ODQ3MZM1MZE2NTU=/101020100
     *
     * @param cityId 城市id
     * @return Observable
     */
    @GET("/v2/weatherforecast/YMFYB256AGFUZZE0ODQ3MZM1MZE2NTU=/{cityId}")
    Observable<WeatherForecast> getWeatherForecast(@Path("cityId") String cityId);

    /**
     * 获取指定城市的实时空气质量
     * <p>
     * API地址：http://service.envicloud.cn:8082/v2/cityairlive/YMFYB256AGFUZZE0ODQ3MZM1MZE2NTU=/101020100
     *
     * @param cityId 城市id
     * @return Observable
     */
    @GET("/v2/cityairlive/YMFYB256AGFUZZE0ODQ3MZM1MZE2NTU=/{cityId}")
    Observable<WeatherAirLive> getWeatherAir(@Path("cityId") String cityId);
}
