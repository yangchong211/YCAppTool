package com.yc.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tencent.map.geolocation.TencentLocationManager;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.constant.Constants;
import com.yc.location.log.LogHelper;
import com.yc.location.manager.DefaultLocationManager;
import com.yc.location.manager.GoogleFLPManager;
import com.yc.location.manager.NLPManager;
import com.yc.location.mode.gps.GpsManager;
import com.yc.location.bean.LocCache;
import com.yc.location.mode.cell.CellManager;
import com.yc.location.mode.cell.Cgi;
import com.yc.location.data.ETraceSource;
import com.yc.location.listener.LocationUpdateInternalListener;
import com.yc.location.mode.wifi.WifiManagerWrapper;
import com.yc.location.monitor.LocationSensorMonitor;
import com.yc.location.strategy.ILocationStrategy;
import com.yc.location.strategy.TencentLocationStrategy;
import com.yc.location.utils.NetworkUtils;
import com.yc.location.utils.TransformUtils;
import com.yc.location.utils.LocationUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 综合定位策略：综合使用GPS、网络定位及其他辅助兜底。涵盖大陆、台湾。
 */
public class CompositeLocationStrategy implements ILocationStrategy {

    private Context mContext;
    private final LocationManager locationManager;
    private Handler mWorkHandler;
    private volatile LocationServiceRequest locReqData = null;
    private LocationUpdateInternalListener mLocationInternalListener;
    private CellManager mCellManager;
    private WifiManagerWrapper mWifiManager;
    private volatile long timeCellM = 0;
    private volatile long timeCellE = 0;
    private volatile long timeWifiM = 0;
    private volatile long timeGpsM = 0;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private long wifiReceiveTimestamp = 0;
    private volatile long mLocListenInterval = LocationUpdateOption.IntervalMode.NORMAL.getValue();
    private GpsManager mGpsManager;
    NLPManager mNLPManager;
    private final NetworkLocateProxy diDiNetworkLocateProxy;
    private long mStartGpsTime = 0;
    private long mWifiScanInterval = Constants.wifiScanIntervalMillis;
    private LocCache lastLocCache = null;
    private long lastEnterTimestamp = 0L;
    private long mWifiScanExpiredInterval = Constants.wifiScanExpiredMillis;
    private boolean isWifiScanRunning = false;
    private long mGpsMonitorInterval = Constants.DEF_GPS_FLP_MONITOR_INTERVAL;
    private long mFlpMonitorInterval = Constants.DEF_GPS_FLP_MONITOR_INTERVAL;
    private volatile boolean isStarted = false;
    private GoogleFLPManager mFlpManager;


    private TencentLocationStrategy mTencentManager;
    /**
     * 是否单次请求腾讯位置，已经得到过位置。如果单次请求过并得到过位置，则下次判断国界不再启动单次请求腾讯位置
     */
    public boolean hasGotOnceTencentLocation = false;
    private volatile StringBuilder mListenersInfo;

    public CompositeLocationStrategy(Context context) {
        mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        diDiNetworkLocateProxy = new NetworkLocateProxy(mContext);

    }

    @Override
    public void start(Handler workHandler) {
        if (isStarted) {
            return;
        }

        mWorkHandler = workHandler;
        locReqData = new LocationServiceRequest();

        initCellManager();
        initWifiManager();
        initGpsManager();
        initGoogleFlpManager(workHandler.getLooper());
        mNLPManager = new NLPManager(mContext, mWorkHandler);
        mNLPManager.start();
        LocationSensorMonitor.getInstance(mContext).start();

        //启动时开启一次腾讯定位，只是为了检测是否在台湾使用

        diDiNetworkLocateProxy.cleanHistory(false);
        mWorkHandler.post(scanWifiLoop);
        isWifiScanRunning = true;
        isStarted = true;
        LogHelper.logFile("loc type didi, nlp is google: " + LocationUtils.isGoogleNlp(mContext));
    }

    private void initGoogleFlpManager(Looper looper) {
        mFlpManager = new GoogleFLPManager(mContext);
        mFlpManager.init(mFlpMonitorInterval, looper);
    }

    private void initGpsManager() {
        mGpsManager = new GpsManager(mContext);
        mGpsManager.setLocationInternalListener(mLocationInternalListener);
        mGpsManager.init(mGpsMonitorInterval);
        mStartGpsTime = LocationUtils.getTimeBoot();
    }

    /** 首次启动wifi扫描 & 注册wifi广播接收器 & 注册gps开关监听 */
    private void initWifiManager() {
        if(mContext == null) return;

        mWifiManager = new WifiManagerWrapper(mContext, (WifiManager) LocationUtils.getServ(mContext, Context.WIFI_SERVICE));
        //改成主线程中监听，并且去掉对亮屏、灭屏的监听。以避免出现Broadcast的onReceive()的ANR。
        mWifiManager.setWifiResultReceiver(mWifiReceiver, null, WifiManager.WIFI_STATE_CHANGED_ACTION,
                LocationManager.PROVIDERS_CHANGED_ACTION,
                Intent.ACTION_AIRPLANE_MODE_CHANGED, "android.location.GPS_FIX_CHANGE",ConnectivityManager.CONNECTIVITY_ACTION);
        try {
            mWifiManager.updateWifi();
            timeWifiM = System.currentTimeMillis(); // think first wifi are fresh, avoid call to deadwifi
        } catch (SecurityException e) {
            LogHelper.logFile("initWifiManager exception, " + e.getMessage());
        }
        mWifiManager.enableWifiAlwaysScan(true);
    }

    @Override
    public void stop() {
        if (!isStarted) {
            return;
        }
        if(mWorkHandler != null) {
            mWorkHandler.removeCallbacks(scanWifiLoop);
        }
        isWifiScanRunning = false;
        mWorkHandler = null;
        if (null != mGpsManager) {
            mGpsManager.setLocationInternalListener(null);
            mGpsManager.removeGpsListeners();
            mGpsManager = null;
        }
        rmWifiListeners();
        if (mCellManager != null) {
            mCellManager.destroy();
            mCellManager = null;
        }
        if (null != mFlpManager) {
            mFlpManager.destory();
            mFlpManager = null;
        }
        if (mNLPManager != null) {
            mNLPManager.stop();
            mNLPManager = null;
        }

        hasGotOnceTencentLocation = false;
        if (null != mTencentManager) {
            mTencentManager.stop();
            mTencentManager = null;
        }

        LocationSensorMonitor.getInstance(mContext).stop();
        isStarted = false;
    }

    @Override
    public DefaultLocation retrieveLocation(ErrorInfo errInfo) {
        long startTimestamp = System.currentTimeMillis();
        boolean validgps = mGpsManager.isGpsLocationValid();
        Location gpsLocation = null;
        if (validgps) {
            gpsLocation = mGpsManager.getGpsLocation();
        } else if (locationManager != null) {
            //  强制读取gps定位，可能是过期的
            try {
                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (!LocationUtils.isMockLocation(loc) || DefaultLocationManager.enableMockLocation) {
                    long timestamp = System.currentTimeMillis();
                    if (LocationUtils.locCorrect(loc) && timestamp - loc.getTime() < Constants.minQpsIntervalMillis) {
                        validgps = true;
                        gpsLocation = loc;
                        LogHelper.logFile("loop: getLastKnownLocation success");
                    }
                }
            } catch (Exception e) {
            }
        }

//        LogHelper.logBamai(String.format("loop, gps validity: %s, gps location: %s", validgps, gpsLocation));
        // when gps valid, don't request net
        if (validgps && gpsLocation != null) {
            DefaultLocation loc = DefaultLocation.loadFromSystemLoc(gpsLocation, ETraceSource.gps, TransformUtils.isOutOfMainLand(gpsLocation.getLongitude(), gpsLocation.getLatitude()) ? DefaultLocation.COORDINATE_TYPE_WGS84 : DefaultLocation.COORDINATE_TYPE_GCJ02);
            LogHelper.logFile("loop gps valid!");
            diDiNetworkLocateProxy.cleanHistoryWithGps(loc);
            lastLocCache = diDiNetworkLocateProxy.generateLocCacheFromGps(loc);
            return loc;
        }

        // gps 打开状态，却没有状态回调，重启试试
        long now = LocationUtils.getTimeBoot();
        if (now - mGpsManager.getReceiveGpsSignalTime() > Constants.GPS_CHECK_INTERVAL_MS && now - mStartGpsTime > Constants.GPS_CHECK_INTERVAL_MS) {
            boolean airPlaneMode = LocationUtils.airPlaneModeOn(mContext);
            if (!airPlaneMode &&
                    LocationSensorMonitor.getInstance(mContext).isGpsEnabled()) {
                LogHelper.logFile("restart gps");
                mGpsManager.removeGpsListeners();
                mGpsManager.init(mGpsMonitorInterval);
                mStartGpsTime = now;
            }
        }

        //lcc:判断是否在台湾，在台湾不同的定位策略流程
        Location flpLoc = null;
        if (null != mFlpManager) {
            flpLoc = mFlpManager.getGoogleFlpLoc();
        }
        Location nlpLocation = null;
        if (mNLPManager != null) {
            nlpLocation = mNLPManager.getNLPLocation();
        }
        DefaultLocation tencentLocation = null;
        if (mTencentManager != null) {
            tencentLocation = mTencentManager.retrieveLocation(null);
        }
        //标志是否在台湾及国外
        boolean outOfMainLand = judgeOutOfMainLand(flpLoc, nlpLocation, tencentLocation);

        DefaultLocation didiLocation = null;
        if (!outOfMainLand) {
            //大陆网络定位流程逻辑
            didiLocation = didiNetworkLocate(DefaultLocation.loadFromSystemLoc(nlpLocation, ETraceSource.nlp, DefaultLocation.COORDINATE_TYPE_GCJ02), errInfo, startTimestamp);
        } else {
            //台湾及国外网络定位流程逻辑

            if (flpLoc != null) {
                lastLocCache = new LocCache(flpLoc.getLongitude(),
                        flpLoc.getLatitude(), (int) flpLoc.getAccuracy(), Constants.googleFlpLocConfi, (int) flpLoc.getSpeed(), flpLoc.getTime(), ETraceSource.googleflp.toString(), DefaultLocation.COORDINATE_TYPE_WGS84);
                lastLocCache.altitude = flpLoc.getAltitude();
                lastLocCache.bearing = flpLoc.getBearing();
                lastLocCache.provider = flpLoc.getProvider();
                diDiNetworkLocateProxy.setLastLocCache(lastLocCache);
//                LogHelper.logBamai(String.format("use flp: %.6f, %.6f, %d", flpLoc.getLongitude(), flpLoc.getLatitude(), flpLoc.getTime()));
                didiLocation = DefaultLocation.loadFromSystemLoc(flpLoc, ETraceSource.googleflp, DefaultLocation.COORDINATE_TYPE_WGS84);
            } else {
                if (true) {
                    //使用滴滴网络定位
                    didiLocation = didiNetworkLocate(DefaultLocation.loadFromSystemLoc(nlpLocation, ETraceSource.nlp, DefaultLocation.COORDINATE_TYPE_WGS84), errInfo, startTimestamp);
//                    if (null != didiLocation) {
//                        LogHelper.logBamai(String.format("use didi nlp: %.6f, %.6f, %d, coType: %d, provider: %s", didiLocation.getLongitude(), didiLocation.getLatitude(), didiLocation.getTime(), didiLocation.getCoordinateType(), didiLocation.getProvider()));
//                    } else {
//                        LogHelper.logBamai("use didi nlp, get null");
//                    }
                } else {
                    if (tencentLocation != null) {
                        didiLocation = tencentLocation;
//                        LogHelper.logBamai(String.format("use tecent: %.6f, %.6f, %d, coType: %d", didiLocation.getLongitude(), didiLocation.getLatitude(), didiLocation.getTime(), didiLocation.getCoordinateType()));
                    } else if (nlpLocation != null) {
                        didiLocation = DefaultLocation.loadFromSystemLoc(nlpLocation, ETraceSource.nlp, DefaultLocation.COORDINATE_TYPE_WGS84);
//                        LogHelper.logBamai(String.format("use nlpLocation: %.6f, %.6f, %d, coType: %d", didiLocation.getLongitude(), didiLocation.getLatitude(), didiLocation.getTime(), didiLocation.getCoordinateType()));
                    }
                    if (null == didiLocation) {
                        produceErr(errInfo, null, mContext);
//                        LogHelper.logBamai("loc not use didi, err:" + errInfo.getErrNo());
                    }
                }
            }

        }
        return didiLocation;
    }

    private boolean judgeOutOfMainLand(Location flpLoc, Location nlpLocation, DefaultLocation tencentLocation) {
        boolean outOfMainLand = false;
        if (flpLoc != null) {
            if (TransformUtils.isOutOfMainLand(flpLoc.getLongitude(), flpLoc.getLatitude())) {
                LogHelper.logFile(String.format("boundary flp: %.6f, %.6f, %d", flpLoc.getLongitude(), flpLoc.getLatitude(), flpLoc.getTime()));
                outOfMainLand = true;
            }
        } else if (nlpLocation != null) {
            if (TransformUtils.isOutOfMainLand(nlpLocation.getLongitude(), nlpLocation.getLatitude())) {
                LogHelper.logFile(String.format("boundary nlp: %.6f, %.6f, %d", nlpLocation.getLongitude(), nlpLocation.getLatitude(), nlpLocation.getTime()));
                outOfMainLand = true;
            }
        } else if (tencentLocation != null) {
            if (tencentLocation.getCoordinateType() == TencentLocationManager.COORDINATE_TYPE_WGS84 && TransformUtils.isOutOfMainLand(tencentLocation.getLongitude(), tencentLocation.getLatitude())) {
                LogHelper.logFile(String.format("boundary tencent: %.6f, %.6f, %d", tencentLocation.getLongitude(), tencentLocation.getLatitude(), tencentLocation.getTime()));
                outOfMainLand = true;
            }
        } else {
            //单次请求腾讯定位
            if (!hasGotOnceTencentLocation) {
                if (mTencentManager == null) {
                    mTencentManager = new TencentLocationStrategy(mContext);
                }
                mTencentManager.requestLocationOnce(mTencentOnceLocationListener, mWorkHandler.getLooper());
            }
        }
        return outOfMainLand;
    }

    private DefaultLocation didiNetworkLocate(DefaultLocation nlpLocation, ErrorInfo errInfo, long startTimestamp) {
        // fake high freguency, use last
        long now = LocationUtils.getTimeBoot();
        if (now - lastEnterTimestamp + mLocListenInterval <= Constants.minQpsIntervalMillis
                && lastLocCache != null) {
            DefaultLocation didiLocation = DefaultLocation.loadFromLocCache(lastLocCache, null);
//                    LogHelper.logBamai(String.format("loop[%s][cache]:%s", (now - lastEnterTimestamp), didiLocation.toString()));

            return didiLocation;
        }
        boolean airPlaneMode = LocationUtils.airPlaneModeOn(mContext);
        if (mCellManager.cgiNeedUpdate(airPlaneMode)) {
            mCellManager.requestCgiLocationUpdate();
        }

        LocationServiceRequest instantReqData = null;
        boolean validcell = false;
        boolean validwifi = false;

        Lock lock = readWriteLock.readLock();
        try {
            lock.lock();
            validcell = mCellManager.isCellValid();
            validwifi = checkReqWifiData();
            LogHelper.logFile(String.format("loop: cell[%s] wifi[%s]", validcell, validwifi));
            instantReqData = (LocationServiceRequest) locReqData.deepClone();
        } catch (Throwable e) {//fix crash bug:5078
            LogHelper.logFile("Exception when deep clone request data, exception:" + e.getMessage());
        } finally {
            lock.unlock();
        }

        //上述方法复制中出错，则另一种方法重新试一次。
        if (null == instantReqData || instantReqData.cell == null
                || instantReqData.wifis == null || instantReqData.user_info == null
                || instantReqData.user_sensors_info == null) {
            try {
                lock.lock();
                instantReqData = (LocationServiceRequest) locReqData.clone();
            } catch (Throwable e) {
                LogHelper.writeException(e);
            } finally {
                lock.unlock();
            }
        }

        //开始进行网络请求定位
        LocCache locCache = null;
        lastEnterTimestamp = now;
        mCellManager.refresh();
        fillCellLocation(instantReqData);
        fillOtherData(instantReqData, validcell, validwifi);
        washReqData(instantReqData, false);


        diDiNetworkLocateProxy.setNlpLoc(nlpLocation);

        // enter to loc strategy
        errInfo.setSource(ErrorInfo.SOURCE_DIDI);
        locCache = diDiNetworkLocateProxy.manage(instantReqData, errInfo);

        if (null != locCache) {
            DefaultLocation didiLocation = DefaultLocation.loadFromLocCache(locCache, null);
            lastLocCache = locCache;
            return didiLocation;
        }

        if (nlpLocation != null) {//先保留司机端SDK逻辑，用NLP兜底。实际在LocStrategy中已经兜底过。
            LogHelper.logFile("loop[network]: use nlp as backup");
            locCache = new LocCache(nlpLocation.getLongitude(),
                    nlpLocation.getLatitude(), (int) nlpLocation.getAccuracy(), Constants.nlpLocConfi, (int) nlpLocation.getSpeed(), nlpLocation.getTime(), ETraceSource.nlp.toString(), nlpLocation.getCoordinateType());
            locCache.altitude = nlpLocation.getAltitude();
            locCache.bearing = nlpLocation.getBearing();
            locCache.provider = DefaultLocation.NLP_PROVIDER;
            diDiNetworkLocateProxy.setLastLocCache(locCache);
            lastLocCache = locCache;
            return nlpLocation;
        }

        produceErr(errInfo, instantReqData, mContext);
        //清空locStrategy里相关值，防止旧值保留引起问题。
        diDiNetworkLocateProxy.cleanHistory(true);
        //出现定位错误时也清空lastLocCache，相当于重新开始定位，防止lastLocCache保留旧值引起错误。
        lastLocCache = null;
        return null;
    }

    private void startTencentLocate() {
        if (isStarted && null != mTencentManager) {
            mTencentManager.setInternalLocationListener(mLocationInternalListener);
            mTencentManager.updateLocListenInterval(mLocListenInterval);
            mTencentManager.start(mWorkHandler);
        }
    }

    LocationUpdateInternalListener mTencentOnceLocationListener =  new LocationUpdateInternalListener() {
        @Override
        public void onLocationUpdate(DefaultLocation loc, long intervalCount) {
            hasGotOnceTencentLocation = true;
            if (isStarted && loc.getCoordinateType() == DefaultLocation.COORDINATE_TYPE_WGS84 && TransformUtils.isOutOfMainLand(loc.getLongitude(), loc.getLatitude())) {
                if (mWorkHandler != null) {
                    //修正腾讯单次定位回调中不能请求连续定位的bug,要post一下，并延迟一点！！！
                    mWorkHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startTencentLocate();
                        }
                    }, 300);
                }
            }
        }

        @Override
        public void onLocationErr(ErrorInfo errInfo, int deltaTime) {

        }

        @Override
        public void onStatusUpdate(String name, int status) {
            if (null != mLocationInternalListener) {
                mLocationInternalListener.onStatusUpdate(name, status);
            }
        }
    };

    private void initCellManager() {
        if (mContext == null) {
            return;
        }
        mCellManager = new CellManager(mContext, mLocationInternalListener);
        mCellManager.init();
        mCellManager.getCellLocation();
    }

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            if (intent != null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if (null != mWorkHandler) {
                    mWorkHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isStarted) {
                                return;
                            }
                            wifiReceiveTimestamp = System.currentTimeMillis();
                            timeWifiM = System.currentTimeMillis();
                            fillWifiData(locReqData);
                        }
                    });
                }
            } else if(intent != null && intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                if(mWifiManager != null) {
                    boolean wifiEnable = false;
                    try {
                        wifiEnable = mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
                    } catch (SecurityException e) {
                    }
                    LogHelper.logFile("wifi enable state change: " + wifiEnable);
                    if(wifiEnable) {
                        mWifiManager.updateWifi();
                        if (null != mLocationInternalListener) {
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_WIFI, DefaultLocation.STATUS_WIFI_ENABLED);
                        }
                    } else {
                        if (null != mLocationInternalListener) {
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_WIFI, DefaultLocation.STATUS_WIFI_DISABLED);
                        }
                    }
                }
            } else if(intent != null && intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                if(locationManager != null) {
                    boolean gpsenable = false;
                    try {
                        gpsenable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (SecurityException e) { LogHelper.writeException(e); }
                    if(gpsenable) {
                        if (null != mLocationInternalListener) {
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_GPS, DefaultLocation.STATUS_GPS_ENABLED);
                        }
                    } else {
                        if (null != mLocationInternalListener) {
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_GPS, DefaultLocation.STATUS_GPS_DISABLED);
                        }
                    }
                }
            } else if (intent != null && intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                LogHelper.logFile("AIRPLANE_MODE change");
            } else if (intent != null && intent.getAction().equals("android.location.GPS_FIX_CHANGE")) {
//                Toast.makeText(mContext, "GPS_FIX_CHANGE", Toast.LENGTH_SHORT).show();
                LogHelper.logFile("GPS_FIX_CHANGE");
            } else if (intent != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                LogHelper.logFile("connectivity changed");
            }
        }
    };

    /**
     * 获取wifi数据并填充到req中
     * WIFI的MAC地址 - 去除冒号
     * WIFI个数限制 - 50
     */
    private boolean fillWifiData(LocationServiceRequest req) {
        //fix crash bug: 5081,5082
        if (null == req.wifis) {
            req.wifis = new ArrayList<>();
        } else {
            req.wifis.clear();
        }

        if(mWifiManager == null) return false;
        List<ScanResult> scanResults = null;
        String conn_bssid = "";
        WifiInfo connectedWifi = null;
        try {
            //lcc：fix bug：当wifi没有连接时，会报异常，从而得不到扫描结果。
            connectedWifi = mWifiManager.getConnectionInfo();
            if (null != connectedWifi) {
                String connectedBssid = connectedWifi.getBSSID();
                if (!TextUtils.isEmpty(connectedBssid)) {
                    conn_bssid = connectedBssid.replace(":", "").toLowerCase();
                    if (conn_bssid.matches("0+")) {//如果全0，重置为空
                        conn_bssid = "";
                    }
                }
            }
            scanResults = mWifiManager.getScanResults();
        } catch (SecurityException|NullPointerException e) {
            LogHelper.writeException(e);
        }
        if (scanResults == null || scanResults.size() == 0) {
            LogHelper.write("WIFI SCAN no results");
        }
        //lcc: fix crash: http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=XRUpYlpIT3SBnT2idNZ05Q
        if (TextUtils.isEmpty(conn_bssid) && (scanResults == null || scanResults.size() <= 0 || System.currentTimeMillis() - timeWifiM > Constants.wifiValidDeadlineMillis)) return false;
        //lcc:单独加连接的wifi相关信息，如果有扫描结果，则扫描结果中包含连接的wifi，添加扫描结果时就可以加入。
        if ((scanResults == null || scanResults.size() <= 0 || System.currentTimeMillis() - timeWifiM > Constants.wifiValidDeadlineMillis)
                && !TextUtils.isEmpty(conn_bssid)) {
            wifi_info_t wifi = new wifi_info_t();
            wifi.mac = conn_bssid;
            wifi.level = connectedWifi.getRssi();
            if (Build.VERSION.SDK_INT >= 21) {
                wifi.frequency = connectedWifi.getFrequency();
            }
            wifi.ssid = washSSID(connectedWifi.getSSID(), true);
            wifi.connect = true;
            req.wifis.add(wifi);
        } else {
            //lcc:为上面的crash解决增加判断，其实走到此分支自然满足下面条件。
            if (!(scanResults == null || scanResults.size() <= 0 || System.currentTimeMillis() - timeWifiM > Constants.wifiValidDeadlineMillis)) {
                int count = 0;
                long now = LocationUtils.getTimeBoot();
                for (ScanResult scanResult : scanResults) {
                    if (TextUtils.isEmpty(scanResult.BSSID)) {//fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&product_id=-99&msgid=uaD2HXtGSsi3uy8Y2fNgXA
                        continue;
                    }
                    wifi_info_t wifi = new wifi_info_t();
                    wifi.mac = scanResult.BSSID.replace(":", "").toLowerCase();
                    wifi.level = scanResult.level;
                    wifi.frequency = scanResult.frequency;
                    wifi.connect = wifi.mac.toLowerCase().equals(conn_bssid);
                    //TODO:LCC:没有连接时是不是不应该这么处理？
                    wifi.ssid = washSSID(scanResult.SSID, wifi.connect);
                    if (Build.VERSION.SDK_INT >= 17) {
                        wifi.time_diff = now - scanResult.timestamp/1000;
                    }
                    req.wifis.add(wifi);
                    if (++count > Constants.wifiMaxCount) break;
                }
            }
        }

        return true;
    }

    /** WIFI的SSID的清洗 - 只保留主连接WIFI，限定20个字符，16进制串方式保存 */
    private String washSSID(String srcSSID, boolean isconnect) {
        if (isconnect && !TextUtils.isEmpty(srcSSID)) {//fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=NDbtikPcTdSs9PHBCNqIqw
            if (srcSSID.length() > 20) {
                srcSSID = srcSSID.substring(0, 20);
            }
            try {
                String mainSSID = Constants.bytesToHex(srcSSID.getBytes(Constants.paramCharset));
                //if (mTraceUpload != null) mTraceUpload.onObtainWifiSSID(mainSSID);
                return mainSSID;
            } catch (UnsupportedEncodingException e) {}
        }
        return "";
    }

    public void updateLocListenInterval(long interval) {
        mLocListenInterval = interval;
        handleListenInterval(mLocListenInterval);
    }

    @Override
    public void setInternalLocationListener(LocationUpdateInternalListener locationUpdateInternalListener) {
        mLocationInternalListener = locationUpdateInternalListener;
    }

    @Override
    public void updateListenersInfo(StringBuilder listenersInfo) {
        mListenersInfo = listenersInfo;
    }

    /**
     * 当外部监听定位频率变化时，为省电，改变内部监听系统GPS
     * @param monitorInterval 外部监听定位的频率
     */
    private void handleListenInterval(long monitorInterval) {
        //改变wifi扫描间隔相关参数
        if (monitorInterval > Constants.wifiScanIntervalMillis) {
            mWifiScanInterval = monitorInterval;
            mWifiScanExpiredInterval = monitorInterval + Constants.wifiScanExpiredMillis;
        }
        //将内部监听GPS的时间监听间隔改变，最小为1s
        long innerInterval = monitorInterval*2/3;
        mGpsMonitorInterval = (innerInterval < Constants.DEF_GPS_FLP_MONITOR_INTERVAL ? Constants.DEF_GPS_FLP_MONITOR_INTERVAL : innerInterval);
        if (null != mGpsManager && isStarted) {
            mGpsManager.init(mGpsMonitorInterval);
        }
    }

    /** 检测wifi数据是否最新 - 15s */
    private boolean checkReqWifiData() {

        long cur = System.currentTimeMillis();
        boolean result = (cur - timeWifiM < mWifiScanInterval && locReqData.wifis.size() > 0);
        if (!result) {
            locReqData.wifis.clear();
        }
        return result;
    }

    /**
     * 获取定位请求的KEY
     *
     * @param
     * @return String
     */
    private void fillCellLocation(LocationServiceRequest request) {
        if (mContext == null) {
            return;
        }

        boolean bAirPlaneMode = LocationUtils.airPlaneModeOn(mContext);
        if (bAirPlaneMode) {
            LogHelper.logFile("air plane mode on");
            mCellManager.reset();
        } else {
            mCellManager.refineCellT();
        }

        List<Cgi> lstCgi = mCellManager.getDetectedCgiList();

//        LogHelper.logBamai("main cell type " + iCgiT);

        if (lstCgi != null && !lstCgi.isEmpty()) {
            for (int i = 0; i < lstCgi.size(); i++) {
                Cgi cgi = lstCgi.get(i);
                if (cgi == null) {
                    continue;
                }
                request.cell.neighcells.clear();
                cell_info_t cell = request.cell;
                if (cell.mnc_sid == 0 && cell.mcc == 0 && cell.cellid_bsid == 0 && cell.lac_nid == 0) {
                    request.cell.mcc = Long.parseLong(cgi.mcc);
                    request.cell.mnc_sid = Long.parseLong(cgi.mnc_sid);
                    request.cell.lac_nid = cgi.lac_nid;
                    request.cell.cellid_bsid = cgi.cid_bid;
                    request.cell.rssi = cgi.sig;

                    request.cell.type = cgi.type;
                } else {
                    neigh_cell_t nc = new neigh_cell_t();
                    nc.lac = lstCgi.get(i).lac_nid;
                    nc.cid = lstCgi.get(i).cid_bid;
                    nc.rssi = lstCgi.get(i).sig;
                    request.cell.neighcells.add(nc);
                }
            }
        }
    }

    /** 填充定位请求的head数据，如当前时间戳/imei/phone/userid/..  */
    private boolean fillOtherData(LocationServiceRequest locReqData, boolean validcell, boolean validwifi) {
        if(mCellManager == null || mContext == null || locReqData == null) return false;

        // fill sensor data for inner loc
        LocationSensorMonitor sensorMonitor = LocationSensorMonitor.getInstance(mContext);
        if (null != locReqData.user_sensors_info) {
            locReqData.user_sensors_info.timestamp = System.currentTimeMillis();
            locReqData.user_sensors_info.wifi_open_not = sensorMonitor.isWifiEnabled();
            locReqData.user_sensors_info.wifi_scan_available = sensorMonitor.isWifiAllowScan();
            locReqData.user_sensors_info.gps_open_not = sensorMonitor.isGpsEnabled();
            locReqData.user_sensors_info.connect_type = LocationUtils.getConnectedType(mContext);
            locReqData.user_sensors_info.air_press = sensorMonitor.getAirPressure();
            locReqData.user_sensors_info.light_value = sensorMonitor.getLight();
            locReqData.user_sensors_info.gps_inter = sensorMonitor.getGpsFixedInterval();
            locReqData.user_sensors_info.location_switch_level = LocationUtils.getLocationSwitchLevel(mContext);
            locReqData.user_sensors_info.location_permission = LocationUtils.getLocationPermissionLevel(mContext);
        }

        try {
            if (null != locReqData.user_info) {
                //fix crash bug 5079
                locReqData.user_info.timestamp = System.currentTimeMillis();
                locReqData.user_info.imei = mCellManager.getDeviceId();
                locReqData.user_info.modellevel = Build.MODEL+"/"+Build.VERSION.SDK_INT;
                locReqData.user_info.user_id = (mContext == null ? "" : mContext.getPackageName());
            }
            locReqData.version = BuildConfig.VERSION_CODE;
            locReqData.trace_id = (long) (Math.random() * 1e5);
            locReqData.valid_flag = ValidFlagEnum.invalid.ordinal();

            validcell = false;
            if (locReqData.cell.mcc != 0 || locReqData.cell.mnc_sid != 0
                    || locReqData.cell.lac_nid != 0 || locReqData.cell.cellid_bsid != 0) {
                validcell = true;
            }

            validwifi = false;
            if (locReqData.wifis != null && locReqData.wifis.size() > 0) {
                validwifi = true;
            }
            if (validcell) locReqData.valid_flag = ValidFlagEnum.cell.ordinal();
            if (validwifi) locReqData.valid_flag = ValidFlagEnum.wifi.ordinal();
            if (validcell && validwifi) locReqData.valid_flag = ValidFlagEnum.mixed.ordinal();
            locReqData.listeners_info = String.valueOf(mListenersInfo);
            return true;
        } catch (SecurityException e) {
            LogHelper.writeException(e);
            return false;
        }
    }

    /**
     * 优化请求，包含临时获取策略
     * 当前无GPS位置时，临时获取一次WIFI，无视WIFI时效判断
     * WIFI排序
     * 清除请求中的无用信息
     */
    private void washReqData(LocationServiceRequest req, boolean hasGps) {

        // 强制加入wifi
        if(!hasGps) doFillTempWifi(req);

        // wifi level 排序
        Collections.sort(req.wifis, new Comparator<wifi_info_t>() {
            @Override
            public int compare(wifi_info_t lhs, wifi_info_t rhs) {
                return (int)(rhs.level - lhs.level);
            }
        });

        // 不包含wifi或基站时，清除相应的列表，减少请求冗余
        int validflag = (int)(req.valid_flag);
        if(validflag == ValidFlagEnum.invalid.ordinal() || validflag == ValidFlagEnum.cell.ordinal()) {
            if (null != req.wifis) {
                req.wifis.clear();
            }
        }
        if(validflag == ValidFlagEnum.invalid.ordinal() || validflag == ValidFlagEnum.wifi.ordinal()) {
            if (null != req.cell && null != req.cell.neighcells) {
                req.cell.neighcells.clear();
            }
        }
    }

    /** 若wifi数据为空时，进行临时获取，改变请求标志 */
    private void doFillTempWifi(LocationServiceRequest req) {
        if (null == req) {
            return;
        }

        if(req.valid_flag != ValidFlagEnum.wifi.ordinal() && req.valid_flag != ValidFlagEnum.mixed.ordinal()) {

            boolean b = fillWifiData(req);
            if(b && req.valid_flag == ValidFlagEnum.invalid.ordinal()) req.valid_flag = ValidFlagEnum.wifi.ordinal();
            if(b && req.valid_flag == ValidFlagEnum.cell.ordinal()) req.valid_flag = ValidFlagEnum.mixed.ordinal();
        }
    }

    /** 移除wifi广播接收器 */
    private void rmWifiListeners() {
        if(mContext == null || mWifiReceiver == null) return;

        try {
            mContext.unregisterReceiver(mWifiReceiver);
            mWifiReceiver = null;
        } catch (Exception e) {
            LogHelper.writeException(e);
        }
    }

    private Runnable scanWifiLoop = new Runnable() {
        @Override
        public void run() {
            if(mWifiManager == null || mWorkHandler == null) return;

            long interval = System.currentTimeMillis() - wifiReceiveTimestamp;
            if((interval >= mWifiScanInterval && interval <= mWifiScanExpiredInterval)
                    || wifiReceiveTimestamp == 0) {
                try {
                    mWifiManager.updateWifi();
                } catch (SecurityException e) {
                    LogHelper.logFile("scanWifiLoop exception, " + e.getMessage());
                    if (null != mLocationInternalListener) {
                        mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_WIFI, DefaultLocation.STATUS_WIFI_DENIED);
                    }
                }
            }

            if(isWifiScanRunning && mWorkHandler != null) mWorkHandler.postDelayed(scanWifiLoop, Constants.wifiScanCheckMillis);
        }
    };

    /**
     * 按优先级生成对应的错误码，如果mCurrentErrInfo已经含有错误信息（如网络定位中产生的），则使用已生成的错误信息，最后不会生成"其他错误"类型的错误。
     * @param instantReqData
     */
    static void produceErr(ErrorInfo errInfo, LocationServiceRequest instantReqData, Context context) {
        //是否为没有权限(获取可以尝试用manifest中权限常量判断)
        if (!LocationUtils.isLocationPermissionGranted(context) || !LocationSensorMonitor.getInstance(context).isGpsEnabled()) {
            errInfo.setErrNo(ErrorInfo.ERROR_LOCATION_PERMISSION);
            errInfo.setErrMessage(context.getString(R.string.location_err_location_permission));
            //lcc: fix bug: 4999, deepClone异常，可能导致instantReqData为空。
        } else if (instantReqData != null && instantReqData.wifis.size() == 0 && instantReqData.cell.cellid_bsid == 0 && instantReqData.cell.neighcells.size() == 0) {
            errInfo.setErrNo(ErrorInfo.ERROR_NO_ELEMENT_FOR_LOCATION);
            errInfo.setErrMessage(context.getString(R.string.location_err_no_element));
        } else if (!NetworkUtils.isNetWorkConnected(context)) {
            errInfo.setErrNo(ErrorInfo.ERROR_NETWORK_CONNECTION);
            errInfo.setErrMessage(context.getString(R.string.location_err_network_connection));
        } else if (errInfo.getErrNo() == 0){
            errInfo.setErrNo(ErrorInfo.ERROR_OTHERS);
            errInfo.setErrMessage(context.getString(R.string.location_err_others));
        }
    }
}
