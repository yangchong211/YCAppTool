package com.yc.location.strategy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.yc.location.constant.Constants;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.log.LogHelper;
import com.yc.location.R;
import com.yc.location.monitor.LocationSensorMonitor;
import com.yc.location.utils.LocationUtils;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.listener.LocationUpdateInternalListener;

/**
 * 腾讯定位策略类：对腾讯定位sdk的封装
 * 兼容腾讯定位策略类和管理类的功能
 */

public class TencentLocationStrategy implements ILocationStrategy {

    private Context mContext;
    private long mListenLocInterval;
    private TencentLocationManager mTencentLocationManager;
    private LocationUpdateInternalListener mLocationInternalListener;
    private long timeStart;
    private int mRequestCoordinateType = DefaultLocation.COORDINATE_TYPE_GCJ02;

    public TencentLocationStrategy(Context context) {
        mContext = context;
    }
    @Override
    public void start(Handler workHandler) {
        // request tencent location
        TencentLocationRequest request = TencentLocationRequest.create().setInterval(1000).setAllowCache(false).setRequestLevel(0);
        mTencentLocationManager = TencentLocationManager.getInstance(mContext);
        if (mRequestCoordinateType == DefaultLocation.COORDINATE_TYPE_WGS84) {
            mTencentLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_WGS84);
        }
        mTencentLocationManager.requestLocationUpdates(request, tencentLocationListener, workHandler.getLooper());
        timeStart = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        // remove tencent location
        if (mTencentLocationManager != null) {
            mTencentLocationManager.removeUpdates(tencentLocationListener);
        }
        mTencentLocationTime = 0;
        mTencentLocationErr = TencentLocation.ERROR_OK;
        mTencentLocation = null;
        timeStart = 0;
        mLocationInternalListener = null;
    }

    @Override
    public DefaultLocation retrieveLocation(ErrorInfo errInfo) {
        if (isTencentLocationValid()) {
            DefaultLocation didiLocation = DefaultLocation.loadFromTencentLoc(mTencentLocation);
            return didiLocation;
//            insertTencentPoint(didiLocation.getLongitude(), didiLocation.getLatitude(), didiLocation.getAccuracy(),
//                    startTimestamp);
        } else if (null != errInfo) {
            if (mTencentLocationErr != TencentLocation.ERROR_OK) {
                errInfo.setSource(ErrorInfo.SOURCE_TENCENT);
                handleErrByTencentErr(mContext, mTencentLocationErr, errInfo);
//                handleErrNotified(errInfo, (int) (System.currentTimeMillis() - startTimestamp));
            } else {
                //照顾下刚启动是弱网情况下，等待时间为Const.START2LOCATION_INTERVAL_MILLIS。这样做单次请求定位也能拿到位置。
                if (System.currentTimeMillis() - timeStart < Constants.START2LOCATION_INTERVAL_MILLIS && mTencentLocation == null) {
                    //不处理赋值错误码
                    return null;
                } else {//假设弱网情况下，再等待时间内。
                    errInfo.setSource(ErrorInfo.SOURCE_TENCENT);
                    if (!LocationUtils.isLocationPermissionGranted(mContext) || !LocationSensorMonitor.getInstance(mContext).isGpsEnabled()) {
                        errInfo.setErrNo(ErrorInfo.ERROR_LOCATION_PERMISSION);
                        errInfo.setErrMessage(mContext.getString(R.string.location_err_location_permission));
                    } else if (!LocationUtils.isNetWorkConnected(mContext)) {
                        errInfo.setErrNo(ErrorInfo.ERROR_NETWORK_CONNECTION);
                        errInfo.setErrMessage(mContext.getString(R.string.location_err_network_connection));
                    } else if (errInfo.getErrNo() == 0) {
                        errInfo.setErrNo(ErrorInfo.ERROR_OTHERS);
                        errInfo.setErrMessage(mContext.getString(R.string.location_err_others));
                    }
//                    handleErrNotified(errInfo, (int) (System.currentTimeMillis() - startTimestamp));
                }
            }
        }

        return null;
    }

    @Override
    public void updateLocListenInterval(long interval) {
        mListenLocInterval = interval;
    }

    @Override
    public void setInternalLocationListener(LocationUpdateInternalListener locationUpdateInternalListener) {
        mLocationInternalListener = locationUpdateInternalListener;
    }

    @Override
    public void updateListenersInfo(StringBuilder mListenersInfo) {

    }

    private int mTencentLocationErr = TencentLocation.ERROR_OK;
    private TencentLocation mTencentLocation = null;
    private long mTencentLocationTime = 0;


    private TencentLocationListener tencentLocationListener = new TencentLocationListener() {
        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int errNo, String s) {

            LogHelper.write("-TencentLocation- status=" + s + ",errno = " + errNo + ", threadid=" + Thread.currentThread().getId());
            mTencentLocationErr = errNo;
            if(errNo == TencentLocation.ERROR_OK) {
                mTencentLocation = tencentLocation;
                mTencentLocationTime = LocationUtils.getTimeBoot();
            } else {
                mTencentLocation = null;
                mTencentLocationTime = 0;
            }

        }

        @Override
        public void onStatusUpdate(String type, int status, String info) {
            notifyTencentStatus(type, status, mLocationInternalListener);

        }
    };

    private void notifyTencentStatus(String type, int status, LocationUpdateInternalListener listener) {
        StringBuilder statusType = new StringBuilder(type);
        int didiStatus = LocationUtils.convertTencentStatus(statusType, status);

        if(didiStatus != -1 && !TextUtils.isEmpty(statusType)) {
            if (null != listener) {
                listener.onStatusUpdate(statusType.toString(), didiStatus);
            }
        }
    }

    private boolean isTencentLocationValid() {
        return (mTencentLocation != null && (LocationUtils.getTimeBoot() - mTencentLocationTime <= Constants.VALIDATE_INTERVAL_TENCENT_LOCATION));
    }


    static void handleErrByTencentErr(Context mContext, int tencentLocationErr, ErrorInfo currentErrInfo) {
        switch (tencentLocationErr) {
            case TencentLocation.ERROR_NETWORK:
                if (LocationUtils.isNetWorkConnected(mContext)) {
                    currentErrInfo.setErrNo(ErrorInfo.ERROR_TENCENT_NETWORK);
                    currentErrInfo.setErrMessage(mContext.getString(R.string.location_err_http_request_exception));
                } else {
                    currentErrInfo.setErrNo(ErrorInfo.ERROR_NETWORK_CONNECTION);
                    currentErrInfo.setErrMessage(mContext.getString(R.string.location_err_network_connection));
                }
                break;
            case TencentLocation.ERROR_BAD_JSON:
                if (!LocationUtils.isLocationPermissionGranted(mContext) || !LocationSensorMonitor.getInstance(mContext).isGpsEnabled()) {
                    currentErrInfo.setErrNo(ErrorInfo.ERROR_LOCATION_PERMISSION);
                    currentErrInfo.setErrMessage(mContext.getString(R.string.location_err_location_permission));
                } else {
                    currentErrInfo.setErrNo(ErrorInfo.ERROR_NO_ELEMENT_FOR_LOCATION);
                    currentErrInfo.setErrMessage(mContext.getString(R.string.location_err_no_element));
                }
                break;
            case TencentLocation.ERROR_WGS84:
            case TencentLocation.ERROR_UNKNOWN:
                currentErrInfo.setErrNo(ErrorInfo.ERROR_OTHERS);
                currentErrInfo.setErrMessage(mContext.getString(R.string.location_err_others));
                break;
            default:
                currentErrInfo.setErrNo(ErrorInfo.ERROR_OTHERS);
                currentErrInfo.setErrMessage(mContext.getString(R.string.location_err_others));
                break;
        }
    }

    public void requestLocationOnce(final LocationUpdateInternalListener listener, Looper looper) {
        TencentLocationManager.getInstance(mContext).requestSingleFreshLocation(new TencentLocationListener(){

            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int errCode, String s) {
                if (errCode == TencentLocation.ERROR_OK) {
                    mTencentLocation = tencentLocation;
                    mTencentLocationTime = LocationUtils.getTimeBoot();
                    listener.onLocationUpdate(DefaultLocation.loadFromTencentLoc(tencentLocation), 0);
                }
            }

            @Override
            public void onStatusUpdate(String type, int status, String info) {
                notifyTencentStatus(type, status, mLocationInternalListener);
            }
        }, looper);
    }

    public void setCoordinateType(int coordinateType) {
        if(coordinateType == DefaultLocation.COORDINATE_TYPE_GCJ02 || coordinateType == DefaultLocation.COORDINATE_TYPE_WGS84) {
            mRequestCoordinateType = coordinateType;
        }
    }
}
