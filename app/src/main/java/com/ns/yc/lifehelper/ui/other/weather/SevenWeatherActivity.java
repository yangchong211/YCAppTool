package com.ns.yc.lifehelper.ui.other.weather;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.weather.bean.FiftyDayWeather;
import com.ns.yc.lifehelper.ui.other.weather.contract.SevenWeatherContract;
import com.ns.yc.lifehelper.ui.other.weather.model.EventCenter;
import com.ns.yc.lifehelper.ui.other.weather.presenter.SevenWeatherPresenter;
import com.ns.yc.lifehelper.ui.other.weather.view.weather.BaseDrawer;
import com.ns.yc.lifehelper.ui.other.weather.view.weather.DynamicWeatherView;
import com.ns.yc.lifehelper.ui.other.weather.weight.moji.bean.AqiDetail;
import com.ns.yc.lifehelper.ui.other.weather.weight.moji.bean.ShowApiWeatherNormalInner;
import com.ns.yc.lifehelper.ui.other.weather.weight.moji.view.AqiView;
import com.ns.yc.lifehelper.ui.other.weather.weight.moji.view.AstroView;
import com.ns.yc.lifehelper.utils.EventBusUtils;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;
import com.yc.cn.ycweatherlib.WeatherAirLevel;
import com.yc.cn.ycweatherlib.WeatherItemView;
import com.yc.cn.ycweatherlib.WeatherModel;
import com.yc.cn.ycweatherlib.WeatherView;

import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/3
 * 描    述：7日天气页面
 * 修订历史：
 * ================================================
 */
public class SevenWeatherActivity extends BaseActivity implements View.OnClickListener, SevenWeatherContract.View {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.weather_view)
    WeatherView weatherView;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.ll_weather)
    LinearLayout llWeather;
    @Bind(R.id.bg_view)
    DynamicWeatherView bgView;
    @Bind(R.id.manual_location)
    ImageView manualLocation;
    @Bind(R.id.tv_temperature)
    TextView tvTemperature;
    @Bind(R.id.air_quality)
    TextView airQuality;
    @Bind(R.id.ast_view)
    AstroView astView;
    @Bind(R.id.aqi_view)
    AqiView aqiView;


    private SevenWeatherContract.Presenter presenter = new SevenWeatherPresenter(this);
    private ViewLoading mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
        EventBusUtils.register(this);
    }


    @Override
    protected void onDestroy() {
        bgView.onDestroy();
        super.onDestroy();
        presenter.unSubscribe();
        EventBusUtils.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bgView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        bgView.onPause();
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            onEvent(eventCenter);
        }
    }

    private void onEvent(EventCenter eventCenter) {
        int eventCode = eventCenter.getEventCode();
        switch (eventCode) {
            case 100:
                setWeatherBg((BaseDrawer.Type) eventCenter.getData());
                break;
        }
    }


    @Override
    public int getContentView() {
        return R.layout.activity_seven_weather;
    }

    @Override
    public void initView() {
        initToolBar();
        initWeatherView();
    }

    private void initToolBar() {
        toolbarTitle.setText("七日天气");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText("设置背景");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
        manualLocation.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter.getData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_title_right:
                presenter.changeWeatherBg();
                break;
            case R.id.manual_location:          //跳转地区选择页面

                break;
        }
    }


    private void initWeatherView() {
        //画折线
        //weatherView.setLineType(ZzWeatherView.LINE_TYPE_DISCOUNT);
        weatherView.setBackgroundColor(Color.TRANSPARENT);
        //画曲线(已修复不圆滑问题)
        weatherView.setLineType(WeatherView.LINE_TYPE_CURVE);
        //设置线宽
        weatherView.setLineWidth(2f);
        //设置一屏幕显示几列(最少3列)
        try {
            weatherView.setColumnNumber(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置白天和晚上线条的颜色
        weatherView.setDayAndNightLineColor(Color.BLUE, Color.RED);
        //点击某一列
        weatherView.setOnWeatherItemClickListener(new WeatherView.OnWeatherItemClickListener() {
            @Override
            public void onItemClick(WeatherItemView itemView, int position, WeatherModel weatherModel) {

            }
        });
    }

    /**
     * 设置七日天气数据
     */
    @Override
    public void setWeatherList(List<FiftyDayWeather.ShowapiResBodyBean.DayListBean> dayList) {
        List<WeatherModel> weatherModelList = new ArrayList<>();
        for (int a = 0; a < dayList.size(); a++) {
            WeatherModel weatherModel = new WeatherModel();
            weatherModel.setDate(dayList.get(a).getDaytime());
            //DateFormat dateFormat = DateFormat.getDateInstance();
            //String yyMMdd = dateFormat.format("yyMMdd");
            DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
            weatherModel.setWeek(TimeUtils.getChineseWeek(dayList.get(a).getDaytime(), dateFormat));
            weatherModel.setDayWeather(dayList.get(a).getDay_weather());
            weatherModel.setDayTemp(Integer.valueOf(dayList.get(a).getDay_air_temperature()));
            weatherModel.setNightTemp(Integer.valueOf(dayList.get(a).getNight_air_temperature()));
            weatherModel.setNightWeather(dayList.get(a).getNight_weather());
            weatherModel.setWindOrientation(dayList.get(a).getDay_wind_direction());
            weatherModel.setWindLevel(dayList.get(a).getDay_wind_power());
            weatherModel.setAirLevel(WeatherAirLevel.EXCELLENT);
            weatherModelList.add(weatherModel);
        }
        weatherView.setList(weatherModelList);


        ShowApiWeatherNormalInner inner = new ShowApiWeatherNormalInner();
        inner.setAir_press(dayList.get(0).getArea());
        inner.setNight_air_temperature(dayList.get(0).getNight_air_temperature());
        inner.setDay_air_temperature(dayList.get(0).getDay_air_temperature());
        inner.setDay_wind_power(dayList.get(0).getDay_wind_power());
        inner.setNight_weather(dayList.get(0).getNight_weather());
        inner.setDay_weather(dayList.get(0).getDay_weather());
        inner.setDay_weather_code(dayList.get(0).getDay_weather_code());
        inner.setNight_weather_code(dayList.get(0).getNight_weather_code());
        astView.setData(inner);

        AqiDetail detail = new AqiDetail();
        detail.setCo(383);
        detail.setAqi(54);
        detail.setNo2(51);
        detail.setQuality("良好");
        aqiView.setData(detail);
    }

    @Override
    public Context getActivity() {
        return this;
    }

    @Override
    public void setWeatherBg(BaseDrawer.Type aDefault) {
        bgView.setDrawerType(aDefault);
    }

    @Override
    public void startLoading() {
        // 添加Loading
        mLoading = new ViewLoading(this, Constant.loadingStyle,"") {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        }, 2000);
    }


}
