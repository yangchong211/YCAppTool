package com.yc.location.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import com.google.android.gms.common.GoogleApiAvailability;
import com.tencent.map.geolocation.TencentLocationListener;
import com.yc.location.constant.Constants;
import com.yc.location.log.LogHelper;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.mode.cell.Cgi;

import java.util.List;

public final class LocationUtils {

    private static int iSdk = 0;
    private static int mCoordinateType = DefaultLocation.COORDINATE_TYPE_GCJ02;
    private static boolean mIsGpsMocked = false;
    private static boolean onlyOSLocationAbroad = false;

    /**
     * 获取系统自启用以来的时间（单位：毫秒）
     * @return                  long时间戳
     */
    public static long getTimeBoot() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * 获取当前时间
     * @return                  当前时间戳
     */
    public static long getNowTime(){
        return System.currentTimeMillis();
    }

    /**
     * 获取手机SDK版本
     *
     * @param
     * @return int
     */
    public static int getSdk() {
        if (iSdk > 0) {
            return iSdk;
        }
        int iLv = 0;
        String strVersion = "android.os.Build$VERSION";
        try {
            iLv = ReflectUtils.getStaticIntProp(strVersion, "SDK_INT");
        } catch (Exception e) {
            try {
                Object obj = ReflectUtils.getStaticProp(strVersion, "SDK");
                iLv = Integer.parseInt(obj.toString());
            } catch (Exception e1) {
                LogHelper.logFile(e1.toString());
            }
        }

        if (iLv > 0) {
            iSdk = iLv;
        }

        return iLv;
    }

    public  static boolean isLocationSwitchOff(Context context) {
        if (getLocationSwitchLevel(context) == Settings.Secure.LOCATION_MODE_OFF) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得位置开关的状态。
     * @return -1：无法获得状态， 0:定位开关关闭，1:仅GPS定位，2:仅网络定位，3:高精度定位（GPS加网络）。参见: {@link Settings.Secure#LOCATION_MODE}
     */
    public static int getLocationSwitchLevel(Context context) {
        int locationLevel = -1;
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                locationLevel = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
                switch (locationLevel) {
                    case Settings.Secure.LOCATION_MODE_OFF:
                        locationLevel = Constants.LOCATION_LEVEL_OFF;
                        break;
                    case Settings.Secure.LOCATION_MODE_SENSORS_ONLY:
                        locationLevel = Constants.LOCATION_LEVEL_SENSORS_ONLY;
                        break;
                    case Settings.Secure.LOCATION_MODE_BATTERY_SAVING:
                        locationLevel = Constants.LOCATION_LEVEL_BATTERY_SAVING;
                        break;
                    case Settings.Secure.LOCATION_MODE_HIGH_ACCURACY:
                        locationLevel = Constants.LOCATION_LEVEL_HIGH_ACCURACY;
                        break;
                    default:
                        break;
                }
            } /*else {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationLevel = Const.LOCATION_LEVEL_SENSORS_ONLY;
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        locationLevel = Const.LOCATION_LEVEL_HIGH_ACCURACY;
                    }
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationLevel = Const.LOCATION_LEVEL_BATTERY_SAVING;
                } else {
                    locationLevel = Const.LOCATION_LEVEL_OFF;
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationLevel;
    }

    public static boolean isLocationPermissionGranted(Context context) {
        if (checkSystemPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && checkSystemPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static int getLocationPermissionLevel(Context context) {
        int level = 0;
        if (checkSystemPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            level = level|2;
        }
        if (checkSystemPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            level = level|1;
        }
        return level;
    }
	/**
     * 获取手机联网类型
     *
     * @param
     * @return int
     */
    public static int getNetT(NetworkInfo ni) {
        int iNetT = -1;
        if (ni == null) {
            iNetT = -1;
        } else if (!ni.isAvailable()) {
            iNetT = -1;
        } else if (!ni.isConnected()) {
            iNetT = -1;
        } else {
            iNetT = ni.getType();
        }
        return iNetT;
    }


    public  static void saveAppVersion(Context context, String version) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME_APPVERSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PREFS_NAME_APPVERSION, version);
        editor.apply();
    }

    public  static String readAppVersion(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME_APPVERSION, Context.MODE_PRIVATE);
        String version = "";
        try {
            version = settings.getString(Constants.PREFS_NAME_APPVERSION, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }


    public static Object getServ(Context ctx, String strServName) {
        if (ctx == null) {
            return null;
        }
        Object o = null;
        try {
            o = ctx.getApplicationContext().getSystemService(strServName);
        } catch (Throwable e) {
        }
        return o;
    }

    public static boolean hasGpsProvider(LocationManager lm) {
        if (lm == null) {
            return false;
        }
        final List<String> lstProviders = lm.getAllProviders();
        if (lstProviders == null) {
            return false;
        }
        return lstProviders.contains(LocationManager.GPS_PROVIDER);
    }

    public static boolean hasPassiveProvider(LocationManager lm) {
        if (lm == null) {
            return false;
        }
        final List<String> lstProviders = lm.getAllProviders();
        if (lstProviders == null) {
            return false;
        }
        return lstProviders.contains(LocationManager.PASSIVE_PROVIDER);
    }

    /**
     * 判断位置记录是否正确
     * @param loc                           location
     * @return                              是否正确
     */
    public static boolean locCorrect(Location loc) {
        if (loc == null) {
            return false;
        }
        boolean bFine = true;
        double dLon = loc.getLongitude();
        double dLat = loc.getLatitude();
        if (dLon == 0.0d && dLat == 0.0d && !loc.hasAccuracy()) {
            bFine = false;
        } else if (dLon > 180.0d || dLat > 90.0d) {
            LogHelper.logFile("invalid loc lon:lat : " + dLon + ":" + dLat);
            bFine = false;
        } else if (dLon < -180.0d || dLat < -90.0d) {
            LogHelper.logFile("invalid loc lon:lat : " + dLon + ":" + dLat);
            bFine = false;
        }
        return bFine;
    }

    /**
     * 判断CELLLOCATION是GSM还是CDMA
     *
     * @param
     * @return int
     */
    public static int getCellLocT(final CellLocation cellLoc, Context ctx) {
        int iType = Cgi.iDefCgiT;
        if (LocationUtils.airPlaneModeOn(ctx)) {
            iType = Cgi.iDefCgiT;
        } else if (cellLoc == null) {
            iType = Cgi.iDefCgiT;
        } else if (cellLoc instanceof GsmCellLocation) {
            iType = Cgi.iGsmT;
        } else {
            try {
                Class.forName("android.telephony.cdma.CdmaCellLocation");
                iType = Cgi.iCdmaT;
            } catch (Exception e) {
                LogHelper.logFile(e.toString());
            }
        }
        return iType;
    }

    public static boolean airPlaneModeOn(Context ctx) {
        if (ctx == null) {
            return false;
        }
        ContentResolver cr = ctx.getContentResolver();
        Object obj = null;
        if (getSdk() < 17) {
            try {
                String strSystem = "android.provider.Settings$System";
                obj = ReflectUtils.getStaticProp(strSystem, "AIRPLANE_MODE_ON");
                String str = ((String) obj).toString();
                Object[] oa = new Object[2];
                oa[0] = cr;
                oa[1] = str;
                Class<?>[] ca = new Class[2];
                ca[0] = ContentResolver.class;
                ca[1] = String.class;
                obj = ReflectUtils.invokeStaticMethod(strSystem, "getInt", oa, ca);
                return ((Integer) obj).intValue() == 1;
            } catch (Exception e) {
                LogHelper.logFile("AIRPLANE_MODE: exceptioin 0: " + e.toString());
            }
        } else {
            try {
                String strGlobal = "android.provider.Settings$Global";
                obj = ReflectUtils.getStaticProp(strGlobal, "AIRPLANE_MODE_ON");
                String str = ((String) obj).toString();
                Object[] oa = new Object[2];
                oa[0] = cr;
                oa[1] = str;
                Class<?>[] ca = new Class[2];
                ca[0] = ContentResolver.class;
                ca[1] = String.class;
                obj = ReflectUtils.invokeStaticMethod(strGlobal, "getInt", oa, ca);
                return ((Integer) obj).intValue() == 1;
            } catch (Exception e) {
                LogHelper.logFile("AIRPLANE_MODE: exceptioin 1: " + e.toString());
            }
        }

//        LogHelper.logBamai("AIRPLANE_MODE: false");
        return false;
    }

    private static String[] saCacheMccMnc = null;
    /**
     * 获取手机MCC和MNC
     *
     * @param
     * @return String[]
     */
    public static String[] getMccMnc(TelephonyManager tm) {
        String strNO = null;
        if (tm != null) {
            strNO = tm.getNetworkOperator();
        }
        String[] saMccMnc = { "0", "0" };
        boolean bNoFine = true;
        if (TextUtils.isEmpty(strNO)) {
            bNoFine = false;
        } else if (!TextUtils.isDigitsOnly(strNO)) {
            bNoFine = false;
        } else if (strNO.length() <= 4) {
            bNoFine = false;
        }
        if (bNoFine) {
            saMccMnc[0] = strNO.substring(0, 3);
            char[] caNo = strNO.substring(3).toCharArray();
            int i = 0;
            for (i = 0; (i < caNo.length) && (Character.isDigit(caNo[i])); i++) {
                //
            }
            saMccMnc[1] = strNO.substring(3, 3 + i);
        } else {
//            LogHelper.logBamai("get mnc_sid mcc failed. NetworkOperator is " + strNO);
        }

        int iMcc = 0;
        try {
            iMcc = Integer.parseInt(saMccMnc[0]);
        } catch (Exception e) {
            iMcc = 0;
        }
        if (iMcc == 0) {
            saMccMnc[0] = "0";
        }
        if (!saMccMnc[0].equals("0") && !saMccMnc[1].equals("0")) {
            saCacheMccMnc = saMccMnc;
        } else if (saMccMnc[0].equals("0") && saMccMnc[1].equals("0")) {
            if (saCacheMccMnc != null) {
                saMccMnc = saCacheMccMnc;
                LogHelper.logFile("try to fix mnc_sid mcc");
            }
        }

        return saMccMnc;
    }

    /**
     * 将手机信号进行单位换算
     *
     * @param
     * @return int
     */
    public static int asu2Dbm(int iAsu) {
        return (-113 + 2 * iAsu);
    }


    /**
     * 判断系统NLP是否为谷歌提供
     *
     * @param
     * @return boolean
     */
    public static boolean isGoogleNlp(Context ctx) {
        if (ctx == null) {
            return false;
        }
        try {
            PackageManager pm = ctx.getPackageManager();
            String[] saBlackLst = new String[3];
            saBlackLst[0] = "com.baidu.map.location";
            saBlackLst[1] = "com.amap.android.location";
            saBlackLst[2] = "android.htc.china.location.service";
            PackageInfo pi = null;
            for (String str : saBlackLst) {
                try {
                    pi = pm.getPackageInfo(str, PackageManager.GET_SERVICES);
                } catch (PackageManager.NameNotFoundException e) {
                    //
                }
                if (pi != null) {
                    LogHelper.logFile("nlp provider: " + str);
                    return false;
                }
            }
            if (getSdk() >= 19) {
                pi = null;
                String[] saWhitelst = { "GmsCore.apk", "PrebuiltGmsCore.apk" };
                for (String str : saWhitelst) {
                    String strPath = "/system/priv-app/" + str;
                    int iFlag = PackageManager.GET_SERVICES;
                    pi = pm.getPackageArchiveInfo(strPath, iFlag);
                    if (pi == null) {
                        continue;
                    }

                    if ("com.google.android.gms".equals(pi.packageName)) {
                        LogHelper.logFile("nlp provider: " + pi.packageName);

                        return true;
                    }
                }
            } else {
                try {
                    int iFlag = PackageManager.GET_SERVICES;
                    pi = pm.getPackageInfo("com.google.android.location", iFlag);
                } catch (Exception e) {
                    LogHelper.logFile(e.toString());
                }
                if (pi != null) {
                    ServiceInfo[] sis = pi.services;
                    if (sis != null) {
                        String strPkgName = "com.google.android.location";
                        for (ServiceInfo si : sis) {
                            if (TextUtils.isEmpty(si.name)) {
                                continue;
                            }
                            if (si.name.startsWith(strPkgName)) {
                                LogHelper.logFile("nlp provider: " + si.name);
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            LogHelper.logFile(e.toString());
        }
        return false;
    }

    /**
     * 获取网络连接的类型
     * @param context
     * @return 1.wifi 2 mobile 0 无连接
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected()) {
                return mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI ? 1 : 2;
            }
        }
        return 0;
    }

    public  static boolean isMockLocation(Location location) {
        Object fromMock = ReflectUtils.invokeMethod(location, "isFromMockProvider");
        return fromMock != null && (boolean)fromMock;
    }

    public  static int getCoordinateType(Context context) {
        return mCoordinateType;
    }

    public   static String getPhonenum(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME_PHONE, Context.MODE_PRIVATE);
        if (null != settings) {
            return settings.getString(Constants.PREFS_NAME_PHONE, "");
        }
        return "";
    }

    public  static void setCoordinateType(int type) {
        mCoordinateType = type;
    }

    /**
     * 注意：此接口只在国际版应用中才使用。
     * 判断google play service在手机中是否已安装，并且版本不比要求的版本低。
     * @param context
     * @return 0 {@link com.google.android.gms.common.ConnectionResult#SUCCESS}:安装并且版本符合要求。
     *          否则可能返回{@link com.google.android.gms.common.ConnectionResult#SERVICE_VERSION_UPDATE_REQUIRED},
     *          {@link com.google.android.gms.common.ConnectionResult#SERVICE_MISSING},
     *          {@link com.google.android.gms.common.ConnectionResult#SERVICE_DISABLED}等
     */
    public static int isGooglePlayServicesAvailable(Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
    }

    /**
     * 将腾讯的定位相关状态转换成滴滴定位状态值。
     * @param statusType 原始腾讯状态名称，函数处理后会变成滴滴状态名称
     * @param status 状态值
     * @return 转换后的滴滴状态值
     */
    public static int convertTencentStatus(StringBuilder statusType, int status) {
        int didiStatus = -1;
        if (TencentLocationListener.CELL.equals(String.valueOf(statusType))) {
            statusType.delete(0, statusType.length());
            statusType.append(DefaultLocation.STATUS_CELL);
            switch (status) {
                case TencentLocationListener.STATUS_DISABLED:
                    didiStatus = DefaultLocation.STATUS_CELL_UNAVAILABLE;
                    break;
                case TencentLocationListener.STATUS_ENABLED:
                    didiStatus = DefaultLocation.STATUS_CELL_AVAILABLE;
                    break;
                case TencentLocationListener.STATUS_DENIED:
                    didiStatus = DefaultLocation.STATUS_CELL_DENIED;
                    break;
                default:
                    break;
            }
        } else if (TencentLocationListener.WIFI.equals(String.valueOf(statusType))) {
            statusType.delete(0, statusType.length());
            statusType.append(DefaultLocation.STATUS_WIFI);
            switch (status) {
                case TencentLocationListener.STATUS_DISABLED:
                    didiStatus = DefaultLocation.STATUS_WIFI_DISABLED;
                    break;
                case TencentLocationListener.STATUS_ENABLED:
                    didiStatus = DefaultLocation.STATUS_WIFI_ENABLED;
                    break;
                case TencentLocationListener.STATUS_DENIED:
                    didiStatus = DefaultLocation.STATUS_WIFI_DENIED;
                    break;
                case TencentLocationListener.STATUS_LOCATION_SWITCH_OFF:
                    didiStatus = DefaultLocation.STATUS_WIFI_LOCATION_SWITCH_OFF;
                    break;
                default:
                    break;
            }
        } else if (TencentLocationListener.GPS.equals(String.valueOf(statusType))) {
            statusType.delete(0, statusType.length());
            statusType.append(DefaultLocation.STATUS_GPS);
            switch (status) {
                case TencentLocationListener.STATUS_DISABLED:
                    didiStatus = DefaultLocation.STATUS_GPS_DISABLED;
                    break;
                case TencentLocationListener.STATUS_ENABLED:
                    didiStatus = DefaultLocation.STATUS_GPS_ENABLED;
                    break;
                case TencentLocationListener.STATUS_GPS_AVAILABLE:
                    didiStatus = DefaultLocation.STATUS_GPS_AVAILABLE;
                    break;
                case TencentLocationListener.STATUS_GPS_UNAVAILABLE:
                    didiStatus = DefaultLocation.STATUS_GPS_UNAVAILABLE;
                    break;
                default:
                    break;
            }
        }
        return didiStatus;
    }

    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        //fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=VZcVjW2vTDqgTdGzV-Yk0g
        String mockValue = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION);
        if (null != mockValue && mockValue.equals("0"))
            return false;
        else
            return true;
    }

    public  static int checkSystemPermission(Context context, String permission) {
        //修改小巴发现的系统层runtime exception
        try{
            return ContextCompat.checkSelfPermission(context, permission);
        } catch (Exception e) {
            return PackageManager.PERMISSION_DENIED;
        }
    }

    public static void setIsGpsMocked(boolean isGpsMocked) {
        mIsGpsMocked = isGpsMocked;
    }
    public static boolean isGpsMocked() {
        return mIsGpsMocked;
    }

    public static void setOnlyOSLocationAbroad(boolean onlyOSLocationAbroad) {
        LocationUtils.onlyOSLocationAbroad = onlyOSLocationAbroad;
    }
    public static boolean isOnlyOSLocationAbroad() {
        return onlyOSLocationAbroad;
    }
}
