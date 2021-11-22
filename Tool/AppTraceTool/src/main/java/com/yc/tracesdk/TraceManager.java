package com.yc.tracesdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class TraceManager {
    /** 默认的数据条数阈值，达到这个值时可以进行上传操作 */
    final static long DATA_COUNT_THRESHOLD_DEFAULT = 1000;
    /** 集成应用的包名汇总 */
    private final static String PACKAGENAME_DRIVER_TAXI = "com.sdu.didi.gui";
    private final static String PACKAGENAME_DRIVER_GS = "com.sdu.didi.gsui";
    private final static String PACKAGENAME_PASSENGER = "com.sdu.didi.psnger";
    
    private static volatile TraceManager mInstance;
    private Context mContext;
    /** 采集服务是否正在运行 */
    private volatile boolean mIsRunning = false;
    /** 从第三方定位服务获取数据的接口对象 */
    private ExtraLocService mExtraLocService;
    private final static String PREFERENCE_NAME = "trace_sdk_pref";
    /** 数据采集的等级，高（等级越高，信息越详细） */
    public final static int LEVEL_HIGH = 1;
    /** 数据采集等级，低 */
    public final static int LEVEL_LOW = 2;
    /** 数据采集等级 */
    private int mLevel = LEVEL_LOW;
    /** 数据条数阈值，达到这个值时可以进行上传操作 */
    private long mDataCountThreshold = DATA_COUNT_THRESHOLD_DEFAULT;

    private int versionCode = -1;

    //private String packageName = "";

    private TraceManager(Context context) {
        /* 使用 Application context */
        this.mContext = context.getApplicationContext();
    }

    /**
     * 获取单例对象
     * 
     * @param context
     *            应用上下文对象
     * @return {@code LCManager}的单例对象
     */
    public static TraceManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (TraceManager.class) {
                mInstance = new TraceManager(context);
            }
        }
        return mInstance;
    }
    
    /**
     * 采集服务是否正在运行
     * @return 
     */
    public boolean  isRunning() {
        return mIsRunning;
    }

    /**
     * 开始采集
     */
    public void startTrace() {

        synchronized (this) {

        /* api level 9+ */
            if (android.os.Build.VERSION.SDK_INT < 9) {
                return;
            }
            if (mIsRunning) {
                return;
            }

            Intent intent = new Intent(mContext, TraceService.class);
            intent.putExtra(TraceService.CMD_ACTION, TraceService.CMD_START);
            try {
                mContext.startService(intent);
                mIsRunning = true;
            } catch (Exception e) {
            }
            UploadManager.getInstance(mContext).setUploadInterval(mDataCountThreshold);

        }
    }

    /**
     * 停止采集
     */
    public void stopTrace() {

        synchronized (this) {

        /* api level 9+ */
            if (android.os.Build.VERSION.SDK_INT < 9) {
                return;
            }
            if (!mIsRunning) {
                return;
            }

            Intent intent = new Intent(mContext, TraceService.class);
            intent.putExtra(TraceService.CMD_ACTION, TraceService.CMD_STOP);
            try {
                mContext.startService(intent);
                mIsRunning = false;
            } catch (Exception e) {
            }
        }
    }

    /** 启动临时高频采集 */
    public void startMaxCollect() {
        if(!mIsRunning) return;
        WifiMonitor.getInstance(mContext).startMaxCollect();
    }

    /** 停止临时高频采集 */
    public void stopMaxCollect() {
        if(!mIsRunning) return;
        WifiMonitor.getInstance(mContext).stopMaxCollect();
    }

    /** 获取采集SDK版本号 */
    public String getVersion() {
        return BuildConfig.VERSION_NAME;
    }
    public String getBuildVersion() {
        return BuildConfig.BUILD_VERSION;
    }

    /**
     * 设置用户id，用于问题定位等
     *
     * @param id
     *            用户id
     */
    public void setUID(String id) {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = pref.edit();
        editor.putString("id", id == null ? "" : id);
        editor.commit();
    }

    /**
     * 获取用户id
     * 
     * @return 用户id，可能为空字符串，但不为{@code null}
     */
    public String getUID() {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        return pref.getString("id", "");
    }
    
    /**
     * 设置数据采集的等级，等级越高，信息越详细
     * 
     * @param level 数据采集的等级
     */
    public void setLevel(int level) {
        mLevel = level;
    }
    
    /**
     * 获取当前的数据采集等级
     * 
     * @return 当前的数据采集等级
     */
    public int getLevel() {
        return mLevel;
    }
    
    /**
     * 设置阈值，数据条数达到阈值时进行上传
     * 
     * @param size 数据条数
     */
    public void setDataCountThreshold(int size) {
        mDataCountThreshold = size;
    }
    
    /**
     * 获取当前的数据上传的阈值
     * 
     * @return 当前的数据上传的阈值
     */
    public long getDataCountThreshold() {
        return mDataCountThreshold;
    }

    //public void setPackageName(String packageName) { this.packageName = packageName;}

    /**
     * 保存上次上传失败的时间戳
     * 
     * @param ts 上传失败的时间戳
     */
    /* package */void setLastUploadFailTs(long ts) {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = pref.edit();
        editor.putLong("last_upload_fail_ts", ts);
        editor.commit();
    }
    
    /**
     * 获取上传失败的时间戳
     * 
     * @return 上传失败的时间戳
     */
    /* package */long getLastUploadFailTs() {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        return pref.getLong("last_upload_fail_ts", 0);
    }
    
    /**
     * 得到手机的IMEI号
     * 
     * @return 手机的imei号，可能为空字符串，但不为{@code null}
     */
    /* package */String getIMEI() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        try {
            imei = mTelephonyMgr.getDeviceId();
        } catch (SecurityException e) {}
        return imei == null ? "" : imei;
    }

    /**
     * 得到手机的IMSI号
     * 
     * @return 手机的imsi号，可能为空字符串，但不为{@code null}
     */
    /* package */String getIMSI() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi;
        try {
            imsi = mTelephonyMgr.getSubscriberId();
        } catch (SecurityException e) {
            imsi = "";
        }
        return imsi == null ? "" : imsi;
    }

    /**
     * 获取手机号
     * 
     * @return 手机号，可能为空字符串，但不为{@code null}
     */
    /* package */String getMobilePhone() {
        TelephonyManager telephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyMgr != null) {
            String tel = telephonyMgr.getLine1Number();
            if (!TextUtils.isEmpty(tel) && tel.startsWith("+86")) {
                tel = tel.replace("+86", "");
            }
            return tel; // #liuc# 'tel' may be null
        } else {
            return "";
        }
    }

    /**
     * 获取包名
     * 
     * @return 当前应用的包名
     */
    /* package */String getPakcageName() {
        //if(packageName != null && packageName.length() >= 0) return packageName;
        return mContext.getPackageName();
    }

    /**
     * 获取 version code
     * 
     * @return version code
     */
    /* package */int getVersionCode() {
        if (versionCode != -1) return versionCode;
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(getPakcageName(), 0);
            if (pi != null) {
                versionCode = pi.versionCode;
            }
        } catch (NameNotFoundException e) {
            //e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取 version name
     * 
     * @return version name
     */
    /* package */String getVersionName() {
        //String versionName = "";
        //try {
        //    PackageInfo pi = mContext.getPackageManager().getPackageInfo(getPakcageName(), 0);
        //    if (pi != null) {
        //        versionName = pi.versionName;
        //    }
        //} catch (NameNotFoundException e) {
        //    e.printStackTrace();
        //}
        //return versionName;
        return getVersion();
    }

    /**
     * 获取系统版本号
     * 
     * @return 系统版本号
     */
    /* package */int getSystemVerion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取硬件版本号，设备厂商对于手机硬件的描述信息
     * @return 硬件版本号
     */
    String getHardwareVersion() {

        String fingerprint = Build.FINGERPRINT;
        String model = Build.MODEL;
        return fingerprint.contains(model) ? fingerprint : model + fingerprint;
    }

    /**
     * 获取ROM版本号，设备厂商提供的安卓ROM版本信息
     * @return ROM版本号
     */
    String getRomVersion() {
        return Build.VERSION.RELEASE + "/" + Build.VERSION.SDK_INT;
    }

    /**
     * 判断集成应用是否是司机端
     * @return 当前是否集成至司机端
     */
    boolean isInDriverPackage() {
        if(mContext == null) return false;
        return mContext.getPackageName().equals(PACKAGENAME_DRIVER_GS) || mContext.getPackageName().equals(PACKAGENAME_DRIVER_TAXI);
    }

    /**
     * 获取·本地最早写入时间
     * 本地最早写入时间用于决定回传时机，大于7天的强制上传
     * 存放于SharedPreference中
     *
     * @return 本地最早写入时间
     */
    long getEarliestInsertTime() {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        return pref.getLong("earliest_insert_time", 0);
    }
    /**
     * 设置·本地最早写入时间
     * 本地最早写入时间用于决定回传时机，大于7天强制上传
     * 存放于SharedPreference中
     *
     * @param eiTime
     *              将最早写入时间配置到SharedPreference中，timestamp
     */
    void setEarliestInsertTime(long eiTime) {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = pref.edit();
        editor.putLong("earliest_insert_time", eiTime);
        editor.commit();
    }
    /**
     * 获取·历史累计的上传成功与失败的次数
     *
     * @return 历史累计的成功与失败的次数
     */
    String getTotalSucsFailTimes() {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        return pref.getString("total_sucs_fail_times", "0-0");
    }
    /**
     * 设置·历史累计的上传成功与失败的次数
     *
     * @param sucsTimes
     *              上传成功的次数
     * @param failTimes
     *              上传失败的次数
     */
    void setTotalSucsFailTimes(long sucsTimes, long failTimes) {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = pref.edit();
        editor.putString("total_sucs_fail_times",
                String.valueOf(sucsTimes) + "-" + String.valueOf(failTimes));
        editor.commit();
    }

    /******************************** 下面内容暂时没有用到 ********************************/

    /**
     * 判断是否有第三方的定位服务
     * 
     * @return {@code true}如果有第三方的定位服务，{@code false}如果没有第三方的定位服务
     */
    public boolean hasExtraLocService() {
        return mExtraLocService != null;
    }

    /**
     * 设置第三方的定位服务
     * 
     * @param extraLocService
     *            从第三方定位服务获取数据的接口
     */
    public void setExtraLocService(ExtraLocService extraLocService) {
        this.mExtraLocService = extraLocService;
    }

    /**
     * 获取从第三方定位服务获取数据的接口
     * 
     * @return 从第三方定位服务获取数据的接口
     */
    public ExtraLocService getExtraLocService() {
        return mExtraLocService;
    }

    /**
     * 从第三方定位服务获取数据的接口
     */
    public interface ExtraLocService {
        public Location getLocation();
    }

}
