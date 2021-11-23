package com.yc.location.bean;

import android.location.Location;
import android.os.SystemClock;

import com.tencent.map.geolocation.TencentLocation;
import com.yc.location.constant.Constants;
import com.yc.location.manager.DefaultLocationManager;
import com.yc.location.data.ETraceSource;
import com.yc.location.utils.TransformUtils;
import com.yc.location.utils.LocationUtils;


public class DefaultLocation {

    public static final int ERROR_STATUS           = 5;

//    public static final int ERROR_REGISTER_TENCENTSDK   = 8;
//    public static final int ERROR_NORMAL_GPS            = 9;

    public static final String GPS_PROVIDER     = "gps";
    public static final String WIFI_PROVIDER = "didi_wifi";
    public static final String CELL_PROVIDER = "didi_cell";
    public static final String NLP_PROVIDER = "nlp_network";
    public static final String TENCENT_NETWORK_PROVIDER = "tencent_network";

    public static final String STATUS_CELL = "cell";
    public static final String STATUS_WIFI = "wifi";
    public static final String STATUS_GPS  = "gps";
    /**
     * 基站信息可得（安装了sim卡）
     */
    public static final int STATUS_CELL_AVAILABLE = 0x00;
    //以下二个变量当获取手机模块状态时，用于位与
    /**
     * 基站信息不可得（没安装sim卡）
     */
    public static final int STATUS_CELL_UNAVAILABLE = 0x01;
    /**
     * 基站（cell）定位权限被禁止（android M以上：没有授予应用定位权限，没有ACCESS_COARSE_LOCATION级别权限）
     */
    public static final int STATUS_CELL_DENIED        = 0x02;
    /**
     * wifi开关打开
     */
    public static final int STATUS_WIFI_ENABLED       = 0x00;
    //以下三个变量当获取手机模块状态时，用于位与
    /**
     * wifi开关关闭
     */
    public static final int STATUS_WIFI_DISABLED      = 0x10;
    /**
     * wifi定位权限被禁止（没有授予应用定位权限，没有ACCESS_COARSE_LOCATION级别权限）
     */
    public static final int STATUS_WIFI_DENIED        = 0x20;
    /**
     * 位置信息开关关闭，在android M系统中，此时禁止进行wifi扫描
     * 注：此值仅用于 {@link DefaultLocationManager} getWifiStatus()返回结果的与运算
     */
    public static final int STATUS_WIFI_LOCATION_SWITCH_OFF        = 0x40;
    /**
     * 手机gps开启
     */
    public static final int STATUS_GPS_ENABLED        = 0x000;
    //以下二个变量当获取手机模块状态时，用于位与
    /**
     * 手机GPS没有开启。
     */
    public static final int STATUS_GPS_DISABLED       = 0x100;
    /**
     * GPS定位权限被禁止（没有授予应用定位权限，没有ACCESS_FINE_LOCATION级别权限）
     */
    public static final int STATUS_GPS_DENIED         = 0x200;

    /**
     * GPS模块是可用的，可用于定位。
     */
    public static final int STATUS_GPS_AVAILABLE      = 0x300;
    /**
     * GPS模块定位是不可用的。
     */
    public static final int STATUS_GPS_UNAVAILABLE    = 0x400;
    ;
    public static final int COORDINATE_TYPE_GCJ02 = 1;
    public static final int COORDINATE_TYPE_WGS84 = 0;
    //经度
    private double longtitude;
    //纬度
    private double latitude;
    //精准度
    private float  accuracy;
    //定位时刻
    private long   time;
    //海拔高度
    private double altitude;
    //地平面上GPS运动的方向
    private float  bearing;
    //位置信息提供者（GPS还是网络定位）
    private String provider;
    //速度
    private float  speed;
    //开机到现在过去的时间（elapsed time）
    private long   elapsedrealtime;
    //坐标类型
    private int    coordinateType;
//    //村
//    private String village;
//    //镇
//    private String town;
//    //国家
//    private String nation;
//    //
//    private int rssi;
//    //方向
//    private double direction;
//    //腾讯poi信息
//    private List<TencentPoi> poilist;
//    //门号
//    private String streetno;
//    //
//    private String name;
//    //地址
//    private String address;
//    //
//    private Integer areaStat;
//    //省
//    private String province;
//    //街道
//    private String street;
//    //区
//    private String district;
//    //市
//    private String city;
//    //
//    private Bundle bundle;
//    //城市编号
//    private String citycode;
    //
    private int isMockGps;

    //
    private String source;
    //查找位置信息的结果状态码
    private int errorno = ErrorInfo.ERROR_OK;
    //位置是否为上次缓存值，只在获取lastKnownLocation时使用。
    private boolean isCacheLocation = false;
    //获取位置时的本地时间
    private long localTime = 0;
    private boolean effective = true;

    /**
     * 位置是否为缓存值。只用于获取lastKnownLocation时使用。
     * @return
     * true: 当前定位服务已停止，得到的位置是上次定位服务运行时得到的值。即这是一个缓存值。
     * false：当前定位服务正在运行，此位置是定位服务内部轮询得到的最新值。但注意：如果轮询间隔为36s，则此位置可能是36秒之前的值。
     * 所以如果要即时得到最新的位置值，应使用接口requestLocationUpdateOnce(DIDILocationListener listener)。
     */
    public boolean isCacheLocation() {
        return isCacheLocation;
    }

    public void setCacheLocation(boolean cacheLocation) {
        isCacheLocation = cacheLocation;
    }

    public static DefaultLocation loadFromLocCache(LocCache locCache, LocCache lastloc) {
        if (null == lastloc && null == locCache) {
            return null;
        }
        DefaultLocation didiLocation = new DefaultLocation();

        if(locCache == null) {
            locCache = lastloc;
        }

        if (locCache.provider == null) {
            //网络定位根据confidence来对应到来源于wifi还是基站
            if (locCache.confidence <= 1.0f) {
                locCache.provider = CELL_PROVIDER;
            } else {
                locCache.provider = WIFI_PROVIDER;
            }
        }

        didiLocation.time = locCache.timestamp > 0 ? locCache.timestamp : System.currentTimeMillis();
        didiLocation.longtitude      = locCache.lonlat.lon;
        didiLocation.latitude        = locCache.lonlat.lat;
        didiLocation.accuracy        = locCache.accuracy;
        didiLocation.altitude        = locCache.altitude;
        didiLocation.bearing         = locCache.bearing;
        didiLocation.provider        = locCache.provider;
        didiLocation.speed           = locCache.speed;
        didiLocation.elapsedrealtime = SystemClock.elapsedRealtime();
        didiLocation.isMockGps       = 0;

        didiLocation.coordinateType = locCache.coordinateType;

        didiLocation.source = locCache.lonlat.source;

        return didiLocation;
    }

    public static DefaultLocation loadFromSystemLoc(Location location, ETraceSource source, int coordinateType) {
        if(location == null) {
            return null;
        }

        double[] lonlat = new double[]{location.getLongitude(), location.getLatitude()};
        if (coordinateType == DefaultLocation.COORDINATE_TYPE_GCJ02) {
            lonlat = TransformUtils.transform(location.getLongitude(), location.getLatitude());
        }
        DefaultLocation didiLocation = new DefaultLocation();
        didiLocation.longtitude      = lonlat[0];
        didiLocation.latitude        = lonlat[1];
        didiLocation.coordinateType = coordinateType;
        //FLP返回的所有provider都是"fuse"，所以根据accuracy数值来划分provider:现在只是为了兼容之前server端轨迹统计方式等，后续可以新增一种provider，要同步相关方。
        boolean isGps = (ETraceSource.gps == source || (ETraceSource.googleflp == source && location.getAccuracy() <= Constants.GPS_ACCURACY_LIMIT_FOR_GOOGLE_FLP));
        didiLocation.time            = isGps ? location.getTime() : System.currentTimeMillis();
        didiLocation.accuracy        = location.getAccuracy();
        didiLocation.altitude        = location.getAltitude();
        didiLocation.bearing         = location.getBearing();
        didiLocation.provider        = isGps ? GPS_PROVIDER : NLP_PROVIDER;
        didiLocation.speed           = location.getSpeed();
        didiLocation.elapsedrealtime = SystemClock.elapsedRealtime();
        didiLocation.isMockGps       = (LocationUtils.isMockLocation(location) ? 1 : 0);


        didiLocation.source = source.toString();

        return didiLocation;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public float getBearing() {
        return bearing;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longtitude;
    }

    public String getProvider() {
        return provider;
    }

    public float getSpeed() {
        return speed;
    }

    public long getTime() {
        return  time;
    }

    public long getElapsedRealtime() {
        return elapsedrealtime;
    }

    public int getCoordinateType() { return coordinateType; }

    //********************************* 以下接口暂时没实现功能，返回为空***************************************//
//    public String getVillage() {
//        return village;
//    }
//
//    public String getTown() {
//        return town;
//    }
//
//    public String getNation() {
//        return nation;
//    }
//
//    public int getRssi() { return rssi; }
//
//    public double getDirection() { return direction; }
//
//    public List<TencentPoi> getPoiList() {
//        return poilist;
//    }
//
//    public String getStreetNo() {
//        return streetno;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public Integer getAreaStat() {
//        return areaStat;
//    }
//
//    public String getProvince() {
//        return province;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public String getDistrict() {
//        return district;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public Bundle getExtra() {
//        return bundle;
//    }
//
//    public String getCityCode() {
//        return citycode;
//    }
    //********************************* 未实现功能的接口 END*****************************************//

    /**
     * 是否是伪造的GPS位置
     * @return
     */
    public int isMockGps() {
        return isMockGps;
    }

    /**
     * 返回位置信息的结果状态码，现在都是0（ERROR_OK）。出错时的错误码请从ErrInfo类的getErrCode()获取。
     * @return
     */
    public int getError() {
        return errorno;
    }


    public String getSource() {
        return source;
    }

    /**
     * 此定位点到目标点location的距离
     * @param location 目标点
     * @return 两点距离，单位米
     */
    public double distanceTo(DefaultLocation location) {
        if (location != null) {
            return distanceTo(location.getLongitude(), location.getLatitude());
        }
        return 0.0;
    }

    /**
     * 此定位点到目标点的距离
     * @param lng 目标点的精度
     * @param lat 目标点的纬度
     * @return 两点距离，单位米
     */
    public double distanceTo(double lng, double lat) {
        return TransformUtils.calcdistance(longtitude, latitude, lng, lat);
    }

    /**
     * 计算两个定位点的距离
     * @param loc1 定位点1
     * @param loc2 定位点2
     * @return 两点距离，单位米
     */
    public static double distanceBetween(DefaultLocation loc1, DefaultLocation loc2) {
        if (loc1 != null && loc2 != null) {
            return loc1.distanceTo(loc2);
        }
        return 0.0;
    }

    /**
     * 计算两个定位点的距离
     * @param lng1 定位点1的经度
     * @param lat1 定位点1的纬度
     * @param lng2 定位点2的经度
     * @param lat2 定位点2的纬度
     * @return 两点距离，单位米
     */
    public static double distanceBetween(double lng1, double lat1, double lng2, double lat2) {
        return TransformUtils.calcdistance(lng1, lat1, lng2, lat2);
    }

    /**
     * 设置获取位置时的本地时间
     * @param localTime
     */
    public void setLocalTime(long localTime) {
        this.localTime = localTime;
    }

    /**
     * 获得 位置获取的本地时间
     */
    public long getLocalTime() {
        return this.localTime;
    }

    /**
     * 设置位置是否为有效（暂时就判断定位时间是否过了30s）
     * @param effective
     */
    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    /**
     * 位置是否为有效（暂时就判断定位时间是否过了30s）
     */
    public boolean isEffective() {
        return this.effective;
    }

    public static DefaultLocation loadFromTencentLoc(TencentLocation tencentLocation) {

        DefaultLocation didiLocation = new DefaultLocation();

        didiLocation.longtitude = tencentLocation.getLongitude();
        didiLocation.latitude = tencentLocation.getLatitude();
        didiLocation.accuracy = tencentLocation.getAccuracy();
        didiLocation.time = tencentLocation.getTime();
        didiLocation.altitude = tencentLocation.getAltitude();
        didiLocation.bearing = tencentLocation.getBearing();

        didiLocation.provider = tencentLocation.getProvider();
        if (TencentLocation.GPS_PROVIDER.equals(didiLocation.provider)) {
            didiLocation.provider = GPS_PROVIDER;
        } else if (TencentLocation.NETWORK_PROVIDER.equals(didiLocation.provider)) {
            didiLocation.provider = TENCENT_NETWORK_PROVIDER;
        }
        didiLocation.speed = tencentLocation.getSpeed();
        didiLocation.elapsedrealtime = tencentLocation.getElapsedRealtime();
        didiLocation.coordinateType = tencentLocation.getCoordinateType();

//        didiLocation.village = tencentLocation.getVillage();
//        didiLocation.town = tencentLocation.getTown();
//        didiLocation.nation = tencentLocation.getNation();
//        didiLocation.rssi = tencentLocation.getRssi();
//        didiLocation.direction = tencentLocation.getDirection();
//        didiLocation.poilist = tencentLocation.getPoiList();
//        didiLocation.streetno = tencentLocation.getStreetNo();
//        didiLocation.name = tencentLocation.getName();
//        didiLocation.address = tencentLocation.getAddress();
//        didiLocation.areaStat = tencentLocation.getAreaStat();
//        didiLocation.province = tencentLocation.getProvince();
//        didiLocation.street = tencentLocation.getStreet();
//        didiLocation.district = tencentLocation.getDistrict();
//        didiLocation.city = tencentLocation.getCity();
//        didiLocation.bundle = tencentLocation.getExtra();
//        didiLocation.citycode = tencentLocation.getCityCode();
        didiLocation.isMockGps = tencentLocation.isMockGps();

        didiLocation.source = ETraceSource.tencent.toString();

        return didiLocation;
    }

    /**
     * 获得到VDR模块计算得到的位置信息。(此分支只是为了兼容司机端国际版能编译正确)
     * @return
     */
    public String getVdrLocationJson() {
        return null;
    }

    @Override
    public String toString() {
        return Constants.joLeft
                + "\"lon\"" + Constants.jsAssi + Constants.formatDouble(longtitude, Constants.lonlatDots) + Constants.jsSepr
                + "\"lat\"" + Constants.jsAssi + Constants.formatDouble(latitude, Constants.lonlatDots) + Constants.jsSepr
                + "\"accuracy\"" + Constants.jsAssi + accuracy + Constants.jsSepr
                + "\"provider\"" + Constants.jsAssi + provider + Constants.jsSepr
                + "\"bearing\"" + Constants.jsAssi + bearing + Constants.jsSepr
                + "\"timestamp\"" + Constants.jsAssi + time + Constants.jsSepr
                + "\"source\"" + Constants.jsAssi + source + Constants.jsSepr
                + "\"coordinate\"" + Constants.jsAssi + coordinateType + Constants.jsSepr
                + "\"speed\"" + Constants.jsAssi + speed
                + Constants.joRight;
    }
}
