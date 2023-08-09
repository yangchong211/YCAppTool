package com.yc.monitorflow;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import java.util.Date;

/**
 * 系统香瓜的API
 */
public final class FlowHelper {

    private static final String TAG = FlowHelper.class.getSimpleName();
    //volatile防止指令重排序，内存可见(缓存中的变化及时刷到主存，并且其他的线程副本内存失效，必须从主存获取)，但不保证原子性
    private static volatile FlowHelper singleton = null;
    //是否开启
    private boolean isOpen = true;
    private Context context;

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static FlowHelper getInstance() {
        if (singleton == null) {
            synchronized (FlowHelper.class) {
                if (singleton == null) {
                    singleton = new FlowHelper();
                }
            }
        }
        return singleton;
    }

    private FlowHelper() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 是否开启检测
     *
     * @return 结果
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 设置是否要检测
     *
     * @param open 是否检测
     */
    public FlowHelper setOpen(boolean open) {
        isOpen = open;
        return singleton;
    }

    /**
     * 初始化
     *
     * @param application 应用
     */
    public FlowHelper start(Application application) {
        setOpen(isOpen);
        return singleton;
    }

    /**
     * 主动获取一次
     */
    public void get() {
        long beginTime = System.currentTimeMillis();
        TrafficBean flowTotal = getFlow();
        TrafficBean flowApp = getAppFlow();
        long endTime = System.currentTimeMillis();
    }

    /**
     * 获取系统总体流量情况
     *
     * @return 流量
     */
    public TrafficBean getFlow() {
        TrafficBean flow = new TrafficBean(0, 0, 0);
        //开机时间
        flow.setBootTime(SystemClock.elapsedRealtime() / 1000);
        //总的下行流量
        flow.setTotalRxKB(TrafficStatsHelper.getAllRxBytes() / 1024);
        //总的上行流量
        flow.setTotalTxKB(TrafficStatsHelper.getAllTxBytes() / 1024);
        return flow;
    }


    /**
     * 获取应用流量情况
     *
     * @return 流量
     */
    public TrafficBean getAppFlow() {
        TrafficBean flow = new TrafficBean(0, 0, 0);
        //开机时间
        flow.setBootTime(SystemClock.elapsedRealtime() / 1000);
        //本应用下行流量
        flow.setTotalRxKB(TrafficStatsHelper.getPackageRxBytes(android.os.Process.myUid()) / 1024);
        //本应用上行流量
        flow.setTotalTxKB(TrafficStatsHelper.getPackageTxBytes(android.os.Process.myUid()) / 1024);
        return flow;
    }

    public TrafficBean getAllDayMonthMobileInfo() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(context);
            boolean hasPermission = networkStatsHelper.hasPermissionToReadNetworkStats(context);
            if (hasPermission){
                return networkStatsHelper.getAllDayMonthMobileInfo(context, true);
            }
        }
        return null;
    }

    public TrafficBean getOneDayMobileInfo() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(context);
            boolean hasPermission = networkStatsHelper.hasPermissionToReadNetworkStats(context);
            if (hasPermission){
                return networkStatsHelper.getOneDayMobileInfo(context, new Date().getDate());
            }
        }
        return null;
    }

    public TrafficBean getSummaryTrafficMobile() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(context);
            boolean hasPermission = networkStatsHelper.hasPermissionToReadNetworkStats(context);
            if (hasPermission){
                return networkStatsHelper.getSummaryTrafficMobile(context, true);
            }
        }
        return null;
    }

}
