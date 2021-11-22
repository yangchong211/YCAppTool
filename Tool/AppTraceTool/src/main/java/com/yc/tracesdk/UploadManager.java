package com.yc.tracesdk;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yc.tracesdk.DBHandler.DataWraper;
import com.yc.tracesdk.net.HttpPostThread;
import com.yc.tracesdk.net.Param;
import com.yc.tracesdk.net.ResponseListener;

public class UploadManager {
    /** 距离上次上传失败的最小时间间隔，防止上传失败时的频繁重试 */
    private final static long UPLOAD_FAIL_INTERVAL = 1 * 60 * 60 * 1000;
    /** 每次上传的最大数据条数 */
    private final static int[] UPLOAD_SIZE_MAXS = {3000, 1500, 750, 300, 100};
    /** 距离最早写入的时间超过1天时，激活上传 */
    final static int FORCEUPLOAD_EARLIEST_TIME = 1 * 24 * 60 * 60 * 1000;
    
    private static UploadManager mInstance;
    private Context mContext;

    private UploadTask mUploadTask;

    /** 上传成功的计数，用于识别连续的成功状态 */
    private int uploadSuccessCounts = 0;
    /** 上传数量的级别，0-4，数字越小表示级别越高 */
    private int uploadSizeLevel = 0;
    private long mUploadInterval = FORCEUPLOAD_EARLIEST_TIME;

    private UploadManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /* package */ static UploadManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UploadManager.class) {
                if (mInstance == null) {
                    mInstance = new UploadManager(context);
                }
            }
        }
        return mInstance;
    }

    void setUploadInterval(long interval) {
        mUploadInterval = interval;
    }
    
    /**
     * 当前是否满足上传条件
     * 
     * @return {@code true}如果满足上传条件，{@code false}如果不满足上传条件
     */
    /* package */boolean canUpload() {
        TraceManager traceManager = TraceManager.getInstance(mContext);
        long dbSize = DBHandler.getInstance(mContext).getDBSize();
        long interval = System.currentTimeMillis() - traceManager.getLastUploadFailTs();
        long ealiestInsertTime_interval = System.currentTimeMillis() - traceManager.getEarliestInsertTime();
        if ((dbSize >= traceManager.getDataCountThreshold()
                || ealiestInsertTime_interval >= mUploadInterval)
                && interval >= UPLOAD_FAIL_INTERVAL) {
            return true;
        }
        return false;
    }
    
    /**
     * 检查是否可以上传
     */
    /* package */void checkUpload() {
        if (canUpload()) {
            uploadOnce();
        }
    }

    /**
     * 尝试进行一次数据上传操作
     */
    /* package */ void uploadOnce() {
        if (Config.TEST && isWifiConnected()) {
            return;
        }
        if (mUploadTask == null || !mUploadTask.isUploading()) {
            mUploadTask = new UploadTask();
            LogHelper.log("-uploadOnce-");
            mUploadTask.start();
        }
    }
    
    /**
     * wifi是否已连接
     * 
     * @return {@code true}如果wifi已连接，{@code false}如果wifi没有连接
     */
    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);   
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 去除最早写入时间的配置
     */
    private void clearEaliestInsertTime() {
        TraceManager.getInstance(mContext).setEarliestInsertTime(0);
    }

    /**
     * 上传数据的线程
     */
    private final class UploadTask extends Thread {
        private volatile boolean mIsUploading = false;

        @Override
        public void run() {
            mIsUploading = true;
            LogHelper.log("-UploadTask-run-");
            uploadUntilEmptyOrFail();
        }

        /**
         * 获取当前线程运行状态
         * 
         * @return {@code true}如果当前线程正在运行，{@code false}如果当前线程没有在运行
         */
        public boolean isUploading() {
            return mIsUploading;
        }

        /**
         * 上传全部，如果出现上传失败的情况停止上传
         */
        private void uploadUntilEmptyOrFail() {
            final DBHandler dbHandler = DBHandler.getInstance(mContext);
            final ArrayList<DataWraper> dataList = dbHandler.popData(UPLOAD_SIZE_MAXS[uploadSizeLevel]);

            if (dataList.isEmpty()) {
                clearEaliestInsertTime();
                mIsUploading = false;
                return;
            }

            UploadResultListener uploadResultListener = new UploadResultListener() {

                @Override
                public void onSuccess() {
                    /* 上传连续成功两次，将上传量增倍 */
                    uploadSuccessCounts += 1;
                    if (uploadSuccessCounts >= 2) {
                        uploadSuccessCounts = 0;
                        uploadSizeLevel = uploadSizeLevel - 1 < 0 ? 0 : uploadSizeLevel - 1;
                    }

                    /* 记录历史上传成功数 */
                    String[] sucsfail = TraceManager.getInstance(mContext)
                            .getTotalSucsFailTimes().split("-");
                    long sucsNum = Long.parseLong(sucsfail[0]) + 1;
                    TraceManager.getInstance(mContext).setTotalSucsFailTimes(
                            sucsNum, Long.parseLong(sucsfail[1]));

                    uploadUntilEmptyOrFail();
                }

                @Override
                public void onFail(String reason) {

                    /* 上传失败一次，将上传量减半 */
                    uploadSuccessCounts = 0;
                    uploadSizeLevel = uploadSizeLevel + 1 > 4 ? 4 : uploadSizeLevel + 1;

                    /* 记录历史上传失败数 */
                    String[] sucsfail = TraceManager.getInstance(mContext)
                            .getTotalSucsFailTimes().split("-");
                    long failNum = Long.parseLong(sucsfail[1]) + 1;
                    TraceManager.getInstance(mContext).setTotalSucsFailTimes(
                            Long.parseLong(sucsfail[0]), failNum);

                    /* 上传失败，将数据重新插入数据库 */
                    dbHandler.insertData(dataList);
                    mIsUploading = false;
                    TraceManager.getInstance(mContext).setLastUploadFailTs(System.currentTimeMillis());

                    //上传失败埋点
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("sdk_ver",BuildConfig.VERSION_NAME);
                    map.put("etl_num",String.valueOf(dataList.size()));
                    map.put("reason",reason);
                }
            };

            upload(uploadResultListener, dataList);
        }

        private void upload(final UploadResultListener uploadResultListener, ArrayList<DataWraper> dataList) {
            DBHandler dbHandler = DBHandler.getInstance(mContext);
            byte[] data = dbHandler.convertDataList2ByteArray(dataList);
            LogHelper.log("data.length[before zip]:" + data.length);
            data = getGZipCompressed(data);
            
            if (data == null) {
                mIsUploading = false;
                return;
            }
            
            LogHelper.log("data.length[after zip]:" + data.length);

            TraceManager traceManager = TraceManager.getInstance(mContext);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Param param = new Param();
            param.mUrl = Config.URL;
            param.mPostMap.put("os_type", "android");
            param.mPostMap.put("uid", traceManager.getUID());
            param.mPostMap.put("imei", traceManager.getIMEI());
            param.mPostMap.put("imsi", traceManager.getIMSI());

            if(Config.DEBUG) {param.mPostMap.put("package_name", "com.ddtaxi.common.tracesdk");}
            else {param.mPostMap.put("package_name", traceManager.getPakcageName());}

            param.mPostMap.put("system_version", traceManager.getSystemVerion() + "");
            param.mPostMap.put("version_name", traceManager.getVersionName());
            param.mPostMap.put("version_code", traceManager.getVersionCode() + "");
            param.mPostMap.put("device_time", simpleDateFormat.format(System.currentTimeMillis()));
            param.mPostMap.put("sucs_fail_times", traceManager.getTotalSucsFailTimes());
            param.mPostMap.put("hardware_version", traceManager.getHardwareVersion());
            param.mPostMap.put("rom_version", traceManager.getRomVersion());
            param.mPostData.put("__trace_log", data);

            ResponseListener responseListener = new ResponseListener() {

                @Override
                public void onReceiveResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (uploadResultListener != null) {
                            if (json.optInt("errno", -1) == 0) {
                                uploadResultListener.onSuccess();
                            } else {
                                uploadResultListener.onFail("status error:" + response);
                            }
                        }
                    } catch (JSONException e) {
                        //e.printStackTrace();
                        uploadResultListener.onFail("exception parse json:" + response);
                    }

                }

                @Override
                public void onReceiveError(int code) {
                    uploadResultListener.onFail("errcode:" + code);
                }

            };

            HttpPostThread httpPost = new HttpPostThread(responseListener, param);
            httpPost.start();
        }

    }

    /** 使用gzip方式压缩数组 */
    public static byte[] gZip(byte[] data) {
        byte[] result = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            result = bos.toByteArray();
            bos.close();
        } catch (Throwable ex) {
            //ex.printStackTrace();
        }
        return result;
    }
    
    /** 使用zip方式压缩数组 */
    public static byte[] zip(byte[] data) {
        byte[] result = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(data.length);
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
            zip.close();
            result = bos.toByteArray();
            bos.close();
        } catch (Throwable ex) {
            //ex.printStackTrace();
        }
        return result;
    }
    
    public static byte[] getGZipCompressed(byte[] byteData) {  
        byte[] compressed = null;  
        try {  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(  
                    byteData.length);  
            Deflater compressor = new Deflater();  
            compressor.setLevel(Deflater.BEST_COMPRESSION); 
            compressor.setInput(byteData, 0, byteData.length);  
            compressor.finish(); 
  
            final byte[] buf = new byte[1024];  
            while (!compressor.finished()) {  
                int count = compressor.deflate(buf);  
                bos.write(buf, 0, count);  
            }  
            compressor.end(); 
            compressed = bos.toByteArray();  
            bos.close();
        } catch (Throwable e) {//fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=H--I-HEkSZm8Oydz01nc5w
            //e.printStackTrace();
        }  
        return compressed;  
    }  

    /* package */interface UploadResultListener {
        public void onSuccess();

        public void onFail(String reason);
    }
}
