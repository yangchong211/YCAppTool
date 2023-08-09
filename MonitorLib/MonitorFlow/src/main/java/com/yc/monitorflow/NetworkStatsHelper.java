package com.yc.monitorflow;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.M)
public class NetworkStatsHelper {

    private final NetworkStatsManager networkStatsManager;
    private int packageUid;

    public NetworkStatsHelper(Context context) {
        String packageName = context.getPackageName();
        networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("WrongConstant") ApplicationInfo ai =
                    pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
            packageUid = ai.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void requestReadNetworkStats(Context context) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 经过测试，只有在 Android 10 及以上加包名才有效果
            // 如果在 Android 10 以下加包名会导致无法跳转
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        context.startActivity(intent);
    }

    protected boolean hasPermissionToReadNetworkStats(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i("流量-", "==当前版本小于23==");
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        requestReadNetworkStats(context);
        return false;
    }


    /**
     * 获取 所有移动使用流量信息
     *
     * @param context       上下文
     * @param isDayAndMonth 是否是当天还是当月
     * @return 返回 当天 还是当月的流量信息
     */
    public TrafficBean getAllDayMonthMobileInfo(Context context, boolean isDayAndMonth) {
        TrafficBean trafficBean = new TrafficBean();
        NetworkStats.Bucket bucket;
        try {
            //查询网络使用统计摘要。结果是整个设备的汇总数据使用情况。结果是随着时间、状态、uid、标签、计量和漫游聚合的单个存储桶
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    isDayAndMonth ? getTimesMorning() : getTimesMonthMorning(),
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return trafficBean;
        }
        trafficBean.setTotalRxKB(bucket.getRxBytes());
        trafficBean.setTotalTxKB(bucket.getTxBytes());
        trafficBean.setTotalKB(bucket.getTxBytes() + bucket.getRxBytes());
        return trafficBean;
    }

    /**
     * 获取所有应用一天使用的移动流量信息
     *
     * @param context   上下文
     * @param startTime 开始时间
     * @return 流量信息
     */
    public TrafficBean getOneDayMobileInfo(Context context, long startTime) {
        TrafficBean trafficBean = new TrafficBean();
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startTime,
                    startTime + 86400000
            );
            trafficBean.setTotalRxKB(bucket.getRxBytes());
            trafficBean.setTotalTxKB(bucket.getTxBytes());
            trafficBean.setTotalKB(bucket.getTxBytes() + bucket.getRxBytes());
        } catch (RemoteException e) {
            return trafficBean;

        }
        return trafficBean;
    }

    /**
     * 获取今日 或者今月的 应用 的实时流量使用情况
     *
     * @param context       上下文
     * @param isDayAndMonth 是否是今天还是今月
     * @return 获取今日 或者今月的流量使用情况
     */
    public TrafficBean getSummaryTrafficMobile(Context context, boolean isDayAndMonth) {
        TrafficBean trafficBean = new TrafficBean();
        NetworkStats networkStats = null;
        try {
            //查询网络使用统计摘要。（查询多个应用流量统计） 会返回多个桶 需要循环遍历 在根据uid 的到每一个的实时流量
            networkStats = networkStatsManager.querySummary(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    isDayAndMonth ? getTimesMorning() : getTimesMonthMorning(),
                    System.currentTimeMillis());
            long mobileTraffic = 0;//
            long mobileRx = 0;
            long mobileTx = 0;
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            do {
                networkStats.getNextBucket(bucket);
                int summaryUid = bucket.getUid();
                if (packageUid == summaryUid) {
                    mobileTx += bucket.getTxBytes();
                    mobileRx += bucket.getRxBytes();
                }
            } while (networkStats.hasNextBucket());
            mobileTraffic = mobileRx + mobileTx;

            trafficBean.setTotalRxKB(mobileTx);
            trafficBean.setTotalTxKB(mobileRx);
            trafficBean.setTotalKB(mobileTraffic);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            if (networkStats != null) {
                networkStats.close();
            }
        }
        return trafficBean;
    }

    /**
     * 获取用户id android 10 以后获取不了 传null 即可
     * 需要权限
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @param context     上下文
     * @param networkType 网络类型
     * @return null
     */
    @SuppressLint("MissingPermission")
    private String getSubscriberId(Context context, int networkType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return null;
        }
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return null;
    }

    /**
     * 获取当天的零点时间
     */
    private long getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis());
    }

    /**
     * 获得本月第一天0点时间
     */
    private long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTimeInMillis();
    }
}
