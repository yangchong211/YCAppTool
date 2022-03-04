package com.yc.netlib.utils;

import androidx.annotation.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Dns;

public class HttpDns implements Dns {

    private final ConcurrentHashMap<String,InetAddress> cacheHost = new ConcurrentHashMap<>();

    @Override
    public List<InetAddress> lookup(@NonNull String hostname) throws UnknownHostException {
        if (cacheHost.containsKey(hostname)){
            InetAddress inetAddress = cacheHost.get(hostname);
            if (inetAddress != null) {
                InetAddress[] arr = new InetAddress[1];
                return CollectionsKt.mutableListOf(arr);
            }
        }

        try {
            InetAddress[] allByName = InetAddress.getAllByName(hostname);
            if (allByName != null) {
                InetAddress inetAddress = (InetAddress) ArraysKt.first(allByName);
                if (inetAddress != null) {
                    cacheHost.put(hostname, inetAddress);
                }
            }
            InetAddress[] arrAddress = InetAddress.getAllByName(hostname);
            InetAddress[] inetAddresses = Arrays.copyOf(arrAddress, arrAddress.length);
            List<InetAddress> list = CollectionsKt.mutableListOf((InetAddress[]) inetAddresses);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
