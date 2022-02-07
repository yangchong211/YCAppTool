package com.yc.netlib.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.netlib.BuildConfig;
import com.yc.netlib.R;
import com.yc.netlib.connect.ConnectionManager;
import com.yc.netlib.connect.ConnectionQuality;
import com.yc.netlib.connect.ConnectionStateChangeListener;
import com.yc.netlib.connect.DeviceBandwidthSampler;
import com.yc.netlib.data.IDataPoolHandleImpl;
import com.yc.netlib.data.NetworkFeedBean;
import com.yc.netlib.ping.PingView;
import com.yc.netlib.utils.NetDeviceUtils;
import com.yc.netlib.utils.NetLogUtils;
import com.yc.netlib.utils.NetWorkUtils;
import com.yc.netlib.utils.NetworkTool;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.yc.netlib.connect.ConnectionQuality.UNKNOWN;

public class NetRequestPhoneFragment extends Fragment {

    private Activity activity;
    private TextView mTvBandWidth;
    private TextView tvPhoneContent;
    private TextView mTvAppInfo;
    private TextView tvContentInfo;
    private TextView tvWebInfo;
    private PingView tvNetInfo;
    private List<NetworkFeedBean> mNetworkFeedList;
    private static final int MESSAGE = 1;
    private ConnectionManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;
    private ConnectionQuality mConnectionClass = UNKNOWN;
    //网上弄的一个图片
    private final String mURL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601463169772&di=80c295c40c3c236a6434a5c66cb84c41&imgtype=0&src=http%3A%2F%2Fimg1.kchuhai.com%2Felite%2F20200324%2Fhead20200324162648.jpg";
    private int mTries = 0;
    private boolean isSetCon = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE){
                String content = (String) msg.obj;
                tvWebInfo.setText(content);
            }
        }
    };


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_phone_info, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFindViewById(view);
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mConnectionClassManager.remove(mListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConnectionClassManager.register(mListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectionClassManager = ConnectionManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mListener = new ConnectionChangedListener();
        mConnectionClassManager.reset();
        isSetCon = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDeviceBandwidthSampler!=null){
            mDeviceBandwidthSampler.stopSampling();
        }
    }


    private void initFindViewById(View view) {
        mTvBandWidth = view.findViewById(R.id.tv_band_width);
        tvPhoneContent = view.findViewById(R.id.tv_phone_content);
        mTvAppInfo = view.findViewById(R.id.tv_app_info);
        tvContentInfo = view.findViewById(R.id.tv_content_info);
        tvWebInfo = view.findViewById(R.id.tv_web_info);
        tvNetInfo = view.findViewById(R.id.tv_net_info);
        mTvBandWidth.setText("网络宽带:测试中……");
        mTvBandWidth.postDelayed(new Runnable() {
            @Override
            public void run() {
                new DownloadImage().execute(mURL);
            }
        },100);
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
        //服务端信息
        //也就是host，域名ip，域名mac，域名是否正常，是ipv4还是ipv6
        setNetInfo();
        //网络诊断，也就是ping一下
        setPingInfo();
    }

    private void initListData() {
        mNetworkFeedList = new ArrayList<>();
        HashMap<String, NetworkFeedBean> networkFeedMap = IDataPoolHandleImpl.getInstance().getNetworkFeedMap();
        if (networkFeedMap != null) {
            Collection<NetworkFeedBean> values = networkFeedMap.values();
            mNetworkFeedList.addAll(values);
            try {
                Collections.sort(mNetworkFeedList, new Comparator<NetworkFeedBean>() {
                    @Override
                    public int compare(NetworkFeedBean networkFeedModel1, NetworkFeedBean networkFeedModel2) {
                        return (int) (networkFeedModel2.getCreateTime() - networkFeedModel1.getCreateTime());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setPhoneInfo() {
        Application application = NetworkTool.getInstance().getApplication();
        final StringBuilder sb = new StringBuilder();
        sb.append("是否root:").append(NetDeviceUtils.isDeviceRooted());
        sb.append("\n系统硬件商:").append(NetDeviceUtils.getManufacturer());
        sb.append("\n设备的品牌:").append(NetDeviceUtils.getBrand());
        sb.append("\n手机的型号:").append(NetDeviceUtils.getModel());
        sb.append("\n设备版本号:").append(NetDeviceUtils.getId());
        sb.append("\nCPU的类型:").append(NetDeviceUtils.getCpuType());
        sb.append("\n系统的版本:").append(NetDeviceUtils.getSDKVersionName());
        sb.append("\n系统版本值:").append(NetDeviceUtils.getSDKVersionCode());
        sb.append("\nSd卡剩余控件:").append(NetDeviceUtils.getSDCardSpace(application));
        sb.append("\n系统剩余控件:").append(NetDeviceUtils.getRomSpace(application));
        sb.append("\n手机总内存:").append(NetDeviceUtils.getTotalMemory(application));
        sb.append("\n手机可用内存:").append(NetDeviceUtils.getAvailMemory(application));
        sb.append("\n手机分辨率:").append(NetDeviceUtils.getWidthPixels(application))
                .append("x").append(NetDeviceUtils.getRealHeightPixels(application));
        sb.append("\n屏幕尺寸:").append(NetDeviceUtils.getScreenInch(activity));
        tvPhoneContent.setText(sb.toString());
        tvPhoneContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWorkUtils.copyToClipBoard(activity,sb.toString());
            }
        });
    }


    private void setAppInfo() {
        Application application = NetworkTool.getInstance().getApplication();
        //版本信息
        String versionName = "";
        String versionCode = "";
        try {
            PackageManager pm = application.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(application.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = String.valueOf(pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("软件App包名:").append(NetworkTool.getInstance().getApplication().getPackageName());
        sb.append("\n是否是DEBUG版本:").append(BuildConfig.BUILD_TYPE);
        if (versionName!=null && versionName.length()>0){
            sb.append("\n版本名称:").append(versionName);
            sb.append("\n版本号:").append(versionCode);
        }
        ApplicationInfo applicationInfo = application.getApplicationInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sb.append("\n最低系统版本号:").append(applicationInfo.minSdkVersion);
            sb.append("\n当前系统版本号:").append(applicationInfo.targetSdkVersion);
            sb.append("\n进程名称:").append(applicationInfo.processName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sb.append("\nUUID:").append(applicationInfo.storageUuid);
            }
            sb.append("\nAPK完整路径:").append(applicationInfo.sourceDir);
        }
        mTvAppInfo.setText(sb.toString());
    }


    private void setLocationInfo() {
        Application application = NetworkTool.getInstance().getApplication();
        StringBuilder sb = new StringBuilder();
        sb.append("wifi信号强度:").append(NetDeviceUtils.getWifiState(application));
        sb.append("\nAndroidID:").append(NetDeviceUtils.getAndroidID(application));
        boolean wifiProxy = NetWorkUtils.isWifiProxy(application);
        if (wifiProxy){
            sb.append("\nwifi是否代理:").append("已经链接代理");
        } else {
            sb.append("\nwifi是否代理:").append("未链接代理");
        }
        sb.append("\nMac地址:").append(NetDeviceUtils.getMacAddress(application));
        sb.append("\nWifi名称:").append(NetDeviceUtils.getWifiName(application));
        int wifiIp = NetDeviceUtils.getWifiIp(application);
        String ip = NetDeviceUtils.intToIp(wifiIp);
        sb.append("\nWifi的Ip地址:").append(ip);
        DhcpInfo dhcpInfo = NetDeviceUtils.getDhcpInfo(application);
        if (dhcpInfo!=null){
            //sb.append("\nipAddress：").append(NetDeviceUtils.intToIp(dhcpInfo.ipAddress));
            sb.append("\n子网掩码地址：").append(NetDeviceUtils.intToIp(dhcpInfo.netmask));
            sb.append("\n网关地址：").append(NetDeviceUtils.intToIp(dhcpInfo.gateway));
            sb.append("\nserverAddress：").append(NetDeviceUtils.intToIp(dhcpInfo.serverAddress));
            sb.append("\nDns1：").append(NetDeviceUtils.intToIp(dhcpInfo.dns1));
            sb.append("\nDns2：").append(NetDeviceUtils.intToIp(dhcpInfo.dns2));
        }
        tvContentInfo.setText(sb.toString());
    }

    private void setNetInfo() {
        if (mNetworkFeedList.size()>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String curl = mNetworkFeedList.get(0).getCURL();
                    String host = Uri.parse(curl).getHost();
                    StringBuilder sb = new StringBuilder();
                    sb.append("域名ip地址:").append(NetDeviceUtils.getHostIP(host));
                    sb.append("\n域名host名称:").append(NetDeviceUtils.getHostName(host));
                    sb.append("\n待完善:").append("获取服务端ip，mac，是ipv4还是ipv6等信息");
                    String string = sb.toString();
                    Message message = new Message();
                    message.what = MESSAGE;
                    message.obj = string;
                    handler.sendMessage(message);
                }
            }).start();
        }
    }

    private void setPingInfo() {
        Application application = NetworkTool.getInstance().getApplication();
        tvNetInfo.setDeviceId(NetDeviceUtils.getAndroidID(application));
        tvNetInfo.setUserId(application.getPackageName());
        //版本信息
        String versionName = "";
        try {
            PackageManager pm = application.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(application.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            if (pi != null) {
                versionName = pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvNetInfo.setVersionName(versionName);
        if (mNetworkFeedList.size()>0){
            String curl = mNetworkFeedList.get(0).getCURL();
            String host = Uri.parse(curl).getHost();
            tvNetInfo.pingHost(host);
        }
    }

    /**
     * 侦听器在connectionclass更改时更新UI
     */
    private class ConnectionChangedListener implements ConnectionStateChangeListener {
        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            setConnText(mConnectionClass);
        }
    }


    private void setConnText(final ConnectionQuality mConnectionClass) {
        if (isSetCon){
            return;
        }
        isSetCon = true;
        mTvBandWidth.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                NetLogUtils.e("DownloadImage-----onBandwidthStateChange----"+mConnectionClass);
                StringBuffer sb = new StringBuffer();
                sb.append("网络宽带:");
                if (mConnectionClass==ConnectionQuality.UNKNOWN){
                    sb.append("未知带宽");
                } else if (mConnectionClass==ConnectionQuality.EXCELLENT){
                    sb.append("带宽超过2000kbps");
                } else if (mConnectionClass==ConnectionQuality.GOOD){
                    sb.append("带宽在550到2000kbps之间");
                } else if (mConnectionClass==ConnectionQuality.MODERATE){
                    sb.append("带宽在150到550kbps之间");
                } else if (mConnectionClass==ConnectionQuality.POOR){
                    sb.append("带宽小于150kbps");
                } else {
                    sb.append("未知带宽");
                }
                double downloadKBitsPerSecond = ConnectionManager.getInstance().getDownloadKBitsPerSecond();
                sb.append("\n平均宽带值:").append(downloadKBitsPerSecond);
                mTvBandWidth.setText(sb.toString());
            }
        },300);
    }


    private class DownloadImage extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            mDeviceBandwidthSampler.startSampling();
            NetLogUtils.e("DownloadImage-----onPreExecute-----开始");
        }

        @Override
        protected Void doInBackground(String... url) {
            String imageURL = url[0];
            try {
                URLConnection connection = new URL(imageURL).openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStream input = connection.getInputStream();
                try {
                    byte[] buffer = new byte[1024];
                    while (input.read(buffer) != -1) {

                    }
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                NetLogUtils.e("Error while downloading image.");
            } finally {
                NetLogUtils.e("DownloadImage-----doInBackground-----");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mDeviceBandwidthSampler.stopSampling();
            // 重试10次，直到我们找到一个ConnectionClass
            if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                mTries++;
                NetLogUtils.e("DownloadImage-----onPostExecute-----"+mTries);
                new DownloadImage().execute(mURL);
            }
            if (mTries==10){
                ConnectionQuality quality = ConnectionManager.getInstance().getCurrentBandwidthQuality();
                setConnText(quality);
            }
        }
    }
}
