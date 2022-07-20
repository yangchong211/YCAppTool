package com.yc.netlib.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.yc.netlib.R;
import com.yc.netlib.data.IDataPoolHandleImpl;
import com.yc.netlib.data.NetworkTraceBean;
import com.yc.toolutils.AppLogUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public final class NetWorkUtils {

    public final static long SECOND = 1000;
    public final static long MINUTE = SECOND * 60;
    public final static long HOUR = MINUTE * 60;
    public final static long DAY = HOUR * 24;


    public static LinkedHashMap<String, Long> transformToTraceDetail(Map<String, Long> eventsTimeMap){
        LinkedHashMap<String, Long> traceDetailList = new LinkedHashMap<>();
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_TOTAL,getEventCostTime(eventsTimeMap, NetworkTraceBean.CALL_START, NetworkTraceBean.CALL_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_DNS,getEventCostTime(eventsTimeMap, NetworkTraceBean.DNS_START, NetworkTraceBean.DNS_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_SECURE_CONNECT,getEventCostTime(eventsTimeMap, NetworkTraceBean.SECURE_CONNECT_START, NetworkTraceBean.SECURE_CONNECT_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_CONNECT,getEventCostTime(eventsTimeMap, NetworkTraceBean.CONNECT_START, NetworkTraceBean.CONNECT_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_REQUEST_HEADERS,getEventCostTime(eventsTimeMap, NetworkTraceBean.REQUEST_HEADERS_START, NetworkTraceBean.REQUEST_HEADERS_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_REQUEST_BODY,getEventCostTime(eventsTimeMap, NetworkTraceBean.REQUEST_BODY_START, NetworkTraceBean.REQUEST_BODY_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_RESPONSE_HEADERS,getEventCostTime(eventsTimeMap, NetworkTraceBean.RESPONSE_HEADERS_START, NetworkTraceBean.RESPONSE_HEADERS_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_RESPONSE_BODY,getEventCostTime(eventsTimeMap, NetworkTraceBean.RESPONSE_BODY_START, NetworkTraceBean.RESPONSE_BODY_END));
        return traceDetailList;
    }

    public static long getEventCostTime(Map<String , Long> eventsTimeMap, String startName, String endName){
        if (!eventsTimeMap.containsKey(startName) || !eventsTimeMap.containsKey(endName)) {
            return 0;
        }
        Long endTime = eventsTimeMap.get(endName);
        Long start = eventsTimeMap.get(startName);
        long result = endTime - start;
        return result;
    }

    public static void timeoutChecker(String requestId){
        if (requestId==null || requestId.length()==0){
            return;
        }
        NetworkTraceBean networkTraceModel = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(requestId);
        Map<String, Long> traceItemList = networkTraceModel.getTraceItemList();
        String url = networkTraceModel.getUrl();
        check(NetworkTraceBean.TRACE_NAME_TOTAL, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_DNS, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_SECURE_CONNECT, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_CONNECT, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_REQUEST_HEADERS, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_REQUEST_BODY, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_RESPONSE_HEADERS, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_RESPONSE_BODY, url, traceItemList);
    }

    private static void check(String key, String url , Map<String , Long> traceItemList){
        Context context = NetworkTool.getInstance().getApplication();
        if (context == null) {
            AppLogUtils.d("OkNetworkMonitor", "context is null.");
            return;
        }
        Long costTime = traceItemList.get(key);
        /*if (isTimeout(context, key, costTime)) {
            String title = "Timeout warning. $key cost ${costTime}ms.";
            String content = "url: $url";
            Intent intent = new Intent(context, NetRequestActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationDispatcher.showNotification(context, title, content, pendingIntent, notificationId);
        }*/
    }


    public static boolean isTimeout(Context context , String key , Long costTime){
        int value = getSettingTimeout(context, key);
        if (value <= 0 || costTime == null) {
            return false;
        }
        return costTime > value;
    }

    private static int getSettingTimeout(Context context , String key){
        String string = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "0");
        try {
            int i = Integer.parseInt(string);
            return i;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return 0;
        }
    }

    public static SpannableString formatTime(Context context, long time) {
        SpannableString spannableString;
        if (time == 0) {
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString = new SpannableString(context.getString(R.string.network_summary_total_time_default));
            spannableString.setSpan(sizeSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (time < 100 * SECOND) {
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString = new SpannableString(context.getString(R.string.network_summary_total_time_second, time / SECOND));
            spannableString.setSpan(sizeSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (time < 100 * MINUTE) {
            long minute = time / MINUTE;
            long second = time % MINUTE / SECOND;
            spannableString = new SpannableString(context.getString(R.string.network_summary_total_time_minute, minute, second));
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString.setSpan(sizeSpan, String.valueOf(minute).length(), String.valueOf(minute).length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString.setSpan(sizeSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (time < 100 * HOUR) {
            long hour = time / HOUR;
            long minute = time % HOUR / MINUTE;
            spannableString = new SpannableString(context.getString(R.string.network_summary_total_time_hour, hour, minute));
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString.setSpan(sizeSpan, String.valueOf(hour).length(), String.valueOf(hour).length() + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString.setSpan(sizeSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            long day = time / DAY;
            long hour = time % DAY / HOUR;
            spannableString = new SpannableString(context.getString(R.string.network_summary_total_time_day, day, hour));
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString.setSpan(sizeSpan, String.valueOf(day).length(), String.valueOf(day).length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            sizeSpan = new RelativeSizeSpan(0.5f);
            spannableString.setSpan(sizeSpan, spannableString.length() - 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}
