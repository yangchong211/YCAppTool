package com.yc.tracesdk;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.yc.tracesdk.WifiMonitor.LocListener;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.GpsStatus.NmeaListener;
import android.os.Bundle;

public class GpsMonitor {
    private static volatile GpsMonitor mInstance;
    private Context mContext;
    /** {@code LocationManager}对象 */
    private LocationManager mLocationManager;
    private NmeaParser mNmeaParser;
    private LocListener mLocListener;
    private ArrayList<GpsSatellite> mGpsSatelliteList;
    private LatLng mLastLatLng;
    private long mLastGpsUpdatedTs = 0L;

    /** 判断GPS点处于高速的阈值，即 > 10m/s */
    private static final float GPS_POINT_HIGH_SPEED = 10.0f;
    /** 判断GPS点半径符合条件的阈值，即 < 25m */
    private static final float GPS_POINT_MAX_ACCURACY = 25.0f;
    /** GPS点处于高速时，GPS点的获取间隔，即 3s */
    private static final long GPS_POINT_HSP_TINT = 3*1000L;

    private GpsMonitor(Context context) {
        this.mContext = context.getApplicationContext();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mNmeaParser = new NmeaParser("" + System.currentTimeMillis());
        mGpsSatelliteList = new ArrayList<GpsSatellite>();
        mLastLatLng = new LatLng();
    }

    /**
     * 获取单例对象
     * 
     * @param context
     *            程序上下文对象
     * @return {@code GpsMonitor}单例对象
     */
    /* package */static GpsMonitor getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GpsMonitor.class) {
                if (mInstance == null) {
                    mInstance = new GpsMonitor(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 开始监听GPS状态
     */
    /* package */void start() {
        LogHelper.log("GpsMonitor#start()");
        try {
            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 10, mLocationListener);
            mLocationManager.addGpsStatusListener(mGpsStatusListener);
            mLocationManager.addNmeaListener(mNmeaListener);
        } catch (Throwable e) {//fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=QmMM3u-uT5ygJigMoU7opQ
            //e.printStackTrace();
        }
    }

    /**
     * 停止监听GPS状态
     */
    /* package */void stop() {
        LogHelper.log("GpsMonitor#stop()");
        try {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager.removeGpsStatusListener(mGpsStatusListener);
            mLocationManager.removeNmeaListener(mNmeaListener);
        } catch (Throwable e) {

        }
    }

    /**
     * 设置位置监听
     * 
     * @param locListener
     *            定位监听
     */
    /* package */void setLocListener(LocListener locListener) {
        mLocListener = locListener;
    }

    /**
     * 去掉位置监听
     */
    /* package */void removeLocListener() {
        mLocListener = null;
    }

    /**
     * 获取当前速度
     * 
     * @return 当前速度,获取不到时返回-1
     */
    /* package */float getCurrentSpeed() {
        if (mNmeaParser != null) {
            Location loc = mNmeaParser.getLocation();
            if (loc != null && loc.hasSpeed()) {
                return loc.getSpeed();
            }
        }
        return -1;
    }

    /**
     * 获取Gps信息
     * 
     * @param location
     *            定位信息
     * @return Gps信息
     */
    private GpsInfo getGpsInfo(Location location) {

        if (location == null) return null;

        GpsInfo gpsInfo = new GpsInfo();

        Location nmeaLocation = mNmeaParser.getLocation();
        if (nmeaLocation != null) {
            Bundle bundle = nmeaLocation.getExtras();
            if (bundle != null) {
                gpsInfo.pdop = bundle.getFloat(NmeaParser.PDOP, 0);
                gpsInfo.hdop = bundle.getFloat(NmeaParser.HDOP, 0);
                gpsInfo.vdop = bundle.getFloat(NmeaParser.VDOP, 0);
            }
        }

        gpsInfo.longtitude = location.getLongitude();
        gpsInfo.latitude = location.getLatitude();
        if (location.hasAltitude()) {
            gpsInfo.altitude = location.getAltitude();
        }
        if (location.hasSpeed()) {
            gpsInfo.speed = location.getSpeed();
        }
        if (location.hasAccuracy()) {
            gpsInfo.accuracy = location.getAccuracy();
        }
        if (location.hasBearing()) {
            gpsInfo.bearing = location.getBearing();
        }
        gpsInfo.gpsTs = location.getTime();

        ArrayList<GpsSatellite> satelliteList = new ArrayList<GpsSatellite>(mGpsSatelliteList);
        gpsInfo.numSatellites = satelliteList.size();
        gpsInfo.satelliteList = satelliteList;

        return gpsInfo;
    }

    /**
     * 将gps信息转换为字节数组
     * 
     * @param gpsInfo
     *            gps信息
     * @return gps信息的细节数组
     */
    private byte[] convertGspInfo2ByteArray(GpsInfo gpsInfo) {
        com.yc.tracesdk.LocInfoProtoBuf.GpsInfo.Builder builder = new com.yc.tracesdk.LocInfoProtoBuf.GpsInfo.Builder();

        builder.time(gpsInfo.time);
        builder.longitude(gpsInfo.longtitude);
        builder.latitude(gpsInfo.latitude);
        builder.altitude(gpsInfo.altitude);
        builder.accuracy(gpsInfo.accuracy);
        builder.pdop(gpsInfo.pdop);
        builder.hdop(gpsInfo.hdop);
        builder.vdop(gpsInfo.vdop);
        builder.speed(gpsInfo.speed);
        builder.gps_ts(gpsInfo.gpsTs);
        builder.bearing(gpsInfo.bearing);
        builder.num_satellites(gpsInfo.numSatellites);

        /* 暂时不上传卫星列表
        for (GpsSatellite satellite : gpsInfo.satelliteList) {
            LocInfoProtoBuf.GpsInfo.SatelliteInfo.Builder satelliteBuilder = LocInfoProtoBuf.GpsInfo.SatelliteInfo
                    .newBuilder();
            satelliteBuilder.setAzimuth(satellite.getAzimuth());
            satelliteBuilder.setElevation(satellite.getElevation());
            satelliteBuilder.setPrn(satellite.getPrn());
            satelliteBuilder.setSnr(satellite.getSnr());
            builder.addSatellite(satelliteBuilder);
        }
        */

        return builder.build().toByteArray();
    }

    /**
     * 保存Gps信息
     * 
     * @param location
     *            定位信息
     */
    private void saveGpsInfo(Location location) {
        GpsInfo gpsInfo = getGpsInfo(location);
        if (gpsInfo == null) {
            return;
        }

        float[] results = new float[15];
        Location.distanceBetween(gpsInfo.latitude, gpsInfo.longtitude, mLastLatLng.lat, mLastLatLng.lng, results);
        float distance = results[0];
        if ((distance >= 10 && gpsInfo.speed < 10) || distance > 100) {

            try {
                byte[] gpsInfoByteArray = convertGspInfo2ByteArray(gpsInfo);
                DBHandler.getInstance(mContext).insertGpsData(gpsInfoByteArray);
            } catch (Exception e) {}
            /* 将json格式的数据写入文件 */
            LogHelper.writeToFile(gpsInfo.toJsonString());

            mLastLatLng.lat = gpsInfo.latitude;
            mLastLatLng.lng = gpsInfo.longtitude;
        }
    }

    /**
     * 判断GPS点有效性
     * <br/> 半径 < 25m <br/> 速度 <= 10m/s
     * <br/> 速度 > 10m/s 时，与上次获取GPS的间隔 >= 3s
     * @param loc android标准location
     * @return true = 有效，准许获取此GPS <br/> false = 无效
     */
    private boolean isValidGps(Location loc) {
        if (!loc.hasAccuracy() || loc.getAccuracy() >= GPS_POINT_MAX_ACCURACY) {
            return false;
        }
        if (!loc.hasSpeed()) {
            return false;
        }
        if (loc.getSpeed() <= GPS_POINT_HIGH_SPEED) {
            return true;
        }
        if (System.currentTimeMillis() - mLastGpsUpdatedTs >= GPS_POINT_HSP_TINT) {
            return true;
        }
        return false;
    }

    /** 定位状态的监听 */
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location loc) {
            LogHelper.log("#onLocationChanged");

            if (loc == null || !isValidGps(loc)) {
                return ;
            }
            mLastGpsUpdatedTs = System.currentTimeMillis();

            saveGpsInfo(loc);

            if (mLocListener != null) {
                mLocListener.onLocationChanged();
            }

            if (loc != null) {
                LogHelper.log("current location: " + loc.toString());
            } else {
            }
        }

        public void onProviderDisabled(final String s) {
            LogHelper.log("#onProviderDisabled");
        }

        public void onProviderEnabled(final String s) {
            LogHelper.log("#onProviderEnabled");
        }

        public void onStatusChanged(final String s, final int i, final Bundle b) {
            LogHelper.log("#onStatusChanged");
        }
    };

    /** GPS状态的监听 */
    private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                mGpsSatelliteList.clear();

                GpsStatus gpsStatus = null;
                try {
                    gpsStatus = mLocationManager.getGpsStatus(null);
                } catch (NullPointerException e) {}

                if (gpsStatus != null) {
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                        if (s.usedInFix()) {
                            mGpsSatelliteList.add(s);
                            // LogHelper.log("方位角：" + s.getAzimuth());
                            // LogHelper.log("卫星仰角：" + s.getElevation());
                            // LogHelper.log("伪随机噪声码：" + s.getPrn());
                            // LogHelper.log("信噪比：" + s.getSnr());

                        }
                    }

                    //Log.i("liuc", "satellite num " + mGpsSatelliteList.size() + " fixed of total " + count);
                }
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                break;
            }
        };
    };

    /** 获取到NMEA报文的监听 */
    private NmeaListener mNmeaListener = new NmeaListener() {

        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            mNmeaParser.parseSentence(nmea);
        }

    };

    /**
     * 需要收集的Gps信息
     */
    private final class GpsInfo {
        public long time;
        public double longtitude;
        public double latitude;
        public double altitude = -1;
        public float accuracy = -1;
        public float pdop;
        public float hdop;
        public float vdop;
        public float speed = -1;
        public long gpsTs;
        public float bearing = -1;
        public int numSatellites;
        public ArrayList<GpsSatellite> satelliteList;

        public GpsInfo() {
            this.time = System.currentTimeMillis();
        }
        
        public String toJsonString() {
            JSONObject json = new JSONObject();
            try {
                json.put("time", time);
                json.put("longtitude", longtitude);
                json.put("latitude", latitude);
                json.put("altitude", altitude);
                json.put("accuracy", accuracy);
                json.put("pdop", pdop);
                json.put("hdop", hdop);
                json.put("vdop", vdop);
                json.put("speed", speed);
                json.put("gpsTs", gpsTs);
                json.put("bearing", bearing);
                json.put("numSatellites", numSatellites);
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            return json.toString();
        }
    }

    private final class LatLng {
        public double lat;
        public double lng;
    }
}
