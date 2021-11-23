package com.yc.location.easymock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : 定位工具类
 *     revise : 主要是获取经纬度数据
 * </pre>
 */
public final class LocationManagerHelper {

    private volatile static LocationManagerHelper uniqueInstance;
    private LocationManager locationManager;
    private final Context mContext;
    private AddressCallback addressCallback;
    private static Location location;
    //是否加载过
    private boolean isInit = false;

    /**
     * 是否成功addTestProvider，默认为true，软件启动时为防止意外退出导致未重置，重置一次
     * Android 6.0系统以下，可以通过Setting.Secure.ALLOW_MOCK_LOCATION获取是否【允许模拟位置】，
     * 当【允许模拟位置】开启时，可addTestProvider；
     * Android 6.0系统及以上，弃用Setting.Secure.ALLOW_MOCK_LOCATION变量，没有【允许模拟位置】选项，
     * 增加【选择模拟位置信息应用】，此时需要选择当前应用，才可以addTestProvider，
     * 但未找到获取当前选择应用的方法，因此通过addTestProvider是否成功来判断是否可用模拟位置。
     */
    private boolean hasAddTestProvider = true;

    /**
     * 模拟位置的提供者
     */
    private final List<String> mockProviders = new ArrayList<>();
    /**
     * 纬度
     */
    public double latitude;

    /**
     * 经度
     */
    public double longitude;
    private boolean isSuccess;

    public void setAddressCallback(AddressCallback addressCallback) {
        this.addressCallback = addressCallback;
        if (isInit) {
            showLocation();
        } else {
            isInit = true;
        }
    }

    private LocationManagerHelper(Context context) {
        mContext = context;
        //gps
        mockProviders.add(LocationManager.GPS_PROVIDER);
        //network
        mockProviders.add(LocationManager.NETWORK_PROVIDER);
        getLocation();
    }

    //采用Double CheckLock(DCL)实现单例
    public static LocationManagerHelper getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationManagerHelper.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationManagerHelper(context);
                }
            }
        }
        return uniqueInstance;
    }

    private void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //添加用户权限申请判断
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //2.获取位置提供器，GPS或是NetWork
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        String locationProvider;
        //provider 是使用的定位服务商，主要有
        //LocationManager.NETWORK_PROVIDER， LocationManager.GPS_PROVIDER， LocationManager.PASSIVE_PROVIDER
        //第一个是网络定位，第二个是GPS定位，第三个是直接取缓存。
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            //GPS 定位的精准度比较高，但是非常耗电。
            LocationToolUtils.log("=====GPS_PROVIDER=====");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {//Google服务被墙不可用
            //网络定位的精准度稍差，但耗电量比较少。
            LocationToolUtils.log("=====NETWORK_PROVIDER=====");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            LocationToolUtils.log("=====NO_PROVIDER=====");
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return;
        }

        //3.获取上次的位置，一般第一次运行，此值为null
        //获取最近一次的有效location，如果没有，则返回null。
        //也就是说最近一次必须获取过定位才能得到lastLocation。第一次登录或者新安装的app是会返回null的。
        //那么问题来了，如何获取第一次的定位信息呢？可以通过下面这个方法注册请求新的位置信息：
        location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            // 显示当前设备的位置信息
            LocationToolUtils.log("==显示当前设备的位置信息==");
            showLocation();
        } else {
            //当GPS信号弱没获取到位置的时候可从网络获取
            LocationToolUtils.log("==Google服务被墙的解决办法==");
            //Google服务被墙的解决办法
            getLngAndLatWithNetwork();
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        // LocationManager 每隔 5 秒钟会检测一下位置的变化情况，当移动距离超过 10 米的时候，
        // 就会调用 LocationListener 的 onLocationChanged() 方法，并把新的位置信息作为参数传入。
        requestLocation(locationProvider);
    }

    private void requestLocation(String locationProvider) {
        //添加用户权限申请判断
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        /**
         * 参1:选择定位的方式
         * 参2:定位的间隔时间
         * 参3:当位置改变多少时进行重新定位
         * 参4:位置的回调监听
         */
        locationManager.requestLocationUpdates(locationProvider,
                5000, 10, locationListener);
    }

    /**
     * 获取经纬度
     */
    private void showLocation() {
        if(location == null){
            getLocation();
        }else {
            double latitude = location.getLatitude();//纬度
            double longitude = location.getLongitude();//经度
            if(addressCallback != null){
                addressCallback.onGetLocation(latitude,longitude);
            }
            getAddress(latitude, longitude);
        }
    }

    /**
     * 通过经纬度获取具体信息
     * @param latitude                          纬度
     * @param longitude                         经度
     */
    private void getAddress(double latitude, double longitude) {
        //Geocoder通过经纬度获取具体信息
        Geocoder gc = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> locationList = gc.getFromLocation(latitude, longitude, 1);
            if (locationList != null && locationList.size()>0) {
                Address address = locationList.get(0);
                String countryName = address.getCountryName();//国家
                String countryCode = address.getCountryCode();//code
                String adminArea = address.getAdminArea();//省
                String locality = address.getLocality();//市
                String subLocality = address.getSubLocality();//区
                String featureName = address.getFeatureName();//街道

                for (int i = 0; address.getAddressLine(i) != null; i++) {
                    String addressLine = address.getAddressLine(i);
                    //街道名称:广东省深圳市罗湖区蔡屋围一街深圳瑞吉酒店
                    LocationToolUtils.log("addressLine=====" + addressLine);
                }
                if(addressCallback != null){
                    addressCallback.onGetAddress(address);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除定位
     */
    public void removeLocationUpdatesListener() {
        if (locationManager != null) {
            uniqueInstance = null;
            locationManager.removeUpdates(locationListener);
        }
    }


    /**
     * 模拟位置是否启用
     * 若启用，则addTestProvider
     */
    public boolean getUseMockPositionEnable(Context context) {
        // Android 6.0以下，通过Setting.Secure.ALLOW_MOCK_LOCATION判断
        // Android 6.0及以上，需要【选择模拟位置信息应用】，未找到方法，因此通过addTestProvider是否可用判断
        int secure = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION, 0);
        //判断是否开启了系统模拟位置
        boolean canMockPosition = ( secure != 0) ||
                Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
        if (canMockPosition && !hasAddTestProvider) {
            try {
                forMockProviders();
                // 模拟位置可用
                hasAddTestProvider = true;
                canMockPosition = true;
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
        //如果模拟位置可用，则停止mock
        if (!canMockPosition) {
            stopMockLocation();
        }
        return canMockPosition;
    }

    /**
     * 取消位置模拟，以免启用模拟数据后无法还原使用系统位置
     * 若模拟位置未开启，则removeTestProvider将会抛出异常；
     * 若已addTestProvider后，关闭模拟位置，未removeTestProvider将导致系统GPS无数据更新；
     */
    public void stopMockLocation() {
        if (hasAddTestProvider) {
            for (String provider : mockProviders) {
                try {
                    locationManager.removeTestProvider(provider);
                } catch (Exception ex) {
                    // 此处不需要输出日志，若未成功addTestProvider，则必然会出错
                    // 这里是对于非正常情况的预防措施
                }
            }
            hasAddTestProvider = false;
        }
    }

    /**
     * 开始模拟位置
     */
    private void mockLocation() {
        try {
            // 模拟位置（addTestProvider成功的前提下）
            for (String providerStr : mockProviders) {
                Location mockLocation = new Location(providerStr);
                mockLocation.setLatitude(latitude);   // 维度（度）
                mockLocation.setLongitude(longitude);  // 经度（度）
                mockLocation.setAccuracy(0.1f);   // 精度（米）
                mockLocation.setTime(new Date().getTime());   // 本地时间
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }
                locationManager.setTestProviderLocation(providerStr, mockLocation);
            }
            isSuccess = true;
        } catch (Exception e) {
            // 防止用户在软件运行过程中关闭模拟位置或选择其他应用
            stopMockLocation();
            isSuccess = false;
        }
    }

    private void forMockProviders() {
        for (String providerStr : mockProviders) {
            LocationProvider provider = locationManager.getProvider(providerStr);
            if (provider != null) {
                locationManager.addTestProvider(provider.getName()
                        , provider.requiresNetwork()
                        , provider.requiresSatellite()
                        , provider.requiresCell()
                        , provider.hasMonetaryCost()
                        , provider.supportsAltitude()
                        , provider.supportsSpeed()
                        , provider.supportsBearing()
                        , provider.getPowerRequirement()
                        , provider.getAccuracy());
            } else {
                if (providerStr.equals(LocationManager.GPS_PROVIDER)) {
                    //GPS 定位的精准度比较高
                    locationManager.addTestProvider(
                            providerStr
                            , true, true,
                            false, false,
                            true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                } else if (providerStr.equals(LocationManager.NETWORK_PROVIDER)) {
                    //走网络定位
                    locationManager.addTestProvider(
                            providerStr
                            , true, false,
                            true, false,
                            false, false, false
                            , Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
                } else {
                    //走其他定位
                    locationManager.addTestProvider(
                            providerStr
                            , false, false,
                            false, false,
                            true, true, true
                            , Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
                }
            }
            //为给定的提供程序设置启用模拟的值。该值将在适当的地方使用提供程序的任何实际值。
            locationManager.setTestProviderEnabled(providerStr, true);
            //AVAILABLE状态表示：当前GPS为可用状态
            locationManager.setTestProviderStatus(providerStr,
                    LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        }
    }

    private final LocationListener locationListener = new BaseLocationListener() {
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location loc) {
            super.onLocationChanged(location);
            location = loc;
            showLocation();
            if (addressCallback!=null){
                addressCallback.onGetLocation(loc.getLatitude(),loc.getLongitude());
            }
        }
    };

    //从网络获取经纬度
    private void getLngAndLatWithNetwork() {
        //添加用户权限申请判断
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        requestLocation(LocationManager.NETWORK_PROVIDER);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        showLocation();
    }

    public void start() {

    }

    public void stop(){
        stopMockLocation();
    }

    public Location getLastKnownLocation() {
        return null;
    }

    public interface AddressCallback{
        void onGetAddress(Address address);
        void onGetLocation(double lat,double lng);
    }

}
