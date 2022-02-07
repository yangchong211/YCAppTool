package com.yc.netlib.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/07/22
 *     desc  : 网络请求跟踪bean
 *     revise:
 * </pre>
 */
public class NetworkTraceBean implements Serializable {
    
    public static String CALL_START = "callStart";
    public static String CALL_END = "callEnd";
    public static String DNS_START = "dnsStart";
    public static String DNS_END = "dnsEnd";
    public static String CONNECT_START = "connectStart";
    public static String SECURE_CONNECT_START = "secureConnectStart";
    public static String SECURE_CONNECT_END = "secureConnectEnd";
    public static String CONNECT_END = "connectEnd";
    public static String REQUEST_BODY_START = "requestBodyStart";
    public static String REQUEST_BODY_END = "requestBodyEnd";
    public static String REQUEST_HEADERS_START = "requestHeadersStart";
    public static String REQUEST_HEADERS_END = "requestHeadersEnd";
    public static String RESPONSE_HEADERS_START = "responseHeadersStart";
    public static String RESPONSE_HEADERS_END = "responseHeadersEnd";
    public static String RESPONSE_BODY_START = "responseBodyStart";
    public static String RESPONSE_BODY_END = "responseBodyEnd";
    public static String TRACE_NAME_TOTAL = "Total Time";
    public static String TRACE_NAME_DNS = "DNS";
    public static String TRACE_NAME_SECURE_CONNECT = "Secure Connect";
    public static String TRACE_NAME_CONNECT = "Connect";
    public static String TRACE_NAME_REQUEST_HEADERS = "Request Headers";
    public static String TRACE_NAME_REQUEST_BODY = "Request Body";
    public static String TRACE_NAME_RESPONSE_HEADERS = "Response Headers";
    public static String TRACE_NAME_RESPONSE_BODY = "Response Body";

    private String id;
    private String url;
    private long time;
    private Map<String, Long> networkEventsMap = new HashMap<>();
    private Map<String, Long> traceItemList = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, Long> getNetworkEventsMap() {
        return networkEventsMap;
    }

    public void setNetworkEventsMap(Map<String, Long> networkEventsMap) {
        this.networkEventsMap = networkEventsMap;
    }

    public Map<String, Long> getTraceItemList() {
        return traceItemList;
    }

    public void setTraceItemList(Map<String, Long> traceItemList) {
        this.traceItemList = traceItemList;
    }
}
