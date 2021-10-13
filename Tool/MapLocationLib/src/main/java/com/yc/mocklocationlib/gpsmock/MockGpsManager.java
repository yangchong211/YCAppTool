package com.yc.mocklocationlib.gpsmock;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yc.mocklocationlib.gpsmock.bean.LatLng;
import com.yc.mocklocationlib.gpsmock.point.GcjPointer;
import com.yc.mocklocationlib.gpsmock.point.WgsPointer;
import com.yc.mocklocationlib.gpsmock.utils.GpsMockConfig;
import com.yc.mocklocationlib.gpsmock.utils.LogMockUtils;

import java.util.List;

public final class MockGpsManager {

    private static float sBearing = 0.0F;
    private static float sSpeed = 3.0F;
    private static MockGpsManager sInstance;
    int triedInit;
    private LocationManager mLocationManager;
    private final Location mLocation = new Location(LocationManager.GPS_PROVIDER);
    private WgsPointer mLastPoint;
    /**
     * 判断mock定位是否可用，尝试三次
     */
    private boolean mIsAvailable;
    private final Handler mHandlerUI;
    private final Handler mHandlerWorker;
    private final Context mContext;

    /**
     * 初始化判断模拟位置是否可用
     */
    private final Runnable laterInitRunnable = new Runnable() {
        public void run() {
            mHandlerWorker.removeCallbacks(laterInitRunnable);
            mIsAvailable = getUseMockPositionEnable();
            if (!mIsAvailable){
                if (triedInit < 3) {
                    mHandlerUI.postDelayed(laterInitRunnable, (long)(triedInit * 2000));
                    LogMockUtils.log("MockGpsManager", "postDelayed再试一次");
                }
            }
        }
    };

    /**
     * 判断模拟位置是否启用
     * @return
     */
    private boolean getUseMockPositionEnable() {
        boolean isAvailable;
        try {
            List<String> providers = mLocationManager.getProviders(true);
            LogMockUtils.log("MockGpsManager", "当前可用的 providers 有:" + providers);
            //创建一个模拟位置提供程序并将其添加到活动提供程序集。
            mLocationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                    false, false, false,
                    false, true, true,
                    true, 0, 5);
            //为给定的提供程序设置启用模拟的值。该值将在适当的地方使用提供程序的任何实际值。
            mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            isAvailable = true;
        } catch (Exception var6) {
            String msg = "请到开发者选项中打开模拟定位\n初始化失败 tried " + triedInit;
            disableTestProvider();
            LogMockUtils.e("MockGpsManager", msg);
            isAvailable = false;
        } finally {
            ++triedInit;
        }
        return isAvailable;
    }

    /**
     * mock gps 经纬度
     */
    private final Runnable mockGpsRunnable = new Runnable() {
        public void run() {
            mHandlerWorker.removeCallbacks(mockGpsRunnable);
            //获取经纬度
            double latitude = GpsMockManager.getInstance().getLatitude();
            double longitude = GpsMockManager.getInstance().getLongitude();
            //为给定的提供程序设置模拟位置
            teleportWgs(latitude,longitude);
            if (GpsMockManager.getInstance().isMocking()) {
                LogMockUtils.log("MockGpsManager", "延迟3秒mock经纬度");
                mHandlerWorker.postDelayed(this, 3000L);
            }
        }
    };

    /**
     * 创建的时候初始化操作
     * @param context                           上下文
     */
    private MockGpsManager(@NonNull Context context) {
        LogMockUtils.log("MockGpsManager", "模拟路测工具初始化");
        HandlerThread handlerThread = new HandlerThread("MockGpsManager Thread");
        handlerThread.start();
        mHandlerWorker = new Handler(handlerThread.getLooper());
        mHandlerUI = new Handler(Looper.getMainLooper());
        mContext = context.getApplicationContext();
        //获取LocationManager对象
        mLocationManager = (LocationManager)
                mContext.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        removeTestProvider();
        //初始化，主要是判断mock经纬度是否可用
        mHandlerUI.post(laterInitRunnable);
    }

    public static float getBearing() {
        return sBearing;
    }

    public static void setBearing(float bearing) {
        LogMockUtils.log("MockGpsManager", "⚠️setBearing() called with" +
                ": bearing = [" + bearing + "]");
        sBearing = bearing;
    }

    public static float getSpeed() {
        return sSpeed;
    }

    public static void setSpeed(float speed) {
        LogMockUtils.log("MockGpsManager", "⚠️setSpeed() called with" +
                ": speed = [" + speed + "]");
        sSpeed = speed;
    }

    public static String getSpeedBearingStr() {
        return "sSpeed = [" + sSpeed + "], bearing = [" + sBearing + "]";
    }

    public static MockGpsManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MockGpsManager(context);
        }
        return sInstance;
    }

    /**
     * 移除test操作
     */
    public void disableTestProvider() {
        try {
            if (mLocationManager == null) {
                mLocationManager = (LocationManager)
                        mContext.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }
            //移除操作
            removeTestProvider();
            //为给定的提供程序设置启用模拟的值。该值将在适当的地方使用提供程序的任何实际值。
            //enabled设置成false表示不可用
            mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, false);
        } catch (Exception var2) {
        }
    }

    public boolean changeMockLoc(@NonNull Context context, double latitude, double longitude) {
        if (longitude <= 180.0D && longitude >= -180.0D) {
            if (latitude <= 90.0D && latitude >= -90.0D) {
                GpsMockManager.getInstance().mockLocation(latitude, longitude);
                GpsMockConfig.saveMockLocation(context, new LatLng(latitude, longitude));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 开始mock轮训
     */
    public void start() {
        if (GpsMockManager.getInstance().isMocking()) {
            LogMockUtils.log("MockGpsManager", "start");
            //发送消息
            mHandlerWorker.post(mockGpsRunnable);
        } else {
            Toast.makeText(mContext,"mock stop",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 停止mock
     */
    public void stop() {
        LogMockUtils.log("MockGpsManager", "stop");
        mHandlerWorker.removeCallbacks(mockGpsRunnable);
    }

    @SuppressLint({"DefaultLocale"})
    public void teleportGcj(double lat, double lng) {
        GcjPointer pointer = new GcjPointer(lat, lng);
        WgsPointer pointerWgs = pointer.toWgsPointer();
        LogMockUtils.log("MockGpsManager", String.format(
                "MockGpsManager 请求模拟定位 火星坐标 lat=%g, lng=%g", lat, lng));
        teleportWgs(pointerWgs.getLatitude(), pointerWgs.getLongitude());
    }

    /**
     * 为给定的提供程序设置模拟位置
     * @param lat                           纬度
     * @param lng                           经度
     */
    public void teleportWgs(double lat, double lng) {
        if (!mIsAvailable) {
            String msg = "第一、设置我们手机允许模拟定位【系统设置】=》开发者选项=》" +
                    "打开允许模拟位置\n第二、进入位置服务切换成只是用gps确定位置";
            onMessage(msg);
            LogMockUtils.log("MockGpsManager", "⚠️teleportWgs() called with " +
                    "mIsAvailable: lat = [" + lat + "], lng = [" + lng + "], sSpeed =" +
                    " [" + sSpeed + "], bearing = [" + sBearing + "]");
            //Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            //return;
        }

        try {
            WgsPointer pointerWgs = new WgsPointer(lat, lng);
            mLastPoint = pointerWgs;
            mLocation.setLatitude(pointerWgs.getLatitude());
            mLocation.setLongitude(pointerWgs.getLongitude());
            mLocation.setAltitude(0.0D);
            mLocation.setAccuracy(5.0F);
            mLocation.setBearing(sBearing);
            mLocation.setSpeed(sSpeed);
            mLocation.setTime(System.currentTimeMillis());
            if (VERSION.SDK_INT >= 17) {
                mLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }

            LogMockUtils.log("MockGpsManager", "⚠️teleportWgs() 中 GPS Provider 可用, " +
                    "设置坐标: lat = [" + lat + "], lng = [" + lng + "], sSpeed = ["
                    + sSpeed + "], bearing = [" + sBearing + "]");
            // 为给定的提供程序设置模拟位置。此位置将用于替代提供者提供的任何实际位置。
            // location对象必须设置一个最小数量的字段，才能被认为是有效的LocationProvider location
            mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mLocation);
        } catch (SecurityException var8) {
            String msg = "请到开发者选项中打开模拟定位";
            onMessage(msg);
        } catch (Exception var9) {
            LogMockUtils.log("MockGpsManager", "Exception Provider \"gps\" unknown. 拦截下来了.");
            try {
                removeTestProvider();
                //创建一个模拟位置提供程序并将其添加到活动提供程序集。
                mLocationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                        false, false, false,
                        false, true, true,
                        true, 0, 5);
                //为给定的提供程序设置启用模拟的值。该值将在适当的地方使用提供程序的任何实际值。
                mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
                //递归调用
                teleportWgs(mLastPoint.getLatitude(), mLastPoint.getLongitude());
            } catch (Exception var7) {
                var7.printStackTrace();
                disableTestProvider();
            }
        }
    }

    public void destroy(){
        stop();
        disableTestProvider();
    }

    private void removeTestProvider() {
        try {
            LogMockUtils.log("MockGpsManager", "removeTestProvider();" +
                    "Removing Test providers");
            mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        } catch (Exception var2) {
            LogMockUtils.log("MockGpsManager", "⚠️Got exception in removing test " +
                    " Exception" + "provider :" + var2.toString());
        }
    }

    private void onMessage(String msg) {
        LogMockUtils.log("MockGpsManager", msg);
    }

}

