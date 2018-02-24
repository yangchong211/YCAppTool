package com.ns.yc.lifehelper.ui.other.weather.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.Constant;
import com.ns.yc.lifehelper.api.constantApi.ConstantEnvicloudApi;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.weather.WeatherActivity;
import com.ns.yc.lifehelper.ui.other.weather.adapter.WeatherDetailAdapter;
import com.ns.yc.lifehelper.ui.other.weather.adapter.WeatherForecastAdapter;
import com.ns.yc.lifehelper.ui.other.weather.adapter.WeatherLifeIndexAdapter;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherAirLive;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherDetail;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherForecast;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherLive;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherSuggestion;
import com.ns.yc.lifehelper.api.http.weather.WeathersModel;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：天气页面
 * 修订历史：
 * ================================================
 */
public class WeatherFragment extends BaseFragment {

    @Bind(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @Bind(R.id.recycler_view_detail)
    RecyclerView recyclerViewDetail;
    @Bind(R.id.tv_forecast_title)
    TextView tvForecastTitle;
    @Bind(R.id.recycler_view_forecast)
    RecyclerView recyclerViewForecast;
    @Bind(R.id.tv_aqi_title)
    TextView tvAqiTitle;
    @Bind(R.id.tv_aqi)
    TextView tvAqi;
    @Bind(R.id.tv_quality)
    TextView tvQuality;
//    @Bind(R.id.indicator_view_aqi)
//    IndicatorView indicatorViewAqi;
    @Bind(R.id.tv_advice)
    TextView tvAdvice;
    @Bind(R.id.tv_city_rank)
    TextView tvCityRank;
    @Bind(R.id.cv_aqi)
    CardView cvAqi;
    @Bind(R.id.tv_index_title)
    TextView tvIndexTitle;
    @Bind(R.id.recycler_view_life_index)
    RecyclerView recyclerViewLifeIndex;
    @Bind(R.id.cv_index)
    CardView cvIndex;
    private WeatherActivity activity;

    private List<WeatherDetail> weatherDetails = new ArrayList<>();
    private List<WeatherForecast.ForecastBean> mWeatherForecasts = new ArrayList<>();
    private List<WeatherSuggestion> lifeIndices = new ArrayList<>();
    private WeatherDetailAdapter detailAdapter;
    private WeatherForecastAdapter forecastAdapter;
    private WeatherLifeIndexAdapter lifeIndexAdapter;
    private String id = "101010100";
    private ViewLoading mLoading;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (WeatherActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_weather_main;
    }

    @Override
    public void initView() {
        // 添加Loading
        mLoading = new ViewLoading(activity, Constant.loadingStyle,"") {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
        initRecycleViewDetail();
        initRecycleViewForecasts();
        initRecycleViewLifeIndex();
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getWeatherData(1);
        getWeatherData(2);
        getWeatherData(3);
    }

    private void initRecycleViewDetail() {
        //天气详情
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new GridLayoutManager(activity, 3));
        detailAdapter = new WeatherDetailAdapter(weatherDetails);
        recyclerViewDetail.setAdapter(detailAdapter);
        detailAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initRecycleViewForecasts() {
        //天气预报
        recyclerViewForecast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewForecast.setNestedScrollingEnabled(false);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(getActivity()));
        forecastAdapter = new WeatherForecastAdapter(mWeatherForecasts);
        recyclerViewForecast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewForecast.setAdapter(forecastAdapter);
        forecastAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initRecycleViewLifeIndex() {
        //生活指数
        recyclerViewLifeIndex.setNestedScrollingEnabled(false);
        recyclerViewLifeIndex.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        lifeIndexAdapter = new WeatherLifeIndexAdapter(activity, lifeIndices);
        recyclerViewLifeIndex.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLifeIndex.setAdapter(lifeIndexAdapter);
        lifeIndexAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void getWeatherData(int type) {
        WeathersModel model = WeathersModel.getInstance(ConstantEnvicloudApi.EnviCloudApi);
        switch (type){
            case 1:
                Observable<WeatherLive> weatherLive = model.getWeatherLive(id);
                weatherLive.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<WeatherLive>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(WeatherLive weatherLive) {
                                if(weatherLive!=null){
                                    activity.tvPublishTime.setText("发布时间"+ weatherLive.getUpdatetime());
                                    activity.tvWeather.setText(weatherLive.getPhenomena());

                                    weatherDetails.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "体感温度", weatherLive.getTemperature() + "°C"));
                                    weatherDetails.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "湿度", weatherLive.getHumidity() + "%"));
                                    weatherDetails.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "风级别", weatherLive.getWinddirect()+"/"+weatherLive.getWindspeed()));
                                    weatherDetails.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "降水量", weatherLive.getRain() + "mm"));
                                    weatherDetails.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "空气气压", weatherLive.getAirpressure()+"Mpa"));
                                    weatherDetails.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "天气", weatherLive.getPhenomena()));
                                    detailAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                break;
            case 2:
                Observable<WeatherForecast> weatherForecast = model.getWeatherForecast(id);
                weatherForecast.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<WeatherForecast>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(WeatherForecast weatherForecast) {
                                if(weatherForecast!=null){
                                    activity.toolBar.setTitle(weatherForecast.getCityname());
                                    activity.tvTemp.setText(weatherForecast.getForecast().get(0).getTmp().getMax());



                                    mWeatherForecasts.addAll(weatherForecast.getForecast());
                                    forecastAdapter.notifyDataSetChanged();

                                    WeatherForecast.SuggestionBean suggestion = weatherForecast.getSuggestion();
                                    lifeIndices.add(new WeatherSuggestion("1", "空气质量", suggestion.getAir().getBrf()));
                                    lifeIndices.add(new WeatherSuggestion("2", "舒适度", suggestion.getComf().getBrf()));
                                    lifeIndices.add(new WeatherSuggestion("3", "洗车", suggestion.getCw().getBrf()));
                                    lifeIndices.add(new WeatherSuggestion("4", "穿衣", suggestion.getDrs().getBrf()));
                                    lifeIndices.add(new WeatherSuggestion("5", "感冒", suggestion.getFlu().getBrf()));
                                    lifeIndices.add(new WeatherSuggestion("6", "运动", suggestion.getSport().getBrf()));
                                    lifeIndices.add(new WeatherSuggestion("7", "旅游", suggestion.getTrav().getBrf()));
                                    lifeIndices.add(new WeatherSuggestion("8", "紫外线", suggestion.getUv().getBrf()));
                                    lifeIndexAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                break;
            case 3:
                Observable<WeatherAirLive> weatherAir = model.getWeatherAir(id);
                weatherAir.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<WeatherAirLive>() {
                            @Override
                            public void onCompleted() {
                                if (mLoading != null && mLoading.isShowing()) {
                                    mLoading.dismiss();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mLoading != null && mLoading.isShowing()) {
                                    mLoading.dismiss();
                                }
                            }

                            @Override
                            public void onNext(WeatherAirLive weatherAirLive) {
                                if(weatherAirLive!=null){
                                    tvAqi.setText(weatherAirLive.getAQI());
//                                    indicatorViewAqi.setIndicatorValue(Integer.valueOf(weatherAirLive.getAQI()));
                                    tvCityRank.setText(weatherAirLive.getPrimary());
                                }
                                if (mLoading != null && mLoading.isShowing()) {
                                    mLoading.dismiss();
                                }
                            }
                        });
                break;
        }

    }

}
