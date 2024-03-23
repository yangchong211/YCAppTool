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

import com.yc.appstatuslib.helper.network.NetWorkManager;
import com.yc.appstatuslib.helper.network.OnNetStatusListener;
import com.yc.toolutils.file.SdCardUtils;
import com.yc.networklib.wifi.WifiHelper;
import com.yc.cpu.ApmCpuHelper;
import com.yc.cpu.CpuRateBean;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.localelib.locale.service.LocaleService;
import com.yc.localelib.locale.utils.LocaleToolUtils;
import com.yc.monitorflow.TrafficBean;
import com.yc.monitorflow.FlowHelper;
import com.yc.networklib.net.AddressToolUtils;
import com.yc.networklib.net.AppNetworkUtils;
import com.yc.privateserver.PrivateService;
import com.yc.toolutils.memory.AppMemoryUtils;
import com.yc.toolutils.memory.DalvikHeapMem;
import com.yc.toolutils.memory.PssInfo;
import com.yc.toolutils.memory.RamMemoryInfo;
import com.yc.toolutils.appinfo.AppDeviceUtils;
import com.yc.toolutils.appinfo.AppInfoUtils;
import com.yc.toolutils.sign.AppSignUtils;
import com.yc.toolutils.system.AppSoLibUtils;
import com.yc.toolutils.time.AppTimeUtils;
import com.yc.toolutils.appinfo.AppWindowUtils;
import com.yc.toolutils.bar.StatusBarUtils;

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
    private TextView tvContentCpu;
    private TextView tvContentFlow;
    private TextView tvContentMg;

    @Override
    public void onAttach(@NonNull Context context) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        boolean unregisterNetStatusListener = NetWorkManager.getInstance()
                .unregisterNetStatusListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 处理fragment的返回键逻辑
     */
    private void initBack() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            OnBackPressedDispatcher onBackPressedDispatcher = activity.getOnBackPressedDispatcher();
            onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
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
        tvContentCpu = view.findViewById(R.id.tv_content_cpu);
        tvContentFlow = view.findViewById(R.id.tv_content_flow);
        tvContentMg = view.findViewById(R.id.tv_content_mg);
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
        //获取cpu相关信息
        setCpuInfo();
        //设置流量
        setFlowInfo();
        //获取敏感设备数据
        setMgInfo();
        //注册广播监听
//        initNetStatus();
    }

    private void initNetStatus() {
        NetWorkManager.getInstance().registerNetStatusListener(listener);
    }

    private final OnNetStatusListener listener = new OnNetStatusListener() {
        @Override
        public void onChange(boolean connect, int netType) {
            CharSequence text = tvContentInfo.getText();
            StringBuilder sb = new StringBuilder();
            sb.append(text);
            switch (netType) {
                case 1:
                    sb.append("\n网络状态:  ").append("以太网 ，网络是否可用 ").append(connect);
                    break;
                case 2:
                    //Log.d(TAG, " network Wi-Fi");
                    sb.append("\n网络状态:  ").append("Wi-Fi ，网络是否可用 ").append(connect);
                    break;
                case 3:
                    //Log.d(TAG, " network 移动数据");
                    sb.append("\n网络状态:  ").append("移动数据 ，网络是否可用 ").append(connect);
                    break;
                default:
                    sb.append("\n网络状态:  ").append("无网络 ，网络是否可用 ").append(connect);
                    break;
            }
            tvContentInfo.setText(sb.toString());
        }
    };

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
                AppWindowUtils.copyToClipBoard(activity, sb.toString());
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
        sb.append("wifi信号强度:  ").append(WifiHelper.getInstance().getWifiLevel());
        boolean wifiProxy = AddressToolUtils.isWifiProxy(application);
        if (wifiProxy) {
            sb.append("\nwifi是否代理:  ").append("已经链接代理");
        } else {
            sb.append("\nwifi是否代理:  ").append("未链接代理");
        }
        sb.append("\nMac地址:  ").append(AppDeviceUtils.getMacAddress(application));
        sb.append("\n运营商名称:  ").append(AppNetworkUtils.getNetworkOperatorName());
        sb.append("\n获取内网IP:  ").append(AddressToolUtils.getIpAddress());
        sb.append("\n获取IPv4地址:  ").append(AddressToolUtils.getIPAddress(true));
        sb.append("\n获取IPv6地址:  ").append(AddressToolUtils.getIPAddress(false));
        sb.append("\n移动网络是否打开:  ").append(AppNetworkUtils.isMobileDataEnabled());
        sb.append("\n判断网络是否是4G:  ").append(AppNetworkUtils.is4G());
        sb.append("\nWifi是否打开:  ").append(WifiHelper.getInstance().isWifiEnable());
        sb.append("\nWifi是否连接状态:  ").append(AppNetworkUtils.isWifiConnected());
        sb.append("\nWifi名称:  ").append(WifiHelper.getInstance().getSsid());
        sb.append("\nWifi的Ip地址:  ").append(WifiHelper.getInstance().getWifiList());
        DhcpInfo dhcpInfo = AppDeviceUtils.getDhcpInfo(application);
        if (dhcpInfo != null) {
            //sb.append("\nipAddress：").append(AppDeviceUtils.intToIp(dhcpInfo.ipAddress));
            sb.append("\n子网掩码地址：").append(AppDeviceUtils.intToIp(dhcpInfo.netmask));
            sb.append("\n网关地址：").append(AppDeviceUtils.intToIp(dhcpInfo.gateway));
            sb.append("\nserverAddress：").append(AppDeviceUtils.intToIp(dhcpInfo.serverAddress));
            sb.append("\nDns1：").append(AppDeviceUtils.intToIp(dhcpInfo.dns1));
            sb.append("\nDns2：").append(AppDeviceUtils.intToIp(dhcpInfo.dns2));
        }
        tvContentInfo.setText(sb.toString());
        DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
            @Override
            public void run() {
                sb.append("\n获取域名 ip 地址:  ").append(AddressToolUtils.getDomainAddress("https://baidu.com"));
                sb.append("\n通过域名获取真实ip地址:  ").append(AddressToolUtils.getHostIP("https://baidu.com"));
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContentInfo.setText(sb.toString());
                    }
                });
            }
        });
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
        sb.append("\n系统剩余控件:  ").append(AppMemoryUtils.getRomSpace(application));
        AppMemoryUtils.getMemoryInfo(application, new AppMemoryUtils.OnGetMemoryInfoCallback() {
            @Override
            public void onGetMemoryInfo(String pkgName, int pid, RamMemoryInfo ramMemoryInfo,
                                        PssInfo pssInfo, DalvikHeapMem dalvikHeapMem) {
                sb.append("\n是否低内存状态运行:  ").append(ramMemoryInfo.isLowMemory);
                sb.append("\n可用RAM:  ").append(ramMemoryInfo.availMem);
                sb.append("\n手机总RAM:  ").append(ramMemoryInfo.totalMem);
                sb.append("\n内存占用满的阀值:  ").append(ramMemoryInfo.lowMemThreshold);
                sb.append("\n总的PSS内存使用量:  ").append(pssInfo.totalPss).append("kb");
                sb.append("\ndalvik堆的比例设置大小:  ").append(pssInfo.dalvikPss);
                sb.append("\n本机堆的比例设置大小:  ").append(pssInfo.nativePss);
                sb.append("\n比例设置大小为其他所有:  ").append(pssInfo.otherPss);
                sb.append("\n空闲内存:  ").append(dalvikHeapMem.freeMem);
                sb.append("\n最大内存:  ").append(dalvikHeapMem.maxMem);
                sb.append("\n已用内存:  ").append(dalvikHeapMem.allocated);
                tvContentStorage.setText(sb.toString());
            }
        });
    }

    private void setMemoryInfo() {
        Application application = activity.getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("当前时区:  ").append(AppTimeUtils.getCurrentTimeZone());
        sb.append("\n手机总内存:  ").append(AppMemoryUtils.getTotalMemory(application)).append("kb");
        sb.append("\n当前应用进程的pid:  ").append(AppMemoryUtils.getCurrentPid());
        tvContentMemory.setText(sb.toString());
    }

    /**
     * 获取app中加载的so库
     */
    @SuppressLint("SetTextI18n")
    private void setAppSoInfo() {
        String currSoLoaded = AppSoLibUtils.getCurrSoLoaded();
        if (currSoLoaded.length() == 0) {
            tvContentSo.setText("本App暂无so库");
        } else {
            tvContentSo.setText(currSoLoaded);
        }
    }

    /**
     * 获取cpu相关信息
     */
    private void setCpuInfo() {
        StringBuilder sb = new StringBuilder();
        DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
            @Override
            public void run() {
                sb.append("CPU使用率:  ").append(ApmCpuHelper.getInstance().getCpuRateTop());
                sb.append("\nApp的CPU时间:  ").append(ApmCpuHelper.getInstance().getAppCpuTime());
                sb.append("\n总的CPU时间:  ").append(ApmCpuHelper.getInstance().getTotalCpuTime());
                sb.append("\nTOP计算CPU使用率:  ").append(ApmCpuHelper.getInstance().getCpuRateTop());
                sb.append("\n获取总的CPU使用率:  ").append(ApmCpuHelper.getInstance().getCpuProcRate());
                sb.append("\n当前CPU占比:  ").append(ApmCpuHelper.getInstance().getTempProcCPURate());
                CpuRateBean cpuRateBean = ApmCpuHelper.getInstance().get();
                sb.append("\nCPU占用率:  ").append(cpuRateBean.getProcess());
                sb.append("\nCPU总使用率:  ").append(cpuRateBean.getTotal());
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContentCpu.setText(sb.toString());
                    }
                });
            }
        });
    }

    private void setFlowInfo() {
        FlowHelper.getInstance().setContext(activity);
        StringBuilder sb = new StringBuilder();
        TrafficBean flow = FlowHelper.getInstance().getFlow();
        sb.append("总的下行流量:  ").append(flow.getTotalRxKB());
        sb.append("\n总的上行流量:  ").append(flow.getTotalTxKB());
        sb.append("\n开机时间:  ").append(flow.getBootTime());
        TrafficBean appFlow = FlowHelper.getInstance().getAppFlow();
        sb.append("\nApp的下行流量:  ").append(appFlow.getTotalRxKB());
        sb.append("\nApp的上行流量:  ").append(appFlow.getTotalTxKB());
        TrafficBean allDayMonthMobileInfo = FlowHelper.getInstance().getAllDayMonthMobileInfo();
        if (allDayMonthMobileInfo !=null){
            sb.append("\n所有移动使用流量信息下行流量:  ").append(allDayMonthMobileInfo.getTotalRxKB());
            sb.append("\n所有移动使用流量信息上行流量:  ").append(allDayMonthMobileInfo.getTotalTxKB());
        }
        TrafficBean oneDayMobileInfo = FlowHelper.getInstance().getOneDayMobileInfo();
        if (oneDayMobileInfo !=null){
            sb.append("\n获取所有应用一天使用下行流量:  ").append(oneDayMobileInfo.getTotalRxKB());
            sb.append("\n获取所有应用一天使用上行流量:  ").append(oneDayMobileInfo.getTotalTxKB());
        }
        tvContentFlow.setText(sb.toString());
    }

    private void setMgInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("AndroidId:  ").append(PrivateService.getAndroidId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //获取MEID
            String meid = PrivateService.getMEID();
            sb.append("\nMEID:  ").append(meid);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("\nIMEI1:  ").append(PrivateService.getImei1());
            sb.append("\nIMEI2:  ").append(PrivateService.getImei2());
            sb.append("\nIMEI:  ").append(PrivateService.getImei());
        }
        sb.append("\nSN:  ").append(PrivateService.getSN());
        sb.append("\nDeviceSN:  ").append(PrivateService.getSN());
        sb.append("\n手机运营商:  ").append(PrivateService.getProviderName());
        sb.append("\nSim卡的运营商Id:  ").append(PrivateService.getOperatorId());
        sb.append("\n卡的运营商名称:  ").append(PrivateService.getOperatorName());
        sb.append("\n设备DeviceId:  ").append(PrivateService.getDeviceId());
        System.out.print(sb.toString());
        tvContentMg.setText(sb.toString());
    }
}
