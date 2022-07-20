package com.yc.netlib.stetho;

import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.yc.netlib.data.IDataPoolHandleImpl;
import com.yc.netlib.data.NetworkTraceBean;
import com.yc.netlib.utils.NetWorkUtils;
import com.yc.toolutils.AppLogUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/07/22
 *     desc  : EventListener子类
 *     revise: 这个是统计耗时的监听
 * </pre>
 */
public class NetworkListener extends EventListener {

    private static final String TAG = "NetworkEventListener";
    private static final AtomicInteger mNextRequestId = new AtomicInteger(0);
    private String mRequestId ;

    public static Factory get(){
        Factory factory = new Factory() {
            @Override
            public EventListener create(Call call) {
                return new NetworkListener();
            }
        };
        return factory;
    }

    @Override
    public void callStart(Call call) {
        super.callStart(call);
        //mRequestId = mNextRequestId.getAndIncrement() + "";
        //getAndAdd，在多线程下使用cas保证原子性
        mRequestId = String.valueOf(mNextRequestId.getAndIncrement());
        AppLogUtils.i(TAG+"-------callStart---requestId-----"+mRequestId);
        saveEvent(NetworkTraceBean.CALL_START);
        saveUrl(call.request().url().toString());
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        AppLogUtils.d(TAG, "dnsStart");
        saveEvent(NetworkTraceBean.DNS_START);
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        AppLogUtils.d(TAG, "dnsEnd");
        saveEvent(NetworkTraceBean.DNS_END);
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        AppLogUtils.d(TAG, "connectStart");
        saveEvent(NetworkTraceBean.CONNECT_START);
    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        AppLogUtils.d(TAG, "secureConnectStart");
        saveEvent(NetworkTraceBean.SECURE_CONNECT_START);
    }

    @Override
    public void secureConnectEnd(Call call, @Nullable Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        AppLogUtils.d(TAG, "secureConnectEnd");
        saveEvent(NetworkTraceBean.SECURE_CONNECT_END);
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress,
                           Proxy proxy, @Nullable Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        AppLogUtils.d(TAG, "connectEnd");
        saveEvent(NetworkTraceBean.CONNECT_END);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, @Nullable Protocol protocol, IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        AppLogUtils.d(TAG, "connectFailed");
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        AppLogUtils.d(TAG, "requestHeadersStart");
        saveEvent(NetworkTraceBean.REQUEST_HEADERS_START);
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        AppLogUtils.d(TAG, "requestHeadersEnd");
        saveEvent(NetworkTraceBean.REQUEST_HEADERS_END);
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        AppLogUtils.d(TAG, "requestBodyStart");
        saveEvent(NetworkTraceBean.REQUEST_BODY_START);
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        AppLogUtils.d(TAG, "requestBodyEnd");
        saveEvent(NetworkTraceBean.REQUEST_BODY_END);
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        AppLogUtils.d(TAG, "responseHeadersStart");
        saveEvent(NetworkTraceBean.RESPONSE_HEADERS_START);
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        AppLogUtils.d(TAG, "responseHeadersEnd");
        saveEvent(NetworkTraceBean.RESPONSE_HEADERS_END);
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        AppLogUtils.d(TAG, "responseBodyStart");
        saveEvent(NetworkTraceBean.RESPONSE_BODY_START);
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        AppLogUtils.d(TAG, "responseBodyEnd");
        saveEvent(NetworkTraceBean.RESPONSE_BODY_END);
    }

    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        AppLogUtils.d(TAG, "callEnd");
        saveEvent(NetworkTraceBean.CALL_END);
        generateTraceData();
        NetWorkUtils.timeoutChecker(mRequestId);
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        AppLogUtils.d(TAG, "callFailed");
    }

    private void generateTraceData(){
        NetworkTraceBean traceModel = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(mRequestId);
        Map<String, Long> eventsTimeMap = traceModel.getNetworkEventsMap();
        Map<String, Long> traceList = traceModel.getTraceItemList();
        traceList.put(NetworkTraceBean.TRACE_NAME_TOTAL,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.CALL_START, NetworkTraceBean.CALL_END));
        traceList.put(NetworkTraceBean.TRACE_NAME_DNS,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.DNS_START, NetworkTraceBean.DNS_END));
        traceList.put(NetworkTraceBean.TRACE_NAME_SECURE_CONNECT,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.SECURE_CONNECT_START, NetworkTraceBean.SECURE_CONNECT_END));
        traceList.put(NetworkTraceBean.TRACE_NAME_CONNECT,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.CONNECT_START, NetworkTraceBean.CONNECT_END));
        traceList.put(NetworkTraceBean.TRACE_NAME_REQUEST_HEADERS,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.REQUEST_HEADERS_START, NetworkTraceBean.REQUEST_HEADERS_END));
        traceList.put(NetworkTraceBean.TRACE_NAME_REQUEST_BODY,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.REQUEST_BODY_START, NetworkTraceBean.REQUEST_BODY_END));
        traceList.put(NetworkTraceBean.TRACE_NAME_RESPONSE_HEADERS,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.RESPONSE_HEADERS_START, NetworkTraceBean.RESPONSE_HEADERS_END));
        traceList.put(NetworkTraceBean.TRACE_NAME_RESPONSE_BODY,NetWorkUtils.getEventCostTime(eventsTimeMap,NetworkTraceBean.RESPONSE_BODY_START, NetworkTraceBean.RESPONSE_BODY_END));
    }

    private void saveEvent(String eventName){
        NetworkTraceBean networkTraceModel = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(mRequestId);
        Map<String, Long> networkEventsMap = networkTraceModel.getNetworkEventsMap();
        networkEventsMap.put(eventName, SystemClock.elapsedRealtime());
    }

    private void saveUrl(String url){
        NetworkTraceBean networkTraceModel = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(mRequestId);
        networkTraceModel.setUrl(url);
    }

}
