package com.yc.tracesdk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.yc.tracesdk.LocInfoProtoBuf.WifiInfo;

public class WifiMonitor {
    private static volatile WifiMonitor mWifiMonitor;
    private Context mContext;
    /** {@code WifiManager}对象 */
    private WifiManager mWifiManager;
    /** 扫描间隔 */
    private final static long SCAN_INTERVAL = 1 * 60 * 1000;
    /** 临时采集间隔 */
    private final static long MAX_SCAN_INTERVAL = 10 * 1000;
    /** 临时采集持续时间 */
    private final static long MAX_COLLECT_DURATION = 10 * 60 * 1000;
    /** 允许扫描的最大速度，单位：米/秒 */
    private final static float MAX_SPEED = 10;
    /** 上次扫描的结果 */
    private ArrayList<MyWifiInfo> mLastScanResult;
    private long mScanTs;
    /** 接收到wifi广播的时间戳 **/
    private long mBroadcastTs;
    /** 保存wifi的时间戳 */
    private long mSaveWifiTs;
    /** 开始临时采集的时间戳 */
    private long mStartMaxTs;
    /** 是否开启定期扫描 */
    private boolean mEnableRegularScan = false;
    /** WifiReceiver是否已经注册 */
    private boolean mIsWifiReceiverRegistered = false;
    /** 是否处于临时采集中 */
    private boolean mIsInMaxCollect = false;

    private Handler mHandler;

    private WifiMonitor(Context context) {
        this.mContext = context.getApplicationContext();
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mLastScanResult = new ArrayList<MyWifiInfo>();
    }

    /**
     * 获取单例对象
     * 
     * @param context
     *            程序上下文对象
     * @return {@code WifiMonitor}单例对象
     */
    /* package */static WifiMonitor getInstance(Context context) {
        if (mWifiMonitor == null) {
            synchronized (WifiMonitor.class) {
                if (mWifiMonitor == null) {
                    mWifiMonitor = new WifiMonitor(context);
                }
            }
        }
        return mWifiMonitor;
    }

    /**
     * 开始监听wifi状态
     */
    /* package */void start() {
        LogHelper.log("WifiMonitor#start()");
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof HandlerThread)) {
            return;
        }
        mHandler = new Handler();
        GpsMonitor.getInstance(mContext).setLocListener(mLocListener); //#liuc# provide gpsmonitor a interface to call
        if (mEnableRegularScan) {
            mHandler.post(mRegularScanRunnable);
        }
        if (mWifiReceiver != null && mContext != null) {
        	mBroadcastTs = System.currentTimeMillis();
            try {
                mContext.registerReceiver(mWifiReceiver,
                        new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            } catch (SecurityException e) {}
        	mIsWifiReceiverRegistered = true;
        }

        //try {mWifiManager.startScan();}catch (SecurityException e) {e.printStackTrace();}
    }

    /**
     * 停止监听wifi状态
     */
    /* package */void stop() {
        LogHelper.log("WifiMonitor#stop()");
        if (mHandler != null) {
            mHandler.removeCallbacks(mRegularScanRunnable);
            mHandler.removeCallbacks(mForceScanRunnable);
        }
        GpsMonitor.getInstance(mContext).removeLocListener();
        if(mIsWifiReceiverRegistered && mContext != null) {
            try {
                mContext.unregisterReceiver(mWifiReceiver);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        	
        	mIsWifiReceiverRegistered = false;
            mIsInMaxCollect = false;
        }
    }
    
    /**
     * 设置是否开启定期扫描
     * 
     * @param enable 是否开启定期扫描
     */
    /* package */void setEnableRegularScan(boolean enable) { //#liuc# depend by TRACE LEVEL
        mEnableRegularScan = enable;
    }
    

    /**
     * 扫描wifi信息
     * @param forceSave 强制保存
     * 
     * @return {@code true}如果本次扫描结果进行了保存，{@code false}如果本次扫描结果丢弃
     */
    private boolean scanWifiInfo(boolean forceSave) { //#liuc# forceSave means do not compare wifiLists
        mScanTs = System.currentTimeMillis();
        
        /* 10%的概率记录ssid */
        boolean saveSSID = (int)(Math.random() * 10.0) == 0;
        mScanTs = mBroadcastTs; //#liuc# use wifi_broadcast received time to fill scanTs.

        // ==== get cur connected wifi mac
        String conn_bssid = null;
        try {
            conn_bssid = mWifiManager.getConnectionInfo().getBSSID();
        } catch (Exception e) {}
        if(conn_bssid == null) conn_bssid = "";

        ArrayList<MyWifiInfo> currentScanResult = new ArrayList<MyWifiInfo>();
        List<ScanResult> scanResults = null;
        try { scanResults = mWifiManager.getScanResults(); } catch (Exception e) {};
        if (scanResults == null || scanResults.isEmpty()) {
            return false;
        }

        for (ScanResult scanResult : scanResults) {
            MyWifiInfo myWifiInfo = new MyWifiInfo();
            if (saveSSID) {
                /* 剔除SSID中的单竖线 */
                String ssid = scanResult.SSID.replaceAll("\\|", "");
                /* 最多保留16个字符 */
                myWifiInfo.ssid = ssid.length() > 16 ? ssid.substring(0, 16) : ssid;
            } else {
                myWifiInfo.ssid = "";
            }
            
            myWifiInfo.bssid = scanResult.BSSID;
            myWifiInfo.level = scanResult.level;
            myWifiInfo.frequency = scanResult.frequency;
            myWifiInfo.isconnected = 0;
            if(myWifiInfo.bssid.equals(conn_bssid)) myWifiInfo.isconnected = 1;
            currentScanResult.add(myWifiInfo);
        }
        
        if (System.currentTimeMillis() - mSaveWifiTs >= 60000 || forceSave || isWifiChanged(mLastScanResult, currentScanResult)) {
            try {
                byte[] wifiInfoByteArray = convertWifiInfo2ByteArray(currentScanResult);
                DBHandler.getInstance(mContext).insertWifiData(wifiInfoByteArray);
            } catch (Exception e) {}
            
            /* 将json格式的数据写入文件 */
            JSONArray jsonArray = new JSONArray();
            for (MyWifiInfo info : currentScanResult) {
                jsonArray.put(info.toJson());
            }
            LogHelper.writeToFile(jsonArray.toString());

            mLastScanResult = currentScanResult;
            mSaveWifiTs = System.currentTimeMillis();
            return true;
        }

        return false;
    }
    
    /**
     * 判断扫描到的wifi是否发生了改变
     * 
     * @param lastWifiList
     *            上次扫描结果
     * @param currentWifiList
     *            本次扫描结果
     * @return {@code true}如果发生了改变，{@code false}如果没有改变
     */
    private boolean isWifiChanged(ArrayList<MyWifiInfo> lastWifiList, ArrayList<MyWifiInfo> currentWifiList) {
        if (lastWifiList == null || currentWifiList == null) {
            return false;
        }
        
        int diffSize = diff(currentWifiList, lastWifiList);
        if (diffSize >= 5 || diffSize > lastWifiList.size() * 0.5 || diffSize > currentWifiList.size() * 0.5) {
            return true;
        }
        return false;
    }

    /**
     * 计算两个列表的差异元素个数
     * 
     * @param list1
     *            列表1
     * @param list2
     *            列表2
     * @return 两个列表的差异元素个数
     */
    private int diff(List<MyWifiInfo> list1, List<MyWifiInfo> list2) {
        int diffSize = 0;

        List<String> l1 = new ArrayList();
        List<String> l2 = new ArrayList();
        for(int i = 0; i < list1.size(); i++) {
            l1.add(list1.get(i).bssid);
        }
        for(int i = 0; i < list2.size(); i++) {
            l2.add(list2.get(i).bssid);
        }

        Iterator<?> it = l1.iterator();
        while (it.hasNext()) {
            if (!l2.contains(it.next())) {
                diffSize++;
            }
        }

        return diffSize;
    }

    /**
     * 将wifi信息转换为字节数组
     * 
     * @param wifiList
     *            wifi信息对象
     * @return wifi信息的字节数组
     */
    private byte[] convertWifiInfo2ByteArray(ArrayList<MyWifiInfo> wifiList) {
        WifiInfo.Builder infoBuilder = new WifiInfo.Builder();
        infoBuilder.time(mScanTs);

        infoBuilder.wifi = new ArrayList<WifiInfo.Wifi>();
        for (MyWifiInfo wifiInfo : wifiList) {
            WifiInfo.Wifi.Builder builder = new WifiInfo.Wifi.Builder();
            builder.bssid(wifiInfo.bssid);
            builder.frequency(wifiInfo.frequency);
            builder.level(wifiInfo.level);
            builder.ssid(wifiInfo.ssid);
            builder.is_connected(wifiInfo.isconnected);
            infoBuilder.wifi.add(builder.build());
        }

        return infoBuilder.build().toByteArray();
    }

    /**
     * 开始临时采集
     * 临时采集覆盖普通wifi获取循环，频率更高
     * */
    void startMaxCollect() {

        if(mIsInMaxCollect || mHandler == null) return;

        mStartMaxTs = System.currentTimeMillis();
        mIsInMaxCollect = true;

        mHandler.removeCallbacks(mRegularScanRunnable);
        mHandler.post(mRegularScanRunnable);

    }

    /**
     * 停止临时采集
     * 临时采集停止时需判断是否灰度普通wifi获取循环
     */
    void stopMaxCollect() {

        if(!mIsInMaxCollect || mHandler == null) return;

        mIsInMaxCollect = false;
        mHandler.removeCallbacks(mRegularScanRunnable);
        if(mEnableRegularScan) {
            mHandler.postDelayed(mRegularScanRunnable, SCAN_INTERVAL);
        }
    }

    /** 每隔一段时间进行一次常规扫描的Runnable */
    private Runnable mRegularScanRunnable = new Runnable() {

        @Override
        public void run() {
            float speed = GpsMonitor.getInstance(mContext).getCurrentSpeed();
            if (speed <= MAX_SPEED) {
                scanWifiInfo(false);
            }

            // 临时采集关闭条件检测 (也许是这样，否则就只有上帝知道了）
            if(mIsInMaxCollect && System.currentTimeMillis() - mStartMaxTs >= MAX_COLLECT_DURATION) {
                mIsInMaxCollect = false;
            }

            // 临时采集触发时覆盖一般wifi获取循环
            if(mIsInMaxCollect) {
                LogHelper.log("============>MaxCollect scan runnable is Running.<=============");
                mHandler.postDelayed(mRegularScanRunnable, MAX_SCAN_INTERVAL);
            } else if (mEnableRegularScan) {
                LogHelper.log("============>Regular scan runnable is Running.<=============");
                mHandler.postDelayed(mRegularScanRunnable, SCAN_INTERVAL);
            }
        }
    };

    /** 由于位置改变，强制扫描wifi的Runnable */
    private Runnable mForceScanRunnable = new Runnable() {

        @Override
        public void run() {
            float speed = GpsMonitor.getInstance(mContext).getCurrentSpeed();
            if (speed <= MAX_SPEED) {

                // 减少司机端的强制采集 - 50%几率不采集且比较相似度
                //boolean isDriver = TraceManager.getInstance(mContext).isInDriverPackage();
                //if(isDriver) {
                    if((int)(Math.random()*10) >= 5) { // 新版中减少采集量
                        scanWifiInfo(false);
                    }
                //} else {
                //    scanWifiInfo(false);
                //}

                if (!mIsInMaxCollect && mEnableRegularScan) {
                    mHandler.removeCallbacks(mRegularScanRunnable);
                    mHandler.postDelayed(mRegularScanRunnable, SCAN_INTERVAL);
                }
            }
        }
    };

    /** 位置状态的监听 */
    private LocListener mLocListener = new LocListener() {

        @Override
        public void onLocationChanged() {
            mHandler.post(mForceScanRunnable);
        }
    };

    /** 接收Wifi更新的广播消息 **/
    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogHelper.log("#onReceive Wifi Broadcast");
			mBroadcastTs = System.currentTimeMillis();

            //try {mWifiManager.startScan();}catch (SecurityException e) {e.printStackTrace();}
            //try {mWifiManager.startScan();}catch (SecurityException e) {e.printStackTrace();}
		}
    };
    
    /* package */interface LocListener {
        public void onLocationChanged();
    }

    /**
     * wifi信息
     */
    private final class MyWifiInfo {
        public String ssid;
        public String bssid;
        public int level;
        public int frequency;
        public int isconnected;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MyWifiInfo:");
            sb.append("[ssid=").append(ssid).append("]");
            sb.append("[bssid=").append(bssid).append("]");
            sb.append("[level=").append(level).append("]");
            sb.append("[frequency=").append(frequency).append("]");
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MyWifiInfo && ((MyWifiInfo) o).bssid.equals(bssid)) {
                return true;
            }
            return false;
        }
        
        /**
         * 生成wifi信息的json对象
         * 
         * @return wifi信息的json对象
         */
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            try {
                json.put("ssid", ssid);
                json.put("bssid", bssid);
                json.put("level", level);
                json.put("frequency", frequency);
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            
            return json;
        }
    }
}
