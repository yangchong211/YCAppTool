package com.yc.location;

import android.content.Context;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.bean.LocCache;
import com.yc.location.constant.Constants;
import com.yc.location.log.LogHelper;
import com.yc.location.net.OkHttpUtils;
import com.yc.location.data.ETraceSource;
import com.yc.location.utils.TransformUtils;
import com.yc.location.utils.LocationUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class NetworkLocateProxy {
    

    final static String TAG = "-DiDiNetworkLocateProxy-";
    private final Context mContext;
    private boolean mReduceLackOfTrace = false;

    //private final byte[] KEY = {
    //        (byte)0x2b, (byte)0x7e, (byte)0x15, (byte)0x16,
    //        (byte)0x28, (byte)0xae, (byte)0xd2, (byte)0xa6,
    //        (byte)0xab, (byte)0xf7, (byte)0x15, (byte)0x88,
    //        (byte)0x09, (byte)0xcf, (byte)0x4f, (byte)0x3c };


    /** 缓存位置 */
    private LocCache lastLocCache = null;

    /** 上一次网络定位使用的请求 */
    private LocationServiceRequest lastReq = null;

    private boolean needNlpFlag = false;
    private DefaultLocation nlpLocation = null;
    private String fillTencentLocToNextReq = null;

    /** 上一次请求结果是否有效的标志 */
    private int lastResponseFlag = 0;

    /**
     * 最近获得滴滴定位服务的时间，首次启动视为获取到服务
     */
    private long lastGetDiDiLocSer_ts = 0;
    private int noServTimes = 0;

    /** 跨城判断 */
    private LocationBuffer locBuffer = new LocationBuffer();

    /** 单次请求时间 */
    private long lastReqCostMillis = 0;

    private boolean needRequestForLoc;

    public NetworkLocateProxy(Context context) {
        this.mContext = context;
    }

    protected LocCache manage(LocationServiceRequest locReqData, ErrorInfo errInfo) {

        LocationServiceResponse resdata = null;
        long                        now_ts = System.currentTimeMillis();

        needNlpFlag = false;

        // reduce QPS :
        // 1) tell wifi cell change when no gps;
        // 2) tell high speed when have gps

        needRequestForLoc = false;

        if(lastLocCache == null || lastLocCache.accuracy > 200) {
            // 无缓存或缓存不佳，需要请求
            needRequestForLoc = true;
        } else if(isWifiChanged(lastReq, locReqData)) {
            // WIFI切换，需要请求
            needRequestForLoc = true;
        } else if(lastResponseFlag == 0) {
            // 上一次返回无效，需要请求
            needRequestForLoc = true;
        } else {
            LogHelper.logBamai("use cache" );
        }

        if(needRequestForLoc) {

            locReqData.tencent_loc = "" + lastReqCostMillis + " ";

            // 上一次使用腾讯位置时，在本次请求中添加标志
            if(fillTencentLocToNextReq != null && fillTencentLocToNextReq.length() > 0) {
                long sertime = System.currentTimeMillis() - lastGetDiDiLocSer_ts;
                noServTimes += 1;
                locReqData.tencent_loc += fillTencentLocToNextReq + " " + sertime + " " + noServTimes;
            }

            // 定位请求总次数埋点
            if ((int) (locReqData.valid_flag) != ValidFlagEnum.invalid.ordinal()) {
                // 请求网络位置
                long timeb = System.currentTimeMillis();
				LogHelper.logBamai("req:\n" + locReqData.toBamaiLog());
                try {
                    OkHttpUtils.HttpResponse response = sendRequest(locReqData);
                    if (response == null) {
                        resdata = null;
                        errInfo.setErrNo(ErrorInfo.ERROR_HTTP_RESPONSE_NULL);
                        errInfo.setErrMessage(mContext.getString(R.string.location_err_http_request_response_null));
                    } else {
                        int code = response.httpRespCode;
                        if (code == 200) {
                            String body = response.body;
                            if (body == null) {
                                errInfo.setErrNo(ErrorInfo.ERROR_HTTP_REQUEST_NO_LOCATION_RETURN);
                                errInfo.setErrMessage(mContext.getString(R.string.location_err_http_request_return_empty));
                            } else {
                                resdata = parseResponse(body);
                                lastReqCostMillis = System.currentTimeMillis() - timeb;
                                if (resdata == null) {
                                    errInfo.setErrNo(ErrorInfo.ERROR_HTTP_REQUEST_NO_LOCATION_RETURN);
                                    errInfo.setErrMessage(mContext.getString(R.string.location_err_http_request_return_empty));
                                } else {
                                    if (resdata.ret_code != 0 ||
                                            resdata.locations == null ||
                                            resdata.locations.size() == 0) {
                                        errInfo.setErrNo(ErrorInfo.ERROR_HTTP_REQUEST_NO_LOCATION_RETURN);
                                        errInfo.setErrMessage(mContext.getString(R.string.location_err_http_request_return_empty));
                                    }
                                }
                            }
                        } else {
                            errInfo.setErrNo(ErrorInfo.ERROR_HTTP_REQUEST_NORMAL_ERR);
                            errInfo.setErrMessage(mContext.getString(R.string.location_err_http_request_normal_err));
                            errInfo.setResponseCode(code);
                            errInfo.setResponseMessage("");
                        }
                    }
                } catch (IOException e) {
                    errInfo.setErrNo(ErrorInfo.ERROR_HTTP_REQUEST_EXCEPTION);
                    errInfo.setErrMessage(mContext.getString(R.string.location_err_http_request_exception));
                    errInfo.setRequestExceptionMessage(e.getMessage());
                    resdata = null;
                    if (LocationUtils.isNetWorkConnected(mContext)) {
                        Map<String, Object> param = new HashMap<>();
                        param.put("sdk_ver", BuildConfig.VERSION_NAME);
                        param.put("exception", e.getMessage());
                        param.put("network_type", LocationUtils.getConnectedType(mContext));
                    }
                }
            } else {
                errInfo.setErrNo(ErrorInfo.ERROR_NO_ELEMENT_FOR_LOCATION);
                errInfo.setErrMessage(mContext.getString(R.string.location_err_no_element));
            }

			if (resdata == null || resdata.ret_code != 0 || resdata.locations.size() <= 0) {
                LogHelper.logBamai("req failed.");
                if (resdata != null && resdata.ret_code != 0) {
                    // todo - 定位失败
                }
            } else {
                LogHelper.logBamai("response\n" + resdata.toJson());
            }

            now_ts = System.currentTimeMillis();

            // 缓存多个位置清洗网络返回的跳变,解决跨城问题
            if(!locBuffer.isCompatible(resdata)) {
                // 不兼容时，将网络位置清空
                resdata = null;
            }

            lastReq = locReqData;

            if(resdata != null) {
                // 本次请求服务成功返回 - 表明服务接收到标志，不论此次位置结果是否为空，应当清除服务不能的标记及下次填充标记
                lastGetDiDiLocSer_ts = System.currentTimeMillis();
                noServTimes = 0;
                fillTencentLocToNextReq = null;
            }
            if(resdata == null || resdata.ret_code != 0 || resdata.locations.size() <= 0) {
                lastResponseFlag = 0;
                needNlpFlag = true;

            } else {
                lastResponseFlag = 1;

            }
        } else {
            // 不进行网络请求
            // 可能有GPS位置
            return lastLocCache;
        }

        // fill locstrategy input array
        ArrayList<LocCache> locCaches = new ArrayList<>();

        if(resdata != null && resdata.locations != null) {
            for(location_info_t loc : resdata.locations) {
                LocCache netloc = new LocCache(loc.lon_gcj, loc.lat_gcj, (int)loc.accuracy, loc.confidence, 0, now_ts, ETraceSource.didi.toString(), resdata.getCoordinateType());
                netloc.setProvider(loc.confidence<=1 ? DefaultLocation.CELL_PROVIDER : DefaultLocation.WIFI_PROVIDER);
                locCaches.add(netloc);
            }
        }
        if(needNlpFlag && nlpLocation != null) {
            LocCache nlploc = new LocCache(nlpLocation.getLongitude(), nlpLocation.getLatitude(), (int)nlpLocation.getAccuracy(), Constants.nlpLocConfi, 0, now_ts, ETraceSource.nlp.toString(), nlpLocation.getCoordinateType());
            nlploc.provider = DefaultLocation.NLP_PROVIDER;

            locCaches.add(nlploc);

            fillTencentLocToNextReq = String.format(Locale.CHINA, "%.6f %.6f %d", nlpLocation.getLongitude(), nlpLocation.getLatitude(), (int)nlpLocation.getAccuracy());
        }

        if(locCaches.isEmpty()) {
            LogHelper.write("-DiDiNetworkLocateProxy-ret- null candidate locs");
            return null;
        }

        // calc loc output
        if(lastLocCache != null) {
            LogHelper.write("-DiDiNetworkLocateProxy- lastLocCache=" + lastLocCache.toJson());

            ArrayList<LocCache> rmLocCaches = new ArrayList<>();

            for(LocCache locCache : locCaches) {

                if(locCache.accuracy > lastLocCache.accuracy * 10 && lastLocCache.accuracy > Constants.gpslocAccuracyConf){
                    rmLocCaches.add(locCache);
                    LogHelper.write("-DiDiNetworkLocateProxy- remove loc that too large accuracy");
                    continue;
                }

                double dist = TransformUtils.calcdistance(lastLocCache.lonlat.lon, lastLocCache.lonlat.lat, locCache.lonlat.lon, locCache.lonlat.lat);

                LogHelper.write("-DiDiNetworkLocateProxy- ["
                        + String.format(Locale.CHINA, "%.6f,%.6f,%.6f,%.6f] dist=%.2f", lastLocCache.lonlat.lon, lastLocCache.lonlat.lat, locCache.lonlat.lon, locCache.lonlat.lat, dist));

                long intervalMs = locCache.timestamp - lastLocCache.timestamp;
                double timeinterval = intervalMs / (double)1000;

                //double maxAcc = lastLocCache.accuracy > locCache.accuracy ? lastLocCache.accuracy : locCache.accuracy;

                double dist_fix = dist;// - maxAcc;
                dist_fix = dist_fix > 0.0 ? dist_fix : 0.0;

                int speed = (int)(dist_fix / timeinterval);
                double transprob = 1.0 / (Math.abs(speed - lastLocCache.speed) + 1);

                LogHelper.write("-DiDiNetworkLocateProxy- "
                        + String.format(Locale.CHINA, "dist=%.2f, timeinterval=%.1f, dist_fix=%.2f, speed=%d, transprob=%.3f", // maxAccuracy=%.2f,
                        dist, timeinterval, /*maxAcc,*/ dist_fix, speed, transprob));

                locCache.speed = speed;
                locCache.transprob = transprob;
            }

            locCaches.removeAll(rmLocCaches);
        } else {
            LogHelper.write("-DiDiNetworkLocateProxy- lastLocCache=null");
        }
        if(locCaches.size() <= 0) {
            LogHelper.logBamai("locations is empty after remove.");
            return null;
        }
        //choose loc 注：第一次网络定位会进入
        if(isAllCandiLowTransprob(locCaches)) {
            LocCache ret = getMaxConfiLoc(locCaches);
            if(lastLocCache == null || ret.confidence >= lastLocCache.confidence) {
                lastLocCache = new LocCache(ret.lonlat.lon, ret.lonlat.lat, ret.accuracy, ret.confidence, ret.speed, now_ts, ret.lonlat.source, ret.coordinateType);
                LogHelper.write("-DiDiNetworkLocateProxy- ret: in low transprob, first loc or cur confidence >= last's : ret=" + ret.toJson());
                return ret;
            }
            LogHelper.logBamai("-DiDiNetworkLocateProxy- ret: in low transprob, cur confidence < last's");
            //第一次网络定位时为null
            //1、保留上次定位位置(不清空lastLocCache)，以对后续出现的位置过滤跳点。也是为了降低轨迹缺失，不至于一次取到基站定位点，后面wifi定位点都被跳点策略（isAllCandiLowTransprob）过滤掉了。前期AB测试，结合下面第2点。
            if (mReduceLackOfTrace) {
                return lastLocCache;
            } else {
                LocCache temp = lastLocCache;
                lastLocCache = null;
                return temp;
            }
        }

        LocCache ret;
        if (null == lastLocCache) {
            //第一次定位就去最大confidence的位置。
            ret = getMaxConfiLoc(locCaches);
        } else {
            //2、上次位置为基站定位所得，本次直接取最大confidence的位置（此情况本次偏向于取wifi定位结果），不至于一次取到基站定位点，后面wifi定位点都被跳点策略（isAllCandiLowTransprob）过滤掉了。前期AB测试，见前1。
            if (lastLocCache.provider != null
                    && lastLocCache.provider.equals(DefaultLocation.CELL_PROVIDER)
                    && mReduceLackOfTrace) {
                ret = getMaxConfiLoc(locCaches);
            } else {
                ret = getMaxConfiTransprobLoc(locCaches);
            }
        }
        lastLocCache = new LocCache(ret.lonlat.lon, ret.lonlat.lat, ret.accuracy, ret.confidence, ret.speed, now_ts, ret.lonlat.source, ret.coordinateType);
        LogHelper.write("-DiDiNetworkLocateProxy- ret: " + ret.toJson());
        return ret;
    }

    public  LocCache generateLocCacheFromGps(DefaultLocation gpsData) {
        LocCache ret = new LocCache(gpsData.getLongitude(), gpsData.getLatitude(),
                (int)gpsData.getAccuracy(),
                2.0,
                (int) gpsData.getSpeed(),
                gpsData.getTime(), ETraceSource.gps.toString(), gpsData.getCoordinateType());
        ret.setProvider(gpsData.getProvider());
        return ret;
    }
    public  void cleanHistoryWithGps(DefaultLocation gpsData) {
        cleanHistory(false);
        lastLocCache = generateLocCacheFromGps(gpsData);

    }
    public void cleanHistory(boolean keepLocBuffer) {
        lastLocCache = null;
        lastReq = null;

        lastGetDiDiLocSer_ts = System.currentTimeMillis();
        noServTimes = 0;

        if (!keepLocBuffer) {
            locBuffer.clear();
        }
    }

    protected void setLastLocCache(LocCache locCache) {
        lastLocCache = locCache;
    }

    protected void setNlpLoc(DefaultLocation location) {
        nlpLocation = location;
    }


//    private double calcGpsConfidence(LocationServiceResponse locationServiceResponse, int accuracy) {
//        double totalConfidence = 0.0;
//        double avgConfidence = 0.0;
//        if(locationServiceResponse != null && locationServiceResponse.locations != null && locationServiceResponse.locations.size() > 0) {
//            for(location_info_t loc : locationServiceResponse.locations) {
//                totalConfidence += loc.confidence;
//            }
//            avgConfidence = totalConfidence/locationServiceResponse.locations.size();
//        }
//        LogHelper.write("-calcGpsConfidence- extra confi=" + (accuracy <= Const.gpslocAccuracyConf ? Const.gpslocConfiExtra : 0.0));
//        return avgConfidence + (accuracy <= Const.gpslocAccuracyConf ? Const.gpslocConfiExtra : 0.0);
//    }

    // is all candidate loc's transprobs are lower than 0.02
    private boolean isAllCandiLowTransprob(ArrayList<LocCache> locCaches) {
        for(LocCache locCache : locCaches) {
            if(locCache.transprob >= Constants.transprobLowThreshold) {
                return false;
            }
        }
        return true;
    }

    private LocCache getMaxConfiLoc(ArrayList<LocCache> locCaches) {

        LocCache ret = locCaches.get(0);
        for(LocCache locCache : locCaches) {
            LogHelper.write("-getMaxConfiLoc- confi=" + locCache.confidence);
            if(ret.confidence < locCache.confidence) {
                ret = locCache;
            }
        }
        return ret;
    }

    private LocCache getMaxConfiTransprobLoc(ArrayList<LocCache> locCaches) {

        LocCache ret = locCaches.get(0);
        for(LocCache locCache : locCaches) {
            LogHelper.write("-getMaxConfiTransprobLoc- confi*transprob=" + locCache.confidence * locCache.transprob);
            if(ret.confidence * ret.transprob < locCache.confidence * locCache.transprob) {
                ret = locCache;
            }
        }
        return ret;
    }

    private OkHttpUtils.HttpResponse sendRequest(LocationServiceRequest locReqData) throws IOException {

            // to json string
        String jsonStr = locReqData.toJson();

        LogHelper.write("-DiDiNetworkLocateProxy- req json: " + jsonStr);

        // gzip compress
        byte[] parambytes;
        try {
            parambytes = jsonStr.getBytes(Constants.paramCharset);
        } catch (UnsupportedEncodingException e) {
            LogHelper.writeException(e);
            parambytes = null;
        }

        try {
            if(parambytes != null && parambytes.length > 0) {
                LogHelper.write("-DiDiNetworkLocateProxy- param len before compress : " + parambytes.length);

                byte[] cpParambytes = Constants.encrypt(Constants.getGZipCompressed(parambytes), false);

                if (cpParambytes != null && cpParambytes.length > 0) {
                    LogHelper.write("-DiDiNetworkLocateProxy- param len after compress : " + cpParambytes.length);

                    // request network location
                    //LogHelper.write("-DiDiNetworkLocateProxy- post send, url=" + Const.serverUrl + ", threadid=" + Thread.currentThread().getId());

                    return postWithRetry(cpParambytes, 0);
                }
            }
        } catch (OutOfMemoryError e) {
            LogHelper.write("sendRequest:" + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        return null;
    }

    private OkHttpUtils.HttpResponse postWithRetry(byte[] bytes, long next_retry_ms) throws IOException{

        OkHttpUtils.HttpResponse response = null;

        try {
            response = OkHttpUtils.post(Constants.serverUrl, bytes, "");
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException) {

                switch ((int)next_retry_ms) {

                    case 0:
                        LogHelper.write("-DiDiNetworkLocateProxy- time out 1 time");
                        response = postWithRetry(bytes, 1000);
                        break;

                    case 1000:
                        LogHelper.write("-DiDiNetworkLocateProxy- time out 2 times");
                        try {Thread.sleep(1000);} catch (InterruptedException ie) { LogHelper.writeException(ie); }
                        response = postWithRetry(bytes, -1);
                        break;

                    //case 1500:
                    //    LogHelper.write("-DiDiNetworkLocateProxy- time out 3 times");
                    //    try {Thread.sleep(1000);} catch (InterruptedException ie) { LogHelper.writeException(ie); }
                    //    ret = postWithRetry(bytes, -1);
                    //    break;

                    default:
                        LogHelper.write("-DiDiNetworkLocateProxy- time out 4 times");
                        throw e;
                }
            } else {
                LogHelper.writeException(e);
                throw e;
            }
        }

        return response;
    }

    private LocationServiceResponse parseResponse(String responseString) {
        if(responseString != null && responseString.length() > 0) {

            LogHelper.write("-DiDiNetworkLocateProxy- response=" + responseString);
            return LocationServiceResponse.toObject(responseString);
        }

        LogHelper.write("-DiDiNetworkLocateProxy- response=null");
        return null;
    }

    private boolean isWifiChanged(LocationServiceRequest old, LocationServiceRequest curr) {

        if (old == null || curr == null) {
            return true;
        }

        List<wifi_info_t> lastWifiList = old.wifis;
        List<wifi_info_t> currentWifiList = curr.wifis;

        if (lastWifiList == null || currentWifiList == null) {
            return true;
        }

        int diffSize = diff(currentWifiList, lastWifiList);
        LogHelper.write("-DiDiNetworkLocateProxy- wifi size: " + lastWifiList.size() + " -> " + currentWifiList.size() + " DIFF(" + diffSize + ")");

        return (diffSize >= 5 || diffSize > lastWifiList.size() * 0.2 || diffSize > currentWifiList.size() * 0.2);
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
    private int diff(List<wifi_info_t> list1, List<wifi_info_t> list2) {

        int same = 0;
        for (wifi_info_t aTwo : list2) {
            if (isOneInWifiList(aTwo, list1)) {
                same++;
            }
        }

        return list1.size() + list2.size() - 2 * same;
    }

    private boolean isOneInWifiList(wifi_info_t w, List<wifi_info_t> l) {
        for(int i = 0; i < l.size(); i++) {
            if(w.mac.equals(l.get(i).mac)) {
                return true;
            }
        }
        return false;
    }
}
