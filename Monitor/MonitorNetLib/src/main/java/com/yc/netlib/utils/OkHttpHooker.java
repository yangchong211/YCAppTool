package com.yc.netlib.utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Dns;
import okhttp3.EventListener;
import okhttp3.Interceptor;


public class OkHttpHooker {

    public static EventListener.Factory globalEventFactory = new EventListener.Factory() {
        public EventListener create(Call call) {
            return EventListener.NONE;
        }
    };;

    public static Dns globalDns = Dns.SYSTEM;

    public static List<Interceptor> globalInterceptors = new ArrayList<>();

    public static List<Interceptor> globalNetworkInterceptors = new ArrayList<>();

    public static void installEventListenerFactory(EventListener.Factory factory) {
        globalEventFactory = factory;
    }

    public static void installDns(Dns dns) {
        globalDns = dns;
    }

    public static void installInterceptor(Interceptor interceptor) {
        if(interceptor != null)
            globalInterceptors.add(interceptor);
    }

    public static void installNetworkInterceptors(Interceptor networkInterceptor) {
        if(networkInterceptor != null)
            globalNetworkInterceptors.add(networkInterceptor);
    }


}
