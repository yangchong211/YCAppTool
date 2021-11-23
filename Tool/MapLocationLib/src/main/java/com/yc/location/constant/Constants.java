package com.yc.location.constant;

import com.yc.location.log.LogHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Constants {

    public static final boolean isDebug = true;
    //考虑弱网情况下等待网络结果时间
    public static final int START2LOCATION_INTERVAL_MILLIS     = 5*1000;

    public static final int MESSAGE_WHAT_LACATION     = 0xDD000010;
    public static final int MESSAGE_WHAT_ERRINFO     = 0xDD000011;

    public static final int serviceCmdNone     = 0xDD000000;
    public static final int serviceCmdStart    = 0xDD000001;
    public static final int serviceCmdStop     = 0xDD000002;
    public static final int serviceCmdStartLocOnce     = 0xDD000003;
//    static final int serviceCmdStopLocOnce     = 0xDD000004;

    public static final int serciceDefMsg      = 0;
    public static final int wifiMaxCount       = 30;
    //protected static final int wifiSSIDMaxLen     = 16;
    //protected static final int httpThreadCount    = 1;
    public static final int gpslocAccuracyConf = 25;
    public static final int lonlatDots         = 6;
    public static final int confiprobDots      = 3;
    //protected static final int gpsHighSpeed       = 10;

    //protected static final long firstReqIntervalMillis      = 1000;
    //protected static final long regulReqIntervalMillis      = 10*1000;
    public static final long regulReqIntervalTurboMillis = 1000;
    public static final long cellDataExpiredMillis       = 60*60*1000;
    public static final long wifiScanIntervalMillis      = 15*1000;
    public static final long wifiScanCheckMillis         = 8*1000;
    public static final long wifiScanExpiredMillis       = 60*1000;
    public static final long wifiValidDeadlineMillis     = 5*60*1000;
    public static final long DEF_GPS_FLP_MONITOR_INTERVAL = 1000;
    /**
     * gps定位
     */
    public static final long gpsLocTimeIntevalMillis     = 30*1000;
    public static final long minQpsIntervalMillis        = 8*1000;
    public static final long tencentLocTimeIntevalMillis     = 10*1000;

    //protected static final long minDriverIntervalMillis     = 3*1000;
    //protected static final long apolloRefreshMillis         = 20*1000;
    public  static final long WORKER_LOOPER_CHECK_INTERVAl_MS = 60*10000;
    public  static final long GPS_CHECK_INTERVAL_MS = 120*1000;
    //GPS/NLP监听到位置打日志的最小时间间隔
    public  static final long MIN_INTERVAL_BAMAI_GPS_NLP_LOCATION = 15*1000;

    public static final float gpsUpdatesMindistMeter = 0f;

    public static final double cdmaLocCoef = 14400.0;
    public static final double gpslocConfiExtra = 0.5;
    public static final double transprobLowThreshold = 0.02;
    //protected static final double wifiChangeThreshold = 0.6;
    //protected static final double wifiChangeThresholdMax = 1.5;
    public static final double nlpLocConfi = 0.8;
    public static final double googleFlpLocConfi = 1.0f;

    public static final String serviceTag                        = "LocCenter";
    //protected static final String serviceCmdAction                  = "cmd_action";
    public static final String logFileName                       = "log.txt";
    public static final String logLRCF                           = "\n";
    public static final String userDefException                  = "UserDevException";
    public static final String paramCharset                      = "UTF-8";
    public static final String serverUrl                         = "https://map.diditaxi.com.cn/v1/location";
    //protected static final String serverUrl                         = "http://indoor.map.xiaojukeji.com/v1/location";
    //protected static final String serverUrlTest                     = "http://10.242.151.164:8080/v1/location";
    //protected static final String omegaEventId_FirstLocDuration     = "locs_first";
    //protected static final String omegaEventId_LocFailed            = "locs_fail";
    //protected static final String omegaEventId_LocTotalTimes        = "locs_totaltimes";

    public static final String joLeft = "{";
    public static final String joRight = "}";
    public static final String jsAssi = ":";
    public static final String jaLeft = "[";
    public static final String jaRight = "]";
    public static final String jsSepr = ",";
    public static final String js_req_user_timestamp = "\"timestamp\"";
    public static final String js_req_user_imei = "\"imei\"";
    public static final String js_req_user_app_id = "\"app_id\"";
    public static final String js_req_user_user_id = "\"user_id\"";
    public static final String js_req_user_phone = "\"phone\"";
    public static final String js_req_user_modellevel = "\"modellevel\"";
    public static final String js_req_app_version = "\"app_version\"";
    public static final String js_req_cell_neigh_lac = "\"lac\"";
    public static final String js_req_cell_neigh_cid = "\"cid\"";
    public static final String js_req_cell_neigh_rssi = "\"rssi\"";
    public static final String js_req_cell_mcc = "\"mcc\"";
    public static final String js_req_cell_mnc_sid = "\"mnc_sid\"";
    public static final String js_req_cell_lac_nid = "\"lac_nid\"";
    public static final String js_req_cell_cellid_bsid = "\"cellid_bsid\"";
    public static final String js_req_cell_rssi = "\"rssi\"";
    public static final String js_req_cell_type = "\"type\"";
    public static final String js_req_cell_lon_cdma = "\"lon_cdma\"";
    public static final String js_req_cell_lat_cdma = "\"lat_cdma\"";
    public static final String js_req_cell_neighcells = "\"neighcells\"";
    public static final String js_req_wifi_mac = "\"mac\"";
    public static final String js_req_wifi_level = "\"level\"";
    public static final String js_req_wifi_ssid = "\"ssid\"";
    public static final String js_req_wifi_frequency = "\"frequency\"";
    public  static final String js_req_wifi_connect = "\"connect\"";
    public static final String js_req_time_diff = "\"time_diff\"";
    public static final String js_req_sensor_timestamp = "\"timestamp\"";
    public static final String js_req_sensor_wifi_open_not = "\"wifi_open_not\"";
    public static final String js_req_sensor_wifi_scan_available = "\"wifi_scan_available\"";
    public static final String js_req_sensor_gps_open_not = "\"gps_open_not\"";
    public static final String js_req_sensor_wifi_connect_not = "\"connect_type\"";
    public static final String js_req_sensor_air_press = "\"air_press\"";
    public static final String js_req_sensor_light_value = "\"light_value\"";
    public static final String js_req_sensor_gps_inter = "\"gps_inter\"";
    public static final String js_req_sensor_location_switch_level = "\"location_switch_level\"";
    public static final String js_req_sensor_location_permission = "\"location_permission\"";
    //protected static final String js_req_sensor_pdr_lon = "\"pdr_lon\"";
    //protected static final String js_req_sensor_pdr_lat = "\"pdr_lat\"";
    public static final String js_req_user_info = "\"user_info\"";
    public static final String js_req_cell = "\"cell\"";
    public  static final String js_req_wifis = "\"wifis\"";
    public static final String js_req_valid_flag = "\"valid_flag\"";
    public static final String js_req_version = "\"version\"";
    public static final String js_req_trace_id = "\"trace_id\"";
    public  static final String js_req_tencent_loc = "\"tencent_loc\"";
    public static final String js_req_user_sensors_info = "\"user_sensors_info\"";
    public  static final String js_req_listeners_info = "\"listeners_info\"";
    public  static final String js_rsp_loc_lon_gcj = "\"lon_gcj\"";
    public  static final String js_rsp_loc_lat_gcj = "\"lat_gcj\"";
    public  static final String js_rsp_loc_accuracy = "\"accuracy\"";
    public  static final String js_rsp_loc_confidence = "\"confidence\"";
    public  static final String js_rsp_ret_code = "\"ret_code\"";
    public  static final String js_rsp_ret_msg = "\"ret_msg\"";
    public static final String js_rsp_timestamp = "\"timestamp\"";
    public static final String js_rsp_coord_system = "\"coord_system\"";
    public static final String js_rsp_locations = "\"locations\"";

    public  static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    public  static final SimpleDateFormat simpleDateFormatExt1 = new SimpleDateFormat("HH:mm:ss.SSS");
    public  static final SimpleDateFormat simpleDateFormatExt2 = new SimpleDateFormat("yyyyMMdd");

    private static final byte[] KEY = "0123456789abcdef".getBytes();
//
//    public static final int COORDINATE_TYPE_GCJ02 = 1;
//    public static final int COORDINATE_TYPE_WGS84 = 0;
    /**
     * 默认CGI信号（GSM/CDMA/WCDMA）强度（DBM）
     */
    public static final int iDefCgiSig = -113;

    /**
     * 双卡类型，未知
     */
    public static final int iSim2Def = 0;
    /**
     * 双卡服务名，未知
     */
    public static final String strSim2Def = "phone2";
    /**
     * 双卡类型，高通
     */
    public static final int iSim2QualComm = 1;
    /**
     * 双卡服务名，高通
     */
    public static final String strSim2QualComm = "phone_msim";
    /**
     * 双卡类型，MTK
     */
    public static final int iSim2Mtk = 2;
    /**
     * 双卡服务名，MTK
     */
    public static final String strSim2Mtk = "phone2";

    /**
     * GPSEVENT处理的最小时间间隔（毫秒）
     */
    public static final long MIN_GPS_EVENT_GAP = 10000L;
    /**
     * CGI强制刷新的默认时间间隔（毫秒）
     */
    public static final long lDefCgiUpdate = 30000L;
    //腾讯位置的有效时间, 30ms
    public static final long VALIDATE_INTERVAL_TENCENT_LOCATION = 30*1000;
    //定位轨迹上传的默认时间间隔
    public static final long UPLOAD_TRACE_INTERVAL = 30*60*1000;

    /**
     * CGI强制刷新的最小时间间隔（毫秒）
     */
    public static long lMinCgiUpdate = lDefCgiUpdate;
    /**
     * NLP 定位位置的有效时间
     */
    public static final int VALIDATE_INTERVAL_NLP_LOCATION = 20*1000; // ms
    //当NLP精度好于滴滴网络定位精度，是否优先使用NLP的开关
    public static final String NLP_APOLLO_SWITCH = "nlp_loc_strategy";
    //是否写日志的开关
    public static final String APOLLO_ALLOW_BAMAI_LOG = "allow_loc_sdk_bamai_log";
    //定位开关打开的级别
    public static final int LOCATION_LEVEL_OFF = 0;
    public static final int LOCATION_LEVEL_SENSORS_ONLY = 1;
    public static final int LOCATION_LEVEL_BATTERY_SAVING = 2;
    public static final int LOCATION_LEVEL_HIGH_ACCURACY = 3;
    /**
     * google flp 获取的位置的有效时间
     */
    public static final long VALIDATE_INTERVAL_GOOGLE_FLP_LOCATION = 20*1000;
    /**
     * gps精度阈值，用于粗判google flp 位置是来自gps还是其他。
     */
    public static final float GPS_ACCURACY_LIMIT_FOR_GOOGLE_FLP = 30.0f;

    public  static String formatDouble(double num, int dots) {
        return String.format("%." + dots + "f", num);
    }
    public static String formatFloat(float num, int dots) {
        return String.format("%." + dots + "f", num);
    }

    public static String formatString(String str) {
        if(str == null) return "\"\"";
        return "\"" + str.replace(jsSepr, "").replace(joLeft, "").replace(joRight, "").replace(jaLeft, "").replace(jaRight, "") + "\"";
    }

    public  static String getJsonObject(String src, String key) {
        int start;
        int end;
        start = src.indexOf(key) + key.length() + 1;
        end = findJsonObjEnd(src, start);

        if(start == key.length() || end == -1) return "";

        return src.substring(start, end).trim();
    }

    public  static String getJsonObjectString(String src, String key) {
        int start;
        int end;
        start = src.indexOf(key) + key.length() + 1;
        end = findJsonObjEnd(src, start);

        if(start == key.length() || end == -1) return "";

        return src.substring(start + 1, end - 1);
    }

    public  static ArrayList<String> getJsonArrayObjects(String arraystr) {
        if(arraystr == null || arraystr.length() <= 1 || arraystr.equals("null")) return new ArrayList<>();

        ArrayList<String> ret = new ArrayList<>();

        int cur = 1;

        while(cur < arraystr.length() - 1) {
            int start = cur;
            int end = findJsonArrayEnd(arraystr, cur);
            if (start != -1 && end != -1) {
                ret.add(arraystr.substring(start, end));
            }
            cur = end + 1;
        }

        return ret;
    }

    private static int findJsonObjEnd(String jsonStr, int cur) {

        if(cur == -1) return -1;

        int branketlv = 0;
        int arraylv = 0;
        while (cur < jsonStr.length()) {
            char curchar = jsonStr.charAt(cur);
            if(curchar == Constants.joLeft.charAt(0)) {
                branketlv++;
                cur++;
                continue;
            }
            if(curchar == Constants.jaLeft.charAt(0)) {
                arraylv++;
                cur++;
                continue;
            }
            if(curchar == Constants.joRight.charAt(0)) {
                branketlv--;
            }
            if(curchar == Constants.jaRight.charAt(0)) {
                arraylv--;
            }
            if((curchar == Constants.jsSepr.charAt(0) || curchar == Constants.joRight.charAt(0)) && branketlv <= 0 && arraylv <= 0) {
                return cur;
            }
            cur++;
        }
        return -1;
    }
    private static int findJsonArrayEnd(String jsonStr, int cur) {

        if(cur == -1) return -1;

        int branketlv = 0;
        int arraylv = 0;
        while (cur < jsonStr.length()) {
            char curchar = jsonStr.charAt(cur);
            if(curchar == Constants.joLeft.charAt(0)) {
                branketlv++;
                cur++;
                continue;
            }
            if(curchar == Constants.jaLeft.charAt(0)) {
                arraylv++;
                cur++;
                continue;
            }
            if(curchar == Constants.joRight.charAt(0)) {
                branketlv--;
            }
            if(curchar == Constants.jaRight.charAt(0)) {
                arraylv--;
            }
            if((curchar == Constants.jsSepr.charAt(0) || curchar == Constants.jaRight.charAt(0)) && branketlv <= 0 && arraylv <= 0) {
                return cur;
            }
            cur++;
        }
        return -1;
    }

    public static byte[] getGZipCompressed(byte[] byteData) {

        if(byteData == null || byteData.length <= 0) return null;

        byte[] compressed;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteData);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            int count;
            byte data[] = new byte[1024];
            while ((count = byteArrayInputStream.read(data)) > 0) {
                gzipOutputStream.write(data, 0, count);
            }

            gzipOutputStream.finish();
            gzipOutputStream.close();

            compressed = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            byteArrayInputStream.close();
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
        return compressed;
    }

    public static byte[] encrypt(byte[] srcBytes, boolean newformat) {

        //String s = Arrays.toString(srcBytes);

        byte[] encrypted = null;
        byte[] iv = new byte[16];
        for (int i = 0; i < 8; i++) {
            int v = (int)(Math.random() * 100000) % 65536;
            if (v > Short.MAX_VALUE) v += 2*Short.MIN_VALUE;
            iv[2 * i]       = (byte)(v);
            iv[2 * i + 1]   = (byte)(v >> 8);
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            encrypted = cipher.doFinal(srcBytes);

        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            LogHelper.writeException(e);
            LogHelper.write("-DiDiNetworkLocateProxy- NoSuchPaddingOrAlgorithm occurred.");
        } catch (InvalidAlgorithmParameterException | InvalidKeyException ex) {
            LogHelper.writeException(ex);
            LogHelper.write("-DiDiNetworkLocateProxy- KeyOrParamException occurred.");
        } catch (IllegalBlockSizeException | BadPaddingException exx) {
            LogHelper.writeException(exx);
            LogHelper.write("-DiDiNetworkLocateProxy- SizeOrPaddingException occurred.");
        }

        if (encrypted == null || encrypted.length == 0) {
            return encrypted;
        }

        byte[] retBytes = null;
        if (newformat) {
            retBytes = new byte[encrypted.length + iv.length];
            System.arraycopy(iv, 0, retBytes, 0, iv.length);
            //retBytes[iv.length] = (byte) (encrypted.length - srcBytes.length);
            System.arraycopy(encrypted, 0, retBytes, iv.length, encrypted.length);
        } else {
            retBytes = new byte[encrypted.length + iv.length + 1];
            System.arraycopy(iv, 0, retBytes, 0, iv.length);
            retBytes[iv.length] = (byte) (encrypted.length - srcBytes.length);
            System.arraycopy(encrypted, 0, retBytes, iv.length + 1, encrypted.length);
        }
        return retBytes;
    }

    public static String bytesToHex(byte[] src) {
        String res = "";
        if (src == null) return res;
        for (byte one : src) {
            res += Integer.toHexString(one);
        }
        return res;
    }
}
