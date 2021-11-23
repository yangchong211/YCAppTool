package com.yc.location.manager;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.constant.Constants;
import com.yc.location.listener.FirstSystemLocationListener;
import com.yc.location.log.LogHelper;
import com.yc.location.utils.LocationUtils;

/**
 * google flp 的管理类，可以从此获得有效flp定位位置等
 */
public class GoogleFLPManager {

    private final Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private volatile Location mGoogleFlpLocation;
    private long mGoogleFlpLocationTime;
    private FirstSystemLocationListener mFirstLocationListener;


    public GoogleFLPManager(Context context) {
        mContext = context;
    }

    public void init(final long interval, final Looper looper) {
        if (mContext == null) {
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //开始请求定位
                        try {
                            //TODO 可以优化，及时按最小监听频率请求定位，省电。
                            final PendingResult<Status> result = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createLocationRequest(interval), mLocationListener, looper);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected LocationRequest createLocationRequest(long interval) {
        LocationRequest locationRequest = new LocationRequest();
        long lowFrequency = LocationUpdateOption.IntervalMode.LOW_FREQUENCY.getValue();
        long finalInterval = (interval < lowFrequency ? interval : lowFrequency);
        locationRequest.setInterval(finalInterval);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private long mFlpBamaiLogTime;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (!LocationUtils.locCorrect(location)) {
                return;
            }
            if (DefaultLocationManager.enableMockLocation || (!LocationUtils.isMockLocation(location) && !LocationUtils.isMockSettingsON(mContext))) {
                //及时通知获得的第一个定位位置
                if (null == mGoogleFlpLocation) {
                    if (null != mFirstLocationListener) {
                        mFirstLocationListener.onFirstLocation(location);
                    }
                }

                mGoogleFlpLocation = location;
                mGoogleFlpLocationTime = LocationUtils.getTimeBoot();
                //有间隔地写日志。
                long nowTime = System.currentTimeMillis();
                if((nowTime - mFlpBamaiLogTime) > Constants.MIN_INTERVAL_BAMAI_GPS_NLP_LOCATION) {
                    LogHelper.logBamai("callback gms location: " + location.getLongitude()
                            + "," + location.getLatitude() + ", " +location.getSpeed() + ", " + location.getBearing());
                    mFlpBamaiLogTime = nowTime;
                }
            }

        }
    };

    public Location getGoogleFlpLoc() {
        if (LocationUtils.locCorrect(mGoogleFlpLocation)) {
            if ((LocationUtils.getTimeBoot() - mGoogleFlpLocationTime > Constants.VALIDATE_INTERVAL_GOOGLE_FLP_LOCATION)) {
                mGoogleFlpLocation = null;
            }
        } else {
            mGoogleFlpLocation = null;
        }
        return mGoogleFlpLocation;
    }

    public void destory() {
        if (null != mGoogleApiClient) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
                mGoogleApiClient.disconnect();
                mGoogleApiClient = null;
            }
        }

        mGoogleFlpLocationTime = 0;
        mGoogleFlpLocation = null;
        mFirstLocationListener = null;
    }

    public void registerFirstLocationListener(FirstSystemLocationListener locationInternalListener) {
        this.mFirstLocationListener = locationInternalListener;
    }
}
