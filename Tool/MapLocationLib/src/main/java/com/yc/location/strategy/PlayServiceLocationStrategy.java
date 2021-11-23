package com.yc.location.strategy;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import com.tencent.map.geolocation.TencentLocation;
import com.yc.location.constant.Constants;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.listener.FirstSystemLocationListener;
import com.yc.location.manager.GoogleFLPManager;
import com.yc.location.manager.NLPManager;
import com.yc.location.R;
import com.yc.location.monitor.LocationSensorMonitor;
import com.yc.location.utils.LocationUtils;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.data.ETraceSource;
import com.yc.location.listener.LocationUpdateInternalListener;
import com.yc.location.utils.NetworkUtils;

/**
 * 通过google推荐的使用google play service来定位，辅助其他方式兜底
 */

public class PlayServiceLocationStrategy implements ILocationStrategy {

    private final Context mContext;

    private TencentLocationStrategy mTencentManager;

    private NLPManager mNLPManager;
    private LocationUpdateInternalListener mLocationUpdateInternalListener;
    private GoogleFLPManager mFlpManager;
    private boolean mIsOnlyOSLocation = false;

    public PlayServiceLocationStrategy(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 通过GoogleApiClient开启定位
     */
    public void start(final Handler workHandler) {
        //1.开启play service定位
        initGoogleFlpManager(workHandler.getLooper());

        mIsOnlyOSLocation = LocationUtils.isOnlyOSLocationAbroad();
        if (!mIsOnlyOSLocation) {
            //2.开启腾讯定位，作为兜底
            startTencentLocate(workHandler);
        }

        //3.开启NLP定位监，某些手机上GMS使用不了NLP定位（NLP模块不是使用gms的）
        mNLPManager = new NLPManager(mContext, workHandler);
        mNLPManager.start();
    }

    private void startTencentLocate(Handler workHandler) {
        mTencentManager = new TencentLocationStrategy(mContext);
        mTencentManager.setCoordinateType(DefaultLocation.COORDINATE_TYPE_WGS84);
        mTencentManager.setInternalLocationListener(mLocationUpdateInternalListener);
        mTencentManager.start(workHandler);
    }

    /**
     * 停止定位
     */
    public void stop() {
        //停止flp
        if (null != mFlpManager) {
            mFlpManager.destory();
            mFlpManager = null;
        }

        if (!mIsOnlyOSLocation && null != mTencentManager) {
            mTencentManager.stop();
            mTencentManager = null;
        }
        if (null != mNLPManager) {
            mNLPManager.stop();
            mNLPManager = null;
        }
        mLocationUpdateInternalListener = null;
    }


    /**
     * 根据各个定位策略模块自己的策略，提取出本策略的有效定位位置。
     * @return 位置
     */
    public DefaultLocation retrieveLocation(ErrorInfo errInfo) {
        DefaultLocation location = null;
        Location flpLoc = null;
        if (null != mFlpManager) {
            flpLoc = mFlpManager.getGoogleFlpLoc();
        }
        DefaultLocation tencentLoc = null;
        if (null != mTencentManager) {
            tencentLoc = mTencentManager.retrieveLocation(null);
        }
        Location nlpLocation = null;
        if (null != mNLPManager) {
            nlpLocation = mNLPManager.getNLPLocation();
        }
        if (null != flpLoc) {
            location = DefaultLocation.loadFromSystemLoc(flpLoc, ETraceSource.googleflp, DefaultLocation.COORDINATE_TYPE_WGS84);
        } else if (null != tencentLoc) {
            if (TencentLocation.GPS_PROVIDER.equals(tencentLoc.getProvider())) {
                location = tencentLoc;
            } else {
                if (null != nlpLocation) {
                    location = DefaultLocation.loadFromSystemLoc(nlpLocation, ETraceSource.nlp, DefaultLocation.COORDINATE_TYPE_WGS84);
                } else {
                    location = tencentLoc;
                }
            }
        } else {
            if (null != nlpLocation) {
                location = DefaultLocation.loadFromSystemLoc(nlpLocation, ETraceSource.nlp, DefaultLocation.COORDINATE_TYPE_WGS84);
            }
        }

        if (null == location) {
            produceErr(errInfo);
        }

        return location;
    }

    @Override
    public void updateLocListenInterval(long interval) {
    }

    @Override
    public void setInternalLocationListener(LocationUpdateInternalListener locationUpdateInternalListener) {
        mLocationUpdateInternalListener = locationUpdateInternalListener;
    }

    @Override
    public void updateListenersInfo(StringBuilder mListenersInfo) {

    }

    private void initGoogleFlpManager(Looper looper) {
        mFlpManager = new GoogleFLPManager(mContext);
        mFlpManager.registerFirstLocationListener(new FirstSystemLocationListener() {
            @Override
            public void onFirstLocation(Location location) {
                if (null != mLocationUpdateInternalListener && null != location) {
                    mLocationUpdateInternalListener.onLocationUpdate(DefaultLocation.loadFromSystemLoc(location, ETraceSource.googleflp,
                            DefaultLocation.COORDINATE_TYPE_WGS84), 0);
                }
            }
        });
        mFlpManager.init(Constants.DEF_GPS_FLP_MONITOR_INTERVAL, looper);
    }

    /**
     * 按优先级生成对应的错误码
     */
    private void produceErr(ErrorInfo errInfo) {
        //是否为没有权限(获取可以尝试用manifest中权限常量判断)
        if (!LocationUtils.isLocationPermissionGranted(mContext) || !LocationSensorMonitor.getInstance(mContext).isGpsEnabled()) {
            errInfo.setErrNo(ErrorInfo.ERROR_LOCATION_PERMISSION);
            errInfo.setErrMessage(mContext.getString(R.string.location_err_location_permission));
            //lcc: fix bug: 4999, deepClone异常，可能导致instantReqData为空。
        } else if (!NetworkUtils.isNetWorkConnected(mContext)) {
            errInfo.setErrNo(ErrorInfo.ERROR_NETWORK_CONNECTION);
            errInfo.setErrMessage(mContext.getString(R.string.location_err_network_connection));
        } else {
            errInfo.setErrNo(ErrorInfo.ERROR_OTHERS);
            errInfo.setErrMessage(mContext.getString(R.string.location_err_others));
        }
    }
}
