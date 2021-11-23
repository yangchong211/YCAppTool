package com.yc.location.mode.wifi;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;

import com.yc.location.log.LogHelper;
import com.yc.location.utils.AppToolUtils;
import com.yc.location.utils.ReflectUtils;
import com.yc.location.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper of WifiManager , provides extra autonavi-related functions
 *
 * @author
 */
public class WifiManagerWrapper {

    private WifiManager mWifiManager;

    /**
     * 当前连接的wifi信息
     */
    private WifiInfo wiAccess = null;

    /**
     * 存放wifi扫描结果
     */
    private List<ScanResult> lstSr = new ArrayList<>();

    private Context mContext;

    public WifiManagerWrapper(Context ctx, WifiManager nativeWifiManager) {
        mWifiManager = nativeWifiManager;
        mContext = ctx;
    }

    /**
     * Return the results of the latest access point scan.
     *
     * @return the list of access points found in the most recent scan.
     */
    public List<ScanResult> getScanResults() {

        if (null != mWifiManager)
            return mWifiManager.getScanResults();
        return null;

    }

    /**
     * Return dynamic information about the current Wi-Fi connection, if any is active.
     *
     * @return the Wi-Fi information, contained in {@link WifiInfo}.
     */
    public WifiInfo getConnectionInfo() {
        if (null != mWifiManager)
            return mWifiManager.getConnectionInfo();
        return null;
    }

    public int getWifiState() {

        if (null != mWifiManager)
            return mWifiManager.getWifiState();
        return WifiManager.WIFI_STATE_UNKNOWN;

    }

    /**
     * Request a scan for access points. Returns immediately. The availability
     * of the results is made known later by means of an asynchronous event sent
     * on completion of the scan.
     *
     * @return {@code true} if the operation succeeded, i.e., the scan was initiated
     */
    public boolean startScan() {
        if (null != mWifiManager)
            return mWifiManager.startScan();

        return false;
    }

    /**
     * reflect method of WifiManager
     *
     * @return
     */
    public boolean startScanActive() {
        try {
            Object o = ReflectUtils.invokeMethod(mWifiManager, "startScanActive");
            boolean bSucc = String.valueOf(o).equals("true");
            if (bSucc) {
//                LogHelper.logBamai("wifi startScanActive work");
                return true;
            }
        } catch (Exception e) {
            //LogHelper.logBamai("wifi startScanActive does not work");
        }

        return false;
    }

    /**
     * 检测是否WIFI有效接入，replacement of wifiAccess() in APS
     *
     * @param cm
     * @return boolean
     */
    public boolean wifiAccess(ConnectivityManager cm) {
        WifiManager managerRef = mWifiManager;

        if (managerRef == null) {
            return false;
        }
        boolean bAccess = false;
        if (wifiEnabled()) {
            NetworkInfo ni = null;
            try {
                ni = cm.getActiveNetworkInfo();
                if (LocationUtils.getNetT(ni) == ConnectivityManager.TYPE_WIFI) {
                    if (wifiUseful(managerRef.getConnectionInfo())) {
                        bAccess = true;
                    }
                }
            } catch (Exception e) {
                //
            }
        }
        return bAccess;
    }

    /**
     * 判断WIFI是否启用, replacement of wifiEnabled() in APS
     *
     * @param
     * @return boolean
     */
    public boolean wifiEnabled() {
        WifiManager managerRef = mWifiManager;

        if (managerRef == null) {
            return false;
        }
        boolean bWifiEnabled = false;
        try {
            bWifiEnabled = managerRef.isWifiEnabled();
        } catch (Exception e) {
            //
        }
        if (!bWifiEnabled && AppToolUtils.getSdk() > 17) {
      /*
       * 判断是否开启了WIFI持续扫描
       */
            try {
                Object o = ReflectUtils.invokeMethod(managerRef, "isScanAlwaysAvailable");
                bWifiEnabled = String.valueOf(o).equals("true");

                if (bWifiEnabled) {
//                    LogHelper.logBamai("api:" + Utils.getSdk() + " always scan");
                }

            } catch (Exception e) {
                LogHelper.logFile(e.toString());
            }
        }
        return bWifiEnabled;
    }

    /**
     * 启用WIFI始终扫描
     *
     * @param bPermWriteSettings wether caller app has android.Manifest.permission.WRITE_SETTINGS
     * @return void
     */
    public void enableWifiAlwaysScan(boolean bPermWriteSettings) {

        Context ctx = mContext;

        if (mWifiManager == null || ctx == null || !bPermWriteSettings) {
            return;
        }
        if (AppToolUtils.getSdk() <= 17) {
            return;
        }

        ContentResolver cr = ctx.getContentResolver();
        String strGlobal = "android.provider.Settings$Global";
        Object obj = null;
        Object[] oa = new Object[2];
        oa[0] = cr;
        oa[1] = "wifi_scan_always_enabled";
        Class<?>[] ca = new Class[2];
        ca[0] = ContentResolver.class;
        ca[1] = String.class;
        try {
            obj = ReflectUtils.invokeStaticMethod(strGlobal, "getInt", oa, ca);
            int iStat = ((Integer) obj).intValue();
            if (iStat == 0) {
                oa = new Object[3];
                oa[0] = cr;
                oa[1] = "wifi_scan_always_enabled";
                oa[2] = 1;
                ca = new Class[3];
                ca[0] = ContentResolver.class;
                ca[1] = String.class;
                ca[2] = int.class;
                ReflectUtils.invokeStaticMethod(strGlobal, "putInt", oa, ca);
            }

            LogHelper.logFile("wifi| always scan :" + iStat);

        } catch (Exception e) {
            LogHelper.logFile("wifi| always scan Exception :" + e.toString());
        }
        oa = null;
        ca = null;
        obj = null;
    }

    /**
     * 判断WIFI是否有用
     *
     * @param
     * @return boolean
     */
    private boolean wifiUseful(final WifiInfo wi) {
        boolean bUseful = true;
        if (wi == null || TextUtils.isEmpty(wi.getBSSID())) {
            bUseful = false;
        } else if (wi.getSSID() == null) {
            bUseful = false;
        } else if (wi.getBSSID().equals("00:00:00:00:00:00")) {
            bUseful = false;
        } else if (wi.getBSSID().contains(" :")) {
            bUseful = false;
        } else if (TextUtils.isEmpty(wi.getSSID())) {
            bUseful = false;
        }
        return bUseful;
    }

    /**
     * 强制刷新WIFI
     *
     * @param
     * @return void
     */
    public void updateWifi() {
        if (!wifiEnabled()) {
            return;
        }

        boolean bSucc = false;
        try {
            bSucc = startScanActive();
            if (bSucc) {
                LogHelper.logFile("start wifi active scan success");
//                wifiScanTimestamp = System.currentTimeMillis();
            }
        } catch (Exception e) {
            LogHelper.logFile("start wifi active scan failed");
        }

        try {
            if (!bSucc && startScan()) {
//                LogHelper.logBamai("start wifi scan");

//                wifiScanTimestamp = Utils.getTimeBoot();
            }
        } catch (Exception e) {
            LogHelper.logFile("start wifi scan failed");
        }
    }

    public void setWifiResultReceiver(BroadcastReceiver receiver, Handler workHandler, String... actions) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        for (String action : actions) {
            intentFilter.addAction(action);
        }

        try {
            if (null != workHandler) {
                mContext.registerReceiver(receiver, intentFilter, null, workHandler);
            } else {
                mContext.registerReceiver(receiver, intentFilter);
            }
        } catch (SecurityException e) {
            LogHelper.logFile("initWifiListeners exception, " + e.getMessage());
        }
    }

}
