package com.ns.yc.lifehelper.ui.other.weather.bean;

import java.util.List;

/**
 * Created by PC on 2017/11/3.
 * 作者：PC
 */

public class FiftyDayWeather {

    /**
     * showapi_res_error :
     * showapi_res_code : 0
     */

    private String showapi_res_error;
    private int showapi_res_code;
    /**
     * areaid : 101200101
     * area : 武汉
     * ret_code : 0
     */

    private ShowapiResBodyBean showapi_res_body;

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {
        private String areaid;
        private String area;
        private int ret_code;
        /**
         * night_weather_pic : http://app1.showapi.com/weather/icon/night/00.png
         * daytime : 20171103
         * day_weather_code : 18
         * area : 武汉
         * night_wind_power : 微风
         * night_weather_code : 00
         * day_wind_direction : 东北风
         * day_weather : 雾
         * day_air_temperature : 24
         * night_wind_direction : 无持续风向
         * areaid : 101200101
         * night_weather : 晴
         * night_air_temperature : 11
         * day_weather_pic : http://app1.showapi.com/weather/icon/day/18.png
         * day_wind_power : 3-4级
         */

        private List<DayListBean> dayList;

        public String getAreaid() {
            return areaid;
        }

        public void setAreaid(String areaid) {
            this.areaid = areaid;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public List<DayListBean> getDayList() {
            return dayList;
        }

        public void setDayList(List<DayListBean> dayList) {
            this.dayList = dayList;
        }

        public static class DayListBean {
            private String night_weather_pic;
            private String daytime;
            private String day_weather_code;
            private String area;
            private String night_wind_power;
            private String night_weather_code;
            private String day_wind_direction;
            private String day_weather;
            private String day_air_temperature;
            private String night_wind_direction;
            private String areaid;
            private String night_weather;
            private String night_air_temperature;
            private String day_weather_pic;
            private String day_wind_power;

            public String getNight_weather_pic() {
                return night_weather_pic;
            }

            public void setNight_weather_pic(String night_weather_pic) {
                this.night_weather_pic = night_weather_pic;
            }

            public String getDaytime() {
                return daytime;
            }

            public void setDaytime(String daytime) {
                this.daytime = daytime;
            }

            public String getDay_weather_code() {
                return day_weather_code;
            }

            public void setDay_weather_code(String day_weather_code) {
                this.day_weather_code = day_weather_code;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getNight_wind_power() {
                return night_wind_power;
            }

            public void setNight_wind_power(String night_wind_power) {
                this.night_wind_power = night_wind_power;
            }

            public String getNight_weather_code() {
                return night_weather_code;
            }

            public void setNight_weather_code(String night_weather_code) {
                this.night_weather_code = night_weather_code;
            }

            public String getDay_wind_direction() {
                return day_wind_direction;
            }

            public void setDay_wind_direction(String day_wind_direction) {
                this.day_wind_direction = day_wind_direction;
            }

            public String getDay_weather() {
                return day_weather;
            }

            public void setDay_weather(String day_weather) {
                this.day_weather = day_weather;
            }

            public String getDay_air_temperature() {
                return day_air_temperature;
            }

            public void setDay_air_temperature(String day_air_temperature) {
                this.day_air_temperature = day_air_temperature;
            }

            public String getNight_wind_direction() {
                return night_wind_direction;
            }

            public void setNight_wind_direction(String night_wind_direction) {
                this.night_wind_direction = night_wind_direction;
            }

            public String getAreaid() {
                return areaid;
            }

            public void setAreaid(String areaid) {
                this.areaid = areaid;
            }

            public String getNight_weather() {
                return night_weather;
            }

            public void setNight_weather(String night_weather) {
                this.night_weather = night_weather;
            }

            public String getNight_air_temperature() {
                return night_air_temperature;
            }

            public void setNight_air_temperature(String night_air_temperature) {
                this.night_air_temperature = night_air_temperature;
            }

            public String getDay_weather_pic() {
                return day_weather_pic;
            }

            public void setDay_weather_pic(String day_weather_pic) {
                this.day_weather_pic = day_weather_pic;
            }

            public String getDay_wind_power() {
                return day_wind_power;
            }

            public void setDay_wind_power(String day_wind_power) {
                this.day_wind_power = day_wind_power;
            }
        }
    }
}
