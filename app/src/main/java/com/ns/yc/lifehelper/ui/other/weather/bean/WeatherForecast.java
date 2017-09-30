package com.ns.yc.lifehelper.ui.other.weather.bean;

import java.util.List;

/**
 * Created by PC on 2017/9/14.
 * 作者：PC
 */

public class WeatherForecast {


    /**
     * citycode : 101010100
     * rdesc : Success
     * forecast : [{"wind":{"dir":"南风","deg":"181","sc":"微风","spd":"4"},"hum":"46","pcpn":"0.0","astro":{"mr":"23:45","sr":"05:55","ms":"14:08","ss":"18:25"},"uv":"7","tmp":{"min":"20","max":"29"},"pop":"0","date":"2017-09-14","pres":"1015","cond":{"cond_n":"多云","cond_d":"多云"},"vis":"18"},{"wind":{"dir":"东南风","deg":"114","sc":"微风","spd":"4"},"hum":"55","pcpn":"0.0","astro":{"mr":"00:16","sr":"05:56","ms":"15:06","ss":"18:24"},"uv":"6","tmp":{"min":"20","max":"27"},"pop":"1","date":"2017-09-15","pres":"1017","cond":{"cond_n":"阴","cond_d":"阴"},"vis":"18"},{"wind":{"dir":"南风","deg":"176","sc":"微风","spd":"5"},"hum":"51","pcpn":"0.0","astro":{"mr":"01:18","sr":"05:57","ms":"15:57","ss":"18:22"},"uv":"7","tmp":{"min":"18","max":"27"},"pop":"12","date":"2017-09-16","pres":"1013","cond":{"cond_n":"阴","cond_d":"多云"},"vis":"20"},{"wind":{"dir":"南风","deg":"180","sc":"微风","spd":"8"},"hum":"44","pcpn":"0.0","astro":{"mr":"02:23","sr":"05:57","ms":"16:42","ss":"18:20"},"uv":"7","tmp":{"min":"18","max":"26"},"pop":"2","date":"2017-09-17","pres":"1009","cond":{"cond_n":"阵雨","cond_d":"阴"},"vis":"19"},{"wind":{"dir":"南风","deg":"183","sc":"微风","spd":"5"},"hum":"31","pcpn":"0.0","astro":{"mr":"03:30","sr":"05:58","ms":"17:22","ss":"18:19"},"uv":"8","tmp":{"min":"19","max":"29"},"pop":"8","date":"2017-09-18","pres":"1006","cond":{"cond_n":"晴","cond_d":"晴"},"vis":"18"},{"wind":{"dir":"西南风","deg":"256","sc":"微风","spd":"3"},"hum":"33","pcpn":"0.0","astro":{"mr":"04:36","sr":"05:59","ms":"17:58","ss":"18:17"},"uv":"7","tmp":{"min":"17","max":"27"},"pop":"0","date":"2017-09-19","pres":"1012","cond":{"cond_n":"晴","cond_d":"晴"},"vis":"19"},{"wind":{"dir":"南风","deg":"184","sc":"微风","spd":"7"},"hum":"24","pcpn":"0.0","astro":{"mr":"05:42","sr":"06:00","ms":"18:31","ss":"18:15"},"uv":"7","tmp":{"min":"19","max":"29"},"pop":"0","date":"2017-09-20","pres":"1009","cond":{"cond_n":"晴","cond_d":"多云"},"vis":"19"}]
     * rcode : 200
     * suggestion : {"trav":{"brf":"一般","txt":"天气较好，同时又有微风伴您一路同行，但是比较热，外出旅游请注意防晒，并注意防暑降温。"},"uv":{"brf":"很强","txt":"紫外线辐射极强，建议涂擦SPF20以上、PA++的防晒护肤品，尽量避免暴露于日光下。"},"flu":{"brf":"少发","txt":"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"},"comf":{"brf":"很不舒适","txt":"白天天气晴好，但烈日炎炎会使您会感到很热，很不舒适。"},"sport":{"brf":"较不宜","txt":"天气较好，但炎热，请注意适当减少运动时间并降低运动强度，户外运动请注意防晒。"},"air":{"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"},"cw":{"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},"drs":{"brf":"炎热","txt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}}
     * cityname : 北京
     */

    private String citycode;
    private String rdesc;
    private int rcode;
    /**
     * trav : {"brf":"一般","txt":"天气较好，同时又有微风伴您一路同行，但是比较热，外出旅游请注意防晒，并注意防暑降温。"}
     * uv : {"brf":"很强","txt":"紫外线辐射极强，建议涂擦SPF20以上、PA++的防晒护肤品，尽量避免暴露于日光下。"}
     * flu : {"brf":"少发","txt":"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"}
     * comf : {"brf":"很不舒适","txt":"白天天气晴好，但烈日炎炎会使您会感到很热，很不舒适。"}
     * sport : {"brf":"较不宜","txt":"天气较好，但炎热，请注意适当减少运动时间并降低运动强度，户外运动请注意防晒。"}
     * air : {"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"}
     * cw : {"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}
     * drs : {"brf":"炎热","txt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}
     */

    private SuggestionBean suggestion;
    private String cityname;
    /**
     * wind : {"dir":"南风","deg":"181","sc":"微风","spd":"4"}
     * hum : 46
     * pcpn : 0.0
     * astro : {"mr":"23:45","sr":"05:55","ms":"14:08","ss":"18:25"}
     * uv : 7
     * tmp : {"min":"20","max":"29"}
     * pop : 0
     * date : 2017-09-14
     * pres : 1015
     * cond : {"cond_n":"多云","cond_d":"多云"}
     * vis : 18
     */

    private List<ForecastBean> forecast;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getRdesc() {
        return rdesc;
    }

    public void setRdesc(String rdesc) {
        this.rdesc = rdesc;
    }

    public int getRcode() {
        return rcode;
    }

    public void setRcode(int rcode) {
        this.rcode = rcode;
    }

    public SuggestionBean getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionBean suggestion) {
        this.suggestion = suggestion;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public List<ForecastBean> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastBean> forecast) {
        this.forecast = forecast;
    }

    public static class SuggestionBean {
        /**
         * brf : 一般
         * txt : 天气较好，同时又有微风伴您一路同行，但是比较热，外出旅游请注意防晒，并注意防暑降温。
         */

        private TravBean trav;
        /**
         * brf : 很强
         * txt : 紫外线辐射极强，建议涂擦SPF20以上、PA++的防晒护肤品，尽量避免暴露于日光下。
         */

        private UvBean uv;
        /**
         * brf : 少发
         * txt : 各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。
         */

        private FluBean flu;
        /**
         * brf : 很不舒适
         * txt : 白天天气晴好，但烈日炎炎会使您会感到很热，很不舒适。
         */

        private ComfBean comf;
        /**
         * brf : 较不宜
         * txt : 天气较好，但炎热，请注意适当减少运动时间并降低运动强度，户外运动请注意防晒。
         */

        private SportBean sport;
        /**
         * brf : 中
         * txt : 气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。
         */

        private AirBean air;
        /**
         * brf : 较适宜
         * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
         */

        private CwBean cw;
        /**
         * brf : 炎热
         * txt : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
         */

        private DrsBean drs;

        public TravBean getTrav() {
            return trav;
        }

        public void setTrav(TravBean trav) {
            this.trav = trav;
        }

        public UvBean getUv() {
            return uv;
        }

        public void setUv(UvBean uv) {
            this.uv = uv;
        }

        public FluBean getFlu() {
            return flu;
        }

        public void setFlu(FluBean flu) {
            this.flu = flu;
        }

        public ComfBean getComf() {
            return comf;
        }

        public void setComf(ComfBean comf) {
            this.comf = comf;
        }

        public SportBean getSport() {
            return sport;
        }

        public void setSport(SportBean sport) {
            this.sport = sport;
        }

        public AirBean getAir() {
            return air;
        }

        public void setAir(AirBean air) {
            this.air = air;
        }

        public CwBean getCw() {
            return cw;
        }

        public void setCw(CwBean cw) {
            this.cw = cw;
        }

        public DrsBean getDrs() {
            return drs;
        }

        public void setDrs(DrsBean drs) {
            this.drs = drs;
        }

        public static class TravBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class UvBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class FluBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class ComfBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class SportBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class AirBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class CwBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class DrsBean {
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
    }

    public static class ForecastBean {
        /**
         * dir : 南风
         * deg : 181
         * sc : 微风
         * spd : 4
         */

        private WindBean wind;
        private String hum;
        private String pcpn;
        /**
         * mr : 23:45
         * sr : 05:55
         * ms : 14:08
         * ss : 18:25
         */

        private AstroBean astro;
        private String uv;
        /**
         * min : 20
         * max : 29
         */

        private TmpBean tmp;
        private String pop;
        private String date;
        private String pres;
        /**
         * cond_n : 多云
         * cond_d : 多云
         */

        private CondBean cond;
        private String vis;

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public AstroBean getAstro() {
            return astro;
        }

        public void setAstro(AstroBean astro) {
            this.astro = astro;
        }

        public String getUv() {
            return uv;
        }

        public void setUv(String uv) {
            this.uv = uv;
        }

        public TmpBean getTmp() {
            return tmp;
        }

        public void setTmp(TmpBean tmp) {
            this.tmp = tmp;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public CondBean getCond() {
            return cond;
        }

        public void setCond(CondBean cond) {
            this.cond = cond;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public static class WindBean {
            private String dir;
            private String deg;
            private String sc;
            private String spd;

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getDeg() {
                return deg;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }
        }

        public static class AstroBean {
            private String mr;
            private String sr;
            private String ms;
            private String ss;

            public String getMr() {
                return mr;
            }

            public void setMr(String mr) {
                this.mr = mr;
            }

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getMs() {
                return ms;
            }

            public void setMs(String ms) {
                this.ms = ms;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }
        }

        public static class TmpBean {
            private String min;
            private String max;

            public String getMin() {
                return min;
            }

            public void setMin(String min) {
                this.min = min;
            }

            public String getMax() {
                return max;
            }

            public void setMax(String max) {
                this.max = max;
            }
        }

        public static class CondBean {
            private String cond_n;
            private String cond_d;

            public String getCond_n() {
                return cond_n;
            }

            public void setCond_n(String cond_n) {
                this.cond_n = cond_n;
            }

            public String getCond_d() {
                return cond_d;
            }

            public void setCond_d(String cond_d) {
                this.cond_d = cond_d;
            }
        }
    }
}
