package com.yc.location.manager;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.yc.location.BuildConfig;
import com.yc.location.LocationCenter;
import com.yc.location.R;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.listener.LocationListener;
import com.yc.location.listener.LocationListenerWrapper;
import com.yc.location.log.LogHelper;
import com.yc.location.utils.LocationUtils;
import com.yc.location.utils.StatusUtils;
import java.io.File;
import java.util.HashSet;

public class DefaultLocationManager {

    public  static volatile DefaultLocation lastKnownLocation = null;
    public static boolean enableMockLocation = false;
    private static volatile DefaultLocationManager instance = null;
    private static Context context = null;
    private boolean isRunning = false;
    private LocationCenter mLocCenter = null;
    private final HashSet<LocationListener> mLocOnceListeners;
    private final LocationListener mOnceListener;
    private final LocationUpdateOption mOnceListenerOption;
    private final Handler mMainHandler;
    private static final String TAG = "DefaultLocationManager";

    private DefaultLocationManager(Context context) {
        DefaultLocationManager.context = context.getApplicationContext();
        LogHelper.init(DefaultLocationManager.context);
        mMainHandler = new Handler(context.getMainLooper());
        mLocOnceListeners = new HashSet<>();
        mOnceListener = new LocationListener() {
            @Override
            public void onLocationChanged(DefaultLocation didiLocation) {
                for (LocationListener l : mLocOnceListeners) {
                    l.onLocationChanged(didiLocation);
                }
                finishLocOnce();
            }

            @Override
            public void onLocationError(int errNo, ErrorInfo errInfo) {
                for (LocationListener l : mLocOnceListeners) {
                    l.onLocationError(errNo, errInfo);
                }
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

    private void finishLocOnce() {
        mLocOnceListeners.clear();
        mOnceListenerOption.setModuleKey(null);
        //当连续定位服务和单次定位服务、都不需要时，停止定位服务。
        removeLocationUpdates(mOnceListener);
        //listener回调中嵌套操作 包含listener的list，调封装函数经handler中转一下。
    }

    /**
     * DefaultLocationManager是一个单例，使用getInstance去获取实例。
     * @param c 应用程序的Context
     * @return 返回DefaultLocationManager实例，用于启动定位，使用一些public方法等。
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

    /**
     * WARNNING: multi calling cannot replace DIDILocationListener to new one,
     * you should call stop - start to replace new DIDILocationListener.
     * @param locListener callback interface who notify didilocation
     * @return 0=ok
     */
    private synchronized int startLocService(LocationListenerWrapper locListener) {
        if (null != lastKnownLocation) {
            lastKnownLocation.setCacheLocation(true);
        }
		LogHelper.logFile("LocManager # startLocService called, locListener hash " + locListener.hashCode());
        LogHelper.logFile("SDK VER : " + BuildConfig.VERSION_NAME);
        if (mLocCenter == null) {
            mLocCenter = new LocationCenter(context);
        }
        // listener 队列
        mLocCenter.start(locListener);
        isRunning = true;
        LogHelper.logFile("-startLocService- : success!");
        return 0;
    }

    public static Context getAppContext() {
        return context;
    }

    private void stopLocService() {
        //if (!isNormalStop()) return;
        if (!isRunning && mLocCenter == null) {
            LogHelper.logFile("LocManager # loc service is not running");
            return;
        }
        LogHelper.logFile("LocManager # stop loc service");
        if (mLocCenter != null) {
            mLocCenter.stop();
        }
        mLocCenter = null;
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
     * 注册listener连续监听获取位置，可以多次调用注册多个listener，SDK会按option配置参数不断回调通知listener最新位置。
     * 首次调用时SDK内部会自动开启定位服务。
     * @param listener  监听位置结果的listener。
     * @param option    本listener监听配置选项，如监听时间间隔等, 其中务必配置moduleKey，负责无法定位，直接出错。
     *                  支持不同的listener配置不同的监听选项。
     */
    public int requestLocationUpdates(final LocationListener listener, final LocationUpdateOption option) {
        if (null == listener || null == option) {
            return -1;
        }
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
        if (null == listener) {
            return -1;
        }
        LogHelper.i(TAG, "lcc, in removeLocationUpdates, listener is:" + listener.hashCode());
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

    /**
     * 获得一个默认的配置选项，使用方根据情况决定是否更改其中选项。
     * @return 默认的配置选项对象。其中更新位置时间间隔模式为NORMAL, 3s。
     */
    public LocationUpdateOption getDefaultLocationUpdateOption() {
        return new LocationUpdateOption();
    }

    public int getGpsStatus() {
        return StatusUtils.getGpsStatus(context);
    }

    public int getWifiStatus() {
        return StatusUtils.getWifiStatus(context);
    }

    public int getCellStatus() {
        return StatusUtils.getCellStatus(context);
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
