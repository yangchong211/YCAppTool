package com.yc.networklib.wifi;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * 如何为一个开放式网络、一个 WPA2 网络和一个 WPA3 网络提供凭据
 * 运行 Android 10 (API 级别 29) 或更高版本 的设备允许您的应用添加设备的网络凭据，以自动连接到 WLAN 接入点。
 * 您可以使用 WifiNetworkSuggestion 就连接到哪个网络提供建议。平台最终会根据您的应用和其他应用的建议，选择要接受的接入点。
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
final class WifiNetworkHelper {


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void connectSsidNetwork(@NonNull String ssid) {
        final WifiNetworkSuggestion suggestion1 =
                new WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                        .build();
        List<WifiNetworkSuggestion> suggestionsList = new ArrayList<WifiNetworkSuggestion>();
        suggestionsList.add(suggestion1);
        WifiManager wifiManager = WifiHelper.getInstance().getWifiManager();
        final int status = wifiManager.addNetworkSuggestions(suggestionsList);
        if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            // do error handling here…
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void connectWpa2Network(@NonNull String ssid , String password) {
        final WifiNetworkSuggestion suggestion1 =
                new WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .setWpa2Passphrase(password)
                        .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                        .build();
        List<WifiNetworkSuggestion> suggestionsList = new ArrayList<WifiNetworkSuggestion>();
        suggestionsList.add(suggestion1);
        WifiManager wifiManager = WifiHelper.getInstance().getWifiManager();
        final int status = wifiManager.addNetworkSuggestions(suggestionsList);
        if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            // do error handling here…
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void connectWpa3Network(@NonNull String ssid , String password) {
        final WifiNetworkSuggestion suggestion1 =
                new WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .setWpa3Passphrase(password)
                        .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                        .build();
        List<WifiNetworkSuggestion> suggestionsList = new ArrayList<WifiNetworkSuggestion>();
        suggestionsList.add(suggestion1);
        WifiManager wifiManager = WifiHelper.getInstance().getWifiManager();
        final int status = wifiManager.addNetworkSuggestions(suggestionsList);
        if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            // do error handling here…
        }
    }

}
