package com.yc.location.manager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yc.location.BuildConfig;
import com.yc.location.LocationCenter;
import com.yc.location.R;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.constant.Constants;
import com.yc.location.listener.LocationListener;
import com.yc.location.listener.LocationListenerWrapper;
import com.yc.location.log.LogHelper;
import com.yc.location.monitor.LocationSensorMonitor;
import com.yc.location.utils.LocationUtils;

import java.io.File;
import java.util.HashSet;



public class DefaultLocationManager {

    public static String appid = "test";
    public  static volatile DefaultLocation lastKnownLocation = null;
    public  static volatile long startstamp = 0L;
    public static boolean enableMockLocation = false;
    private static volatile DefaultLocationManager instance = null;
    private static Context context = null;
    private boolean isRunning = false;
    private LocationCenter mLocCenter = null;
    //处理收集数据相关操作的handler，工作在异步线程中。
    private Handler mDataWorkHandler;
    private HashSet<LocationListener> mLocOnceListeners;
    private LocationListener mOnceListener;
    private LocationUpdateOption mOnceListenerOption;
    private Handler mMainHandler;


    private void notifyLocOnceListeners(DefaultLocation loc) {
        if (mLocOnceListeners != null) {
            //StringBuilder log = new StringBuilder("notify loc " + (loc == null ? null : loc.toString()) + " to once listeners:");
            for (LocationListener l : mLocOnceListeners) {
                //log.append("{" + "listener").append(l.hashCode()).append("}");
                l.onLocationChanged(loc);
            }
            //LogHelper.write(log.toString());
        }
    }

    private DefaultLocationManager(Context context) {
        DefaultLocationManager.context = context.getApplicationContext();

        mDataWorkThread = new HandlerThread("DataWorkThread");
        mDataWorkThread.start();
        mDataWorkHandler = new Handler(mDataWorkThread.getLooper());
        LogHelper.init(DefaultLocationManager.context);

        mMainHandler = new Handler(context.getMainLooper());

        mLocOnceListeners = new HashSet<>();
        mOnceListener = new LocationListener() {
            @Override
            public void onLocationChanged(DefaultLocation didiLocation) {
                notifyLocOnceListeners(didiLocation);
                finishLocOnce();
            }

            @Override
            public void onLocationError(int errNo, ErrorInfo errInfo) {
                notifyErrOnceListeners(errNo, errInfo);
                finishLocOnce();
            }

            @Override
            public void onStatusUpdate(String name, int status, String desc) {

            }
        };
        mOnceListenerOption = getDefaultLocationUpdateOption();
        mOnceListenerOption.setInterval(LocationUpdateOption.IntervalMode.HIGH_FREQUENCY);
		LogHelper.logFile("DIDILocationManager single instance constructed!!");
    }

    private void notifyErrOnceListeners(int errNo, ErrorInfo errInfo) {
        if (mLocOnceListeners != null) {
//            String log = "notify error " + (errInfo == null ? null : errInfo.getErrNo()) + " to once listeners:";
            for (LocationListener l : mLocOnceListeners) {
                //Log.i("lcc", "lcc, notify once listener " + l.hashCode());
//                log += "{" + "listener" + l.hashCode() + "}";
                l.onLocationError(errNo, errInfo);
            }
//            LogHelper.write(log);
        }
    }

    private void finishLocOnce() {
        mLocOnceListeners.clear();
        mOnceListenerOption.setModuleKey(null);

//        mIsOnceLocating = false;
        //当连续定位服务和单次定位服务、都不需要时，停止定位服务。
        removeLocationUpdates(mOnceListener);//listener回调中嵌套操作 包含listener的list，调封装函数经handler中转一下。
    }

    /**
     * <p>
     *     LocManager是一个单例，使用getInstance去获取实例。
     * </p>
     * <p>
     *     需要一个ApplicationContext作为参数且不能为空，否则会导致LocManager无法实例化，
     *     因此返回无效值。
     * </p>
     * <p>
     *     参数ApplicationContext用于程序package name的获取，会影响到case的标注，
     *     为了保证context的生命周期，不要将某个activity的context作为ApplicationContext。
     * </p>
     *
     * @param c 应用程序的Context
     * @return 返回LocManager实例，用于启动定位，使用一些public方法等。
     */
    public static DefaultLocationManager getInstance(Context c) {
        if(c == null) return null;
        context = c.getApplicationContext();

        if(instance == null) {
            synchronized (DefaultLocationManager.class) {
                if(instance == null) {
                    instance = new DefaultLocationManager(context);
                }
            }
        }
        return instance;
    }

    //收集数据相关操作的工作线程。
    private HandlerThread mDataWorkThread = null;
    /**
     * WARNNING: multi calling cannot replace DIDILocationListener to new one,
     * you should call stop - start to replace new DIDILocationListener.
     * @param locListener callback interface who notify didilocation
     * @return 0=ok
     */
    private synchronized int startLocService(LocationListenerWrapper locListener) {

        if (Build.VERSION.SDK_INT < 9) return 1;
        startstamp = System.currentTimeMillis();
        if (null != lastKnownLocation) {
            lastKnownLocation.setCacheLocation(true);
        }

		LogHelper.logFile("LocManager # startLocService called, locListener hash " + locListener.hashCode());
        LogHelper.logFile("SDK VER : " + BuildConfig.VERSION_NAME);



        if (mLocCenter == null) {
            mLocCenter = new LocationCenter(context);
        }
        mLocCenter.start(locListener); // listener 队列

        //LocService.maptype = -1;
        if (LocationUtils.getCoordinateType(context) == DefaultLocation.COORDINATE_TYPE_GCJ02) {//国际应用不采集
            startTrace();
        }

        isRunning = true;


        LogHelper.logFile("-startLocService- : success!");

        return 0;
    }

    /** give/update phonenum to locsdk, '15802348421', etc. */
    public void setPhonenum(String num) {

        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME_PHONE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PREFS_NAME_PHONE, num);

        editor.apply();

        LogHelper.setPhonenum(num);
    }

    protected static String getPhonenum(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME_PHONE, Context.MODE_PRIVATE);
        return settings.getString(Constants.PREFS_NAME_PHONE, "");
    }

    public static Context getAppContext() {
        return context;
    }

    public void setAppVersionName(String version) {
        LocationUtils.saveAppVersion(context, version);

    }

    /** give/update appid to locsdk, 'gs','taxi','passenger','test',etc. */
    public void setAppid(String id) { appid = id; }

    private void stopLocService() {

        if (Build.VERSION.SDK_INT < 9) return;
        //if (!isNormalStop()) return;
        if (!isRunning && mLocCenter == null) {
            LogHelper.logFile("LocManager # loc service is not running");
            return;
        }

        LogHelper.logFile("LocManager # stop loc service");

        if (mLocCenter != null) mLocCenter.stop();
        mLocCenter = null;
        if (LocationUtils.getCoordinateType(context) == DefaultLocation.COORDINATE_TYPE_GCJ02) {//国际应用就没有开启采集
            stopTrace();
        }


        isRunning = false;
        //useTencentSDK = false;
        if (null != lastKnownLocation) {
            lastKnownLocation.setCacheLocation(true);
        }
        LogHelper.stopWorkThread();

    }

    public String getVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 同步获取最近的获得到的位置。非连续定位服务运行期间，得到的值为null。
     * @return
     */
    public DefaultLocation getLastKnownLocation() {
        if (lastKnownLocation != null && System.currentTimeMillis() - lastKnownLocation.getLocalTime() > 30*1000) {
            lastKnownLocation.setEffective(false);
        }
        return lastKnownLocation;
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 异步获取一次位置。会回调通知当前最新位置。
     * @param listener  回调的listener
     * @param moduleKey 分配给各业务线的key（暂时使用POI业务分配的key）。务必传入各业务线自己正确的key。
     */
    public int requestLocationUpdateOnce(final LocationListener listener, final String moduleKey) {
        if (null == listener) {
            return -1;
        }
        LogHelper.write("LocationManager#requestLocationUpdateOnce: listener" + listener.hashCode() + " key:" + moduleKey);
        if (TextUtils.isEmpty(moduleKey)) {
            final ErrorInfo errInfo = new ErrorInfo(ErrorInfo.ERROR_MODULE_PERMISSION);
            errInfo.setErrMessage(context.getString(R.string.location_err_module_permission));
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onLocationError(ErrorInfo.ERROR_MODULE_PERMISSION, errInfo);
                }
            });
            return -1;
        }
        /*用handler post，fix bug：当遍历通知listener错误时，业务方重试将同一listener又request，会crash。
        同时，用handler post已起到同步作用，synchronized可以去掉了，下个版本去掉。
         */
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                requestLocationUpdateOnceInternal(listener, moduleKey);
            }
        });
        return 0;
    }

    private void requestLocationUpdateOnceInternal(LocationListener listener, String moduleKey) {
//        if (!mIsOnceLocating) {
//            //Log.i("lcc", "lcc, requestLocationUpdateOnceInternal");
//            startLocServiceOnce(mOnceListener);
//            mIsOnceLocating = true;
//        }
        mLocOnceListeners.add(listener);
        String originModuleKey = mOnceListenerOption.getModuleKey();
        if (TextUtils.isEmpty(originModuleKey)) {
            originModuleKey = moduleKey;
        } else {
            originModuleKey = originModuleKey + "|" + moduleKey;
        }
        mOnceListenerOption.setModuleKey(originModuleKey);
        requestLocationUpdatesInternal(mOnceListener, mOnceListenerOption);
    }

    /**
     * 注册listener连续监听获取位置，可以多次调用注册多个listener，SDK会按option配置参数不断回调通知listener最新位置。首次调用时SDK内部会自动开启定位服务。
     * @param listener 监听位置结果的listener。
     * @param option 本listener监听配置选项，如监听时间间隔等, 其中务必配置moduleKey，负责无法定位，直接出错。支持不同的listener配置不同的监听选项。
     */
    public int requestLocationUpdates(final LocationListener listener, final LocationUpdateOption option) {
        if (null == listener || null == option) {
            return -1;
        }
//        LogHelper.write("DIDILocationManager#requestLocationUpdates: listener" + listener.hashCode() + " key:" + option.getModuleKey());
        if (TextUtils.isEmpty(option.getModuleKey())) {
            final ErrorInfo errInfo = new ErrorInfo(ErrorInfo.ERROR_MODULE_PERMISSION);
            errInfo.setErrMessage(context.getString(R.string.location_err_module_permission));
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onLocationError(ErrorInfo.ERROR_MODULE_PERMISSION, errInfo);
                }
            });
            return -1;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                requestLocationUpdatesInternal(listener, option);
            }
        });
        return 0;
    }

    private void requestLocationUpdatesInternal(LocationListener listener, LocationUpdateOption option) {
        final LocationListenerWrapper listenerWraper = new LocationListenerWrapper(listener, option);
        if (isRunning && mLocCenter != null) {
            //如果已经运行定位服务，则此处立刻回调通知一下此listener。
            if (lastKnownLocation != null && !lastKnownLocation.isCacheLocation()) {
                if (mLocCenter.getLastErrInfo() != null
                        && mLocCenter.getLastErrInfo().getLocalTime() > lastKnownLocation.getLocalTime()) {
                    listener.onLocationError(mLocCenter.getLastErrInfo().getErrNo(), mLocCenter.getLastErrInfo());
                } else {
                    listener.onLocationChanged(lastKnownLocation);
                }
            } else if (mLocCenter.getLastErrInfo() != null) {
                listener.onLocationError(mLocCenter.getLastErrInfo().getErrNo(), mLocCenter.getLastErrInfo());
            }
            mLocCenter.addLocListener(listenerWraper);
        } else {
            startLocService(listenerWraper);
        }
    }

    /**
     * 移除listener，使此listener不再监听获取位置。注意此函数一定要与requestLocationUpdates(DIDILocationListener listener, DIDILocationUpdateOption option)成对使用，即在不再需要继续监听位置时，要及时移除listener。
     * SDK内部发现当所有注册的listener都被移除时，会停止定位服务。
     */
    public int removeLocationUpdates(final LocationListener listener) {
        //Log.i("lcc", "lcc, in removeLocationUpdates, listener is:" + listener.hashCode());
        if (null == listener) {
            return -1;
        }
//        LogHelper.write("DIDILocationManager#removeLocationUpdates: listener" + listener.hashCode());
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                removeLocationUpdatesInternal(listener);
            }
        });
        return 0;
    }

    private void removeLocationUpdatesInternal(LocationListener listener) {
        if (isRunning && mLocCenter != null) {
            //fix bug:定位失败情况下，当外部单次请求定位失败后马上再单次请求，会导致退到后台后无法停止定位。（公交业务线的单次定位请求）
            if (listener == mOnceListener && mLocOnceListeners.size() > 0) {
                return;
            }
            mLocCenter.removeLocListener(listener);
            //当连续定位服务和单次定位服务都不需要时，停止定位服务。
            if (mLocCenter.getLocListenersLength() == 0 && mLocOnceListeners.size() == 0) {
                stopLocService();
            }
        }
    }

    public void enableMockLocation(boolean enableMockLocation) {
        DefaultLocationManager.enableMockLocation = enableMockLocation;
        // 此版本腾讯不含有mock过滤，故不处理腾讯接收的mock
    }

    private void startTrace() {

    }

    private void stopTrace() {

    }

    /**
     * 获得一个默认的配置选项，使用方根据情况决定是否更改其中选项。
     * @return 默认的配置选项对象。其中更新位置时间间隔模式为NORMAL, 3s。
     */
    public LocationUpdateOption getDefaultLocationUpdateOption() {
        return new LocationUpdateOption();
    }

    /**
     * 获得手机GPS状态，返回结果会同时包含是否开启、权限是否被禁止，请用{@link DefaultLocation}中gps状态标示与此返回值与运算，得到所有状态。
     * 如返回值为status，则status&DIDILocation.STATUS_GPS_DISABLED = DIDILocation.STATUS_GPS_DISABLED,则代表gps没有打开。
     * 与运算用到的常量范围：{@link DefaultLocation} STATUS_GPS_DISABLED, STATUS_GPS_DENIED。
     * @return 手机GPS状态值。
     */
    public int getGpsStatus() {
        int gpsEnableState = 0;
        boolean gpsenable = LocationSensorMonitor.getInstance(context).isGpsEnabled();
        gpsEnableState = !gpsenable ? DefaultLocation.STATUS_GPS_DISABLED : 0;
        int gpsPermissionState = 0;
        if (LocationUtils.checkSystemPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            gpsPermissionState = DefaultLocation.STATUS_GPS_DENIED;
        }

        return gpsEnableState|gpsPermissionState;
    }

    /**
     * 获得手机wifi状态，返回结果会同时包含是否开启、权限是否被禁止、定位开关是否关闭（在android M系统中，此时禁止进行wifi扫描），请用{@link DefaultLocation}中wifi状态标示与此返回值与运算，得到所有状态。
     * 如返回值为status，则status&DIDILocation.STATUS_WIFI_DISABLED = DIDILocation.STATUS_WIFI_DISABLED,则代表wifi没有打开。
     * 与运算用到的常量范围：{@link DefaultLocation} STATUS_WIFI_DISABLED, STATUS_WIFI_DENIED, STATUS_WIFI_LOCATION_SWITCH_OFF。
     * @return 手机wifi状态值。
     */
    public int getWifiStatus() {
        boolean wifiEnabled = LocationSensorMonitor.getInstance(context).isWifiEnabled();
        int wifiEnableState = !wifiEnabled ? DefaultLocation.STATUS_WIFI_DISABLED : 0;
        int wifiPermissionState = 0;
        if (!LocationUtils.isLocationPermissionGranted(context)) {
            wifiPermissionState = DefaultLocation.STATUS_WIFI_DENIED;
        }
        int locationSwithState = LocationUtils.isLocationSwitchOff(context) ? DefaultLocation.STATUS_WIFI_LOCATION_SWITCH_OFF : 0;

        return wifiEnableState|wifiPermissionState|locationSwithState;

    }

    /**
     * 获得手机基站状态，返回结果会同时包含是否插入sim卡、权限是否被禁止（对android M以上系统不能使用基站定位），请用{@link DefaultLocation}中cell状态标示与此返回值与运算，得到所有状态。
     * 如返回值为status，则status&DIDILocation.STATUS_CELL_UNAVAILABLE = DIDILocation.STATUS_CELL_UNAVAILABLE,则代表基站信息不可得（没安装sim卡）。
     * 与运算用到的常量范围：{@link DefaultLocation} STATUS_CELL_UNAVAILABLE, STATUS_CELL_DENIED。
     * @return 手机基站（sim卡）状态值。
     */
    public int getCellStatus() {
        TelephonyManager manager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();
        boolean hasSim = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
            case TelephonyManager.SIM_STATE_UNKNOWN:
                hasSim = false; // 没有SIM卡
                break;
            default:
                break;
        }
        int cellPermissionState = 0;
        if (!LocationUtils.isLocationPermissionGranted(context) && Build.VERSION.SDK_INT >= 23) {
            cellPermissionState = DefaultLocation.STATUS_CELL_DENIED;
        }
        int simAvailable = !hasSim ? DefaultLocation.STATUS_CELL_UNAVAILABLE : 0;
        return simAvailable|cellPermissionState;
    }

    /**
     * 设置定位SDK内部写log的路径。
     * @param path log路径
     */
    public void setLogPath(File path) {
        LogHelper.setBamaiLogPath(path);
    }

    /**
     * 设置返回的定位坐标类型。默认为GCJ02坐标(国内定位时使用的坐标)。
     * 一定注意:1.当前只有做国际版业务时，才需要调用此函数。别的业务线或其他情况下禁止调用此函数。2.若需要调用，请在初始化阶段、请求定位之前调用。3.在开启定位前设置
     * @param type 坐标类型。只能设置成{@link DefaultLocation#COORDINATE_TYPE_GCJ02, YCLocation#COORDINATE_TYPE_WGS84}之一。
     */
    public void setCoordinateType(int type) {
        if (isRunning) {
            return;
        }
        if(type == DefaultLocation.COORDINATE_TYPE_GCJ02 || type == DefaultLocation.COORDINATE_TYPE_WGS84) {
            LocationUtils.setCoordinateType(type);
        }
    }

    /**
     * 获得监听方信息，用于监控。
     * 格式：“listener1的module key”@“listener1的监听频率”#“listener2的module key”@”listener2的监听频率”#...
     * @return 监听定位的listener信息，如果没有监听的listener，为""；
     */
    public String getListenersInfo() {
        String info = "";
        if (mLocCenter != null) {
            info = mLocCenter.getListenersInfo();
        }
        return info;
    }

    /**
     * 国外版app定位（{@link DefaultLocationManager#setCoordinateType(int)}为WGS84坐标）时，是否只是使用系统定位，
     * 如GPS，FLP，NLP等，只使用系统定位可以避免定位开关或权限关闭仍然可以定位，更符合国外用户使用隐私习惯。业务方视需求
     * 而设置。默认为false。
     * @param onlyOSLocationAbroad 国外定位是否只使用系统定位
     */
    public void setOnlyOSLocationAbroad(boolean onlyOSLocationAbroad) {
        if (isRunning) {
            return;
        }
        LocationUtils.setOnlyOSLocationAbroad(onlyOSLocationAbroad);
    }

}
