package com.yc.monitorphone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.DhcpInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yc.appfilelib.SdCardUtils;
import com.yc.localelib.service.LocaleService;
import com.yc.localelib.utils.LocaleToolUtils;
import com.yc.networklib.AppNetworkUtils;
import com.yc.toolmemorylib.AppMemoryUtils;
import com.yc.toolutils.AppDeviceUtils;
import com.yc.toolutils.AppInfoUtils;
import com.yc.toolutils.AppSignUtils;
import com.yc.toolutils.AppSoLibUtils;
import com.yc.toolutils.AppTimeUtils;
import com.yc.toolutils.AppWindowUtils;
import com.yc.toolutils.StatusBarUtils;
import java.util.Arrays;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  :   yangchong211@163.com
 *     time  :   2018/5/6
 *     desc  :   查看手机信息
 *     revise:  
 * </pre>
 */
public class MonitorPhoneFragment extends Fragment {

    private Activity activity;
    private TextView tvPhoneContent;
    private TextView tvAppInfo;
    private TextView tvContentInfo;
    private TextView tvContentLang;
    private TextView tvContentStorage;
    private TextView tvContentMemory;
    private TextView tvContentSo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_all_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFindViewById(view);
        initData();
        initBack();
    }

    /**
     * 处理fragment的返回键逻辑
     */
    private void initBack() {
        FragmentActivity activity = getActivity();
        if (activity != null){
            OnBackPressedDispatcher onBackPressedDispatcher = activity.getOnBackPressedDispatcher();
            onBackPressedDispatcher.addCallback(this,new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    activity.finish();
                }
            });
        }
    }

    private void initFindViewById(View view) {
        tvPhoneContent = view.findViewById(R.id.tv_phone_content);
        tvAppInfo = view.findViewById(R.id.tv_app_info);
        tvContentInfo = view.findViewById(R.id.tv_content_info);
        tvContentLang = view.findViewById(R.id.tv_content_lang);
        tvContentStorage = view.findViewById(R.id.tv_content_storage);
        tvContentMemory = view.findViewById(R.id.tv_content_memory);
        tvContentSo = view.findViewById(R.id.tv_content_so);
    }

    private void initData() {
        initListData();
        //手机设备信息
        setPhoneInfo();
        //设置手机信息
        setAppInfo();
        //本机信息
        //比如mac地址，子网掩码，ip，wifi名称
        setLocationInfo();
        //设置时间和语言
        setTimeAndLang();
        //获取App存储信息
        setStorageInfo();
        //获取App和系统内存信息
        setMemoryInfo();
        //获取app中加载的so库
        setAppSoInfo();
    }

    private void initListData() {

    }

    private void setPhoneInfo() {
        Application application = activity.getApplication();
        final StringBuilder sb = new StringBuilder();
        sb.append("是否root:  ").append(AppDeviceUtils.isDeviceRooted());
        sb.append("\n系统硬件商:  ").append(AppDeviceUtils.getManufacturer());
        sb.append("\n设备的品牌:  ").append(AppDeviceUtils.getBrand());
        sb.append("\n手机的型号:  ").append(AppDeviceUtils.getModel());
        sb.append("\n设备版本号:  ").append(AppDeviceUtils.getId());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String[] cpuType2 = AppDeviceUtils.getCpuType2();
            sb.append("\nCPU的类型:  ").append(Arrays.toString(cpuType2));
        } else {
            sb.append("\nCPU的类型:  ").append(AppDeviceUtils.getCpuType());
        }
        sb.append("\n系统的版本:  ").append(AppDeviceUtils.getSDKVersionName());
        sb.append("\n系统版本值:  ").append(AppDeviceUtils.getSDKVersionCode());
        sb.append("\nSd卡剩余控件:  ").append(SdCardUtils.getSDCardSpace(application));
        sb.append("\n系统剩余控件:  ").append(AppMemoryUtils.getRomSpace(application));
        sb.append("\n手机分辨率:  ").append(AppWindowUtils.getRealScreenHeight(activity))
                .append("x").append(AppWindowUtils.getRealScreenWidth(activity));
        sb.append("\n屏幕尺寸:  ").append(AppWindowUtils.getScreenInch(activity));
        sb.append("\n屏幕密度:  ").append(AppWindowUtils.getScreenDensity());
        sb.append("\n屏幕密度DPI:  ").append(AppWindowUtils.getScreenDensityDpi());
        sb.append("\n状态栏高度:  ").append(StatusBarUtils.getStatusBarHeight(activity)).append("px");
        sb.append("\n导航栏高度:  ").append(StatusBarUtils.getNavigationBarHeight(activity)).append("px");
        sb.append("\nAndroidID:  ").append(AppDeviceUtils.getAndroidID(application));
        tvPhoneContent.setText(sb.toString());
        tvPhoneContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppWindowUtils.copyToClipBoard(activity,sb.toString());
            }
        });
    }


    private void setAppInfo() {
        Application application = activity.getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("软件App包名:  ").append(AppInfoUtils.getAppPackageName());
        sb.append("\n是否是DEBUG版本:  ").append(AppInfoUtils.isAppDebug());
        sb.append("\nApp名称:  ").append(AppInfoUtils.getAppName());
        sb.append("\nApp签名:  ").append(AppSignUtils.getPackageSign());
        sb.append("\n版本名称:  ").append(AppInfoUtils.getAppVersionName());
        sb.append("\n版本号:  ").append(AppInfoUtils.getAppVersionCode());
        sb.append("\n是否是系统App:  ").append(AppInfoUtils.isSystemApp());
        ApplicationInfo applicationInfo = application.getApplicationInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sb.append("\n最低系统版本号:  ").append(applicationInfo.minSdkVersion);
            sb.append("\n当前系统版本号:  ").append(applicationInfo.targetSdkVersion);
            sb.append("\n进程名称:  ").append(applicationInfo.processName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sb.append("\nUUID:  ").append(applicationInfo.storageUuid);
            }
            sb.append("\nAPK完整路径:  ").append(AppInfoUtils.getAppPath());
            sb.append("\n备份代理:  ").append(applicationInfo.backupAgentName);
            sb.append("\nclass名称:  ").append(applicationInfo.className);
            boolean profileableByShell;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                profileableByShell = applicationInfo.isProfileableByShell();
                sb.append("\n是否在分析模式:  ").append(profileableByShell);
            }
        }
        tvAppInfo.setText(sb.toString());
    }


    private void setLocationInfo() {
        Application application = activity.getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("wifi信号强度:  ").append(AppNetworkUtils.getWifiState());
        boolean wifiProxy = AppNetworkUtils.isWifiProxy(application);
        if (wifiProxy){
            sb.append("\nwifi是否代理:  ").append("已经链接代理");
        } else {
            sb.append("\nwifi是否代理:  ").append("未链接代理");
        }
        sb.append("\nMac地址:  ").append(AppDeviceUtils.getMacAddress(application));
        sb.append("\n运营商名称:  ").append(AppNetworkUtils.getNetworkOperatorName());
        sb.append("\n获取IPv4地址:  ").append(AppNetworkUtils.getIPAddress(true));
        sb.append("\n获取IPv6地址:  ").append(AppNetworkUtils.getIPAddress(false));
        sb.append("\n移动网络是否打开:  ").append(AppNetworkUtils.getMobileDataEnabled());
        sb.append("\n判断网络是否是4G:  ").append(AppNetworkUtils.is4G());
        sb.append("\nWifi是否打开:  ").append(AppNetworkUtils.getWifiEnabled());
        sb.append("\nWifi是否连接状态:  ").append(AppNetworkUtils.isWifiConnected());
        sb.append("\nWifi名称:  ").append(AppNetworkUtils.getWifiName(application));
        int wifiIp = AppNetworkUtils.getWifiIp(application);
        String ip = AppDeviceUtils.intToIp(wifiIp);
        sb.append("\nWifi的Ip地址:  ").append(ip);
        DhcpInfo dhcpInfo = AppDeviceUtils.getDhcpInfo(application);
        if (dhcpInfo!=null){
            //sb.append("\nipAddress：").append(AppDeviceUtils.intToIp(dhcpInfo.ipAddress));
            sb.append("\n子网掩码地址：").append(AppDeviceUtils.intToIp(dhcpInfo.netmask));
            sb.append("\n网关地址：").append(AppDeviceUtils.intToIp(dhcpInfo.gateway));
            sb.append("\nserverAddress：").append(AppDeviceUtils.intToIp(dhcpInfo.serverAddress));
            sb.append("\nDns1：").append(AppDeviceUtils.intToIp(dhcpInfo.dns1));
            sb.append("\nDns2：").append(AppDeviceUtils.intToIp(dhcpInfo.dns2));
        }
        tvContentInfo.setText(sb.toString());
    }

    /**
     * 设置时间和语言
     */
    private void setTimeAndLang() {
        Application application = activity.getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("当前时区:  ").append(AppTimeUtils.getCurrentTimeZone());
        Locale locale = LocaleService.getInstance().getCurrentLocale();
        if (locale != null) {
            sb.append("\nApp当前语言:  ").append(locale.getDisplayLanguage());
            sb.append("\nApp当前地区:  ").append(locale.getCountry());
            sb.append("\nApp区域设置的名:  ").append(locale.getDisplayName());
            sb.append("\nAppLocale的tag:  ").append(LocaleToolUtils.localeToTag(locale));
        }
        Locale systemLocale = LocaleService.getInstance().getSystemLocale();
        if (systemLocale != null) {
            sb.append("\n系统当前语言:  ").append(systemLocale.getDisplayLanguage());
            sb.append("\n系统当前地区:  ").append(systemLocale.getCountry());
            sb.append("\n系统区域设置的名:  ").append(systemLocale.getDisplayName());
            sb.append("\n系统Locale的tag:  ").append(LocaleToolUtils.localeToTag(systemLocale));
        }
        tvContentLang.setText(sb.toString());
    }

    private void setStorageInfo() {
        Application application = activity.getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("当前时区:  ").append(AppTimeUtils.getCurrentTimeZone());
        sb.append("\n手机总内存:  ").append(AppMemoryUtils.getTotalMemory(application)).append("kb");
        sb.append("\n手机可用内存:  ").append(AppMemoryUtils.getAvailMemory(application)).append("mb");
        tvContentStorage.setText(sb.toString());
    }

    private void setMemoryInfo() {
        Application application = activity.getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("当前时区:  ").append(AppTimeUtils.getCurrentTimeZone());
        sb.append("\n手机总内存:  ").append(AppMemoryUtils.getTotalMemory(application)).append("kb");
        sb.append("\n手机可用内存:  ").append(AppMemoryUtils.getAvailMemory(application)).append("mb");
        tvContentMemory.setText(sb.toString());
    }

    /**
     * 获取app中加载的so库
     */
    @SuppressLint("SetTextI18n")
    private void setAppSoInfo() {
        String currSoLoaded = AppSoLibUtils.getCurrSoLoaded();
        if (currSoLoaded.length() == 0){
            tvContentSo.setText("本App暂无so库");
        } else {
            tvContentSo.setText(currSoLoaded);
        }
    }

}
