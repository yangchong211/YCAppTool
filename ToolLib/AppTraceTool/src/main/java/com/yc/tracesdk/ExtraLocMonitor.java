package com.yc.tracesdk;

import android.content.Context;
import android.location.Location;

import com.yc.tracesdk.LocInfoProtoBuf.ExtraLocInfo;

public class ExtraLocMonitor {
    private static volatile ExtraLocMonitor mInstance;
    private Context mContext;
    private Location mLastLocation;
    /** 判定位置发生改变的最小距离 */
    private final static int MIN_DISTANCE = 10;
    private volatile boolean mRun = false;

    private ExtraLocMonitor(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 获取单例对象
     * 
     * @param context
     *            程序上下文对象
     * @return {@code ExtraLocMonitor}单例对象
     */
    public static ExtraLocMonitor getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ExtraLocMonitor.class) {
                if (mInstance == null) {
                    mInstance = new ExtraLocMonitor(context);
                }
            }
        }
        return mInstance;
    }

    public void onLocationChanged(Location loc) {
        LogHelper.log("ExtraLocMonitor#onLocationChanged");
        if (!mRun) {
            return;
        }
        if (mLastLocation == null) {
            saveLocation(loc);
            mLastLocation = loc;
            return;
        }

        float[] results = new float[15];
        Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(), loc.getLatitude(),
                loc.getLongitude(), results);
        float distance = results[0];
        if (distance >= MIN_DISTANCE) {
            saveLocation(loc);
            mLastLocation = loc;
        }
    }

    /* package */void start() {
        this.mRun = true;
    }

    /* package */void stop() {
        this.mRun = false;
    }

    /**
     * 将位置信息写入数据库
     * 
     * @param loc
     *            位置信息
     */
    private void saveLocation(Location loc) {
        LogHelper.log("ExtraLocMonitor#saveLocation");
        byte[] data = convertExtraLocInfo2ByteArray(loc);
        DBHandler.getInstance(mContext).insertExtraLocData(data);
    }

    /**
     * 将位置信息转换为字节数组
     * 
     * @param loc
     *            位置对象
     * @return 位置信息的字节数组
     */
    private byte[] convertExtraLocInfo2ByteArray(Location loc) {
        ExtraLocInfo.Builder builder = new ExtraLocInfo.Builder();

        builder.time(System.currentTimeMillis());
        builder.latitude(loc.getLatitude());
        builder.longitude(loc.getLongitude());
        builder.gps_ts(loc.getTime());
        builder.altitude(loc.hasAltitude() ? loc.getAltitude() : -1);
        builder.accuracy(loc.hasAccuracy() ? loc.getAccuracy() : -1);
        builder.speed(loc.hasSpeed() ? loc.getSpeed() : -1);
        builder.bearing(loc.hasBearing() ? loc.getBearing() : -1);

        return builder.build().toByteArray();
    }
}
