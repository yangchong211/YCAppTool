package com.yc.appmvx;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.appwifilib.WifiHelper;
import com.yc.appwifilib.WifiToolUtils;
import com.yc.netreceiver.OnNetStatusListener;
import com.yc.netreceiver.NetWorkManager;
import com.yc.netsettinglib.DefaultNetCallback;
import com.yc.netsettinglib.NetRequestHelper;
import com.yc.netsettinglib.CapabilitiesUtils;
import com.yc.nettestlib.NetTestHelper;
import com.yc.nettestlib.OnNetTestListener;
import com.yc.networklib.AppNetworkUtils;
import com.yc.networklib.NetworkType;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetWorkActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNet1;
    private TextView tvWifiOpen;
    private TextView tvWifiScan;
    private TextView tvWifiOther;
    private RecyclerView recyclerView;
    private TextView tvContent;
    /**
     * Wifi结果列表
     */
    private final List<ScanResult> wifiList = new ArrayList<>();
    private WifiAdapter wifiAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work);
        initView();
        initNetStatus();
        showNetInfo();
        initRecyclerView();
        initWifi();
    }

    private void initView() {
        tvNet1 = findViewById(R.id.tv_net_1);
        recyclerView = findViewById(R.id.recycle_view);
        tvContent = findViewById(R.id.tv_content);
        tvWifiOpen = findViewById(R.id.tv_open_wifi);
        tvWifiScan = findViewById(R.id.tv_scan_wifi);
        tvWifiOther = findViewById(R.id.tv_wifi_other);
        tvNet1.setOnClickListener(this);
        tvWifiOpen.setOnClickListener(this);
        tvWifiScan.setOnClickListener(this);
        tvWifiOther.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WifiHelper.getInstance().unregisterWifiBroadcast();
        WifiHelper.getInstance().release();
        NetWorkManager.getInstance().destroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetRequestHelper.getInstance().destroy();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClick(View v) {
        if (v == tvNet1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetRequestHelper.getInstance().requestNetwork(NetRequestHelper.MOBILE);
            }
            ToastUtils.showRoundRectToast("切换默认网络");
        } else if (v == tvWifiOpen) {
            WifiHelper.getInstance().openWifi();
            Log.i("Network-Receiver" , "openWifi ");
        } else if (v == tvWifiScan) {
            if (!WifiHelper.getInstance().isWifiEnable()) {
                return;
            }
            boolean startScan = WifiHelper.getInstance().startScan();
            Log.i("Network-Receiver" , "startScan " + startScan);
        } else if (v == tvWifiOther) {
            WifiHelper.getInstance().closeWifi();
            wifiList.clear();
            wifiAdapter.notifyDataSetChanged();
            showNetInfo();
        }
    }

    private void initNetStatus() {
        NetWorkManager.getInstance().registerNetStatusListener(new OnNetStatusListener() {
            @Override
            public void onChange(boolean connect, int netType) {
                if (netType == 1) {
                    AppLogUtils.d("NetWork-NetReceiver: " + "有网线" + connect);
                } else if (netType == 2) {
                    AppLogUtils.d("NetWork-NetReceiver: " + "Wi-Fi " + connect);
                } else if (netType == 3) {
                    AppLogUtils.d("NetWork-NetReceiver: " + "手机流量 " + connect);
                } else {
                    AppLogUtils.d("NetWork-NetReceiver: " + "无网络 " + connect);
                }
                showNetInfo();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetRequestHelper.getInstance().registerNetStatusListener(new DefaultNetCallback() {
                @Override
                public void onDefaultChange(boolean available, String netType) {
                    AppLogUtils.d("NetWork-Default: " + netType + " , " + available);
                }
            });
            NetRequestHelper.getInstance().registerDefaultNetworkCallback();
        }
    }

    private void showNetInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("判断网络是否连接：").append(AppNetworkUtils.isConnected());
        sb.append("\n判断网络连接后是否可用：").append(AppNetworkUtils.isAvailable());
        sb.append("\n判断移动数据是否打开：").append(AppNetworkUtils.isMobileDataEnabled());
        sb.append("\n判断移动数据是否是可用：").append(AppNetworkUtils.isMobileAvailable());
        sb.append("\n判断网络是否是 4G：").append(AppNetworkUtils.is4G());
        sb.append("\n判断 wifi 是否连接状态：").append(AppNetworkUtils.isWifiConnected());
        sb.append("\n判断 wifi 是否可用状态：").append(AppNetworkUtils.isWifiAvailable());
        sb.append("\n获取网络运营商名称：").append(AppNetworkUtils.getNetworkOperatorName());
        sb.append("\n判断是否是以太网网络：").append(AppNetworkUtils.getNetworkType() == NetworkType.NETWORK_ETHERNET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sb.append("\n判断是否是以太网：").append(CapabilitiesUtils.isActivityEthernet());
            sb.append("\n判断是否是Wi-Fi：").append(CapabilitiesUtils.isActivityWifi());
            sb.append("\n判断是否是移动流量：").append(CapabilitiesUtils.isActivityCellular());
        }

        WifiHelper wifiHelper = WifiHelper.getInstance();
        sb.append("\n\n下面是Wi-Fi相关工具：");
        sb.append("\n判断 wifi 是否打开：").append(wifiHelper.isWifiEnable());
        sb.append("\n获取wifi的名称：").append(wifiHelper.getSsid());
        sb.append("\n获取被连接网络的mac地址：").append(wifiHelper.getBssid());
        sb.append("\n获取wifi的ip：").append(wifiHelper.getWifiIpStr());
        sb.append("\n获取wifi的强弱：").append(wifiHelper.getWifiLevel());
        sb.append("\n便携热点是否开启：").append(wifiHelper.isApEnable());
        sb.append("\n是否连接着指定WiFi：").append(wifiHelper.isConnectedTargetSsid("iPhone"));
        sb.append("\n获取开启便携热点后自身热点IP地址：").append(wifiHelper.getLocalIp());

        sb.append("\n\n下面是网络Wi-Fi工具：");
        tvContent.setText(sb.toString());
    }


    private void initWifi() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
    }


    private void initRecyclerView() {
        //配置适配器
        wifiAdapter = new WifiAdapter(wifiList);
        //Item点击事件
        wifiAdapter.setOnItemClickListener(new WifiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (wifiList.size() > 0 && wifiList.size() > position){
                    ScanResult scanResult = wifiList.get(position);
                    //获取Wifi扫描结果
                    String capabilities = scanResult.capabilities;
                    //Wifi状态标识 true：加密，false：开放
                    boolean wifiStateFlag = capabilities.contains("WEP") || capabilities.contains("PSK") || capabilities.contains("EAP");
                    if (wifiStateFlag) {
                        Log.i("Network-Connect" , "connectWifi: 加密连接");
                        clickConnectWifi(scanResult.SSID , "yc123456");
                    } else {
                        Log.i("Network-Connect" , "connectWifi: 非加密连接");
                        clickConnectWifi(scanResult.SSID , "");
                    }
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wifiAdapter);
    }

    public static class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {

        private final List<ScanResult> lists;
        private OnItemClickListener listener;

        public WifiAdapter(List<ScanResult> lists) {
            this.lists = lists;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public WifiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_wifi_rv, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(viewHolder.getAdapterPosition());
                    }
                }
            });
            return viewHolder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull WifiAdapter.ViewHolder holder, int position) {
            //Wifi名称
            String ssid = lists.get(position).SSID;
            holder.tvWifiName.setText(ssid);
            //Wifi功能
            String capabilities = lists.get(position).capabilities;
            //Wifi状态标识 true：加密，false：开放
            boolean wifiStateFlag = capabilities.contains("WEP") || capabilities.contains("PSK") || capabilities.contains("EAP");
            //Wifi状态描述
            String wifiState = wifiStateFlag ? "加密" : "开放";
            holder.tvWifiStatus.setText(wifiState);
            //信号强度
            int imgLevel;
            int level = lists.get(position).level;
            if (level <= 0 && level >= -50) {
                imgLevel = 5;
            } else if (level < -50 && level >= -70) {
                imgLevel = 4;
            } else if (level < -70 && level >= -80) {
                imgLevel = 3;
            } else if (level < -80 && level >= -100) {
                imgLevel = 2;
            } else {
                imgLevel = 1;
            }
            holder.tvWifiLevel.setText(imgLevel + "等级");
        }

        @Override
        public int getItemCount() {
            return lists == null ? 0 : lists.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvWifiName;
            TextView tvWifiStatus;
            TextView tvWifiLevel;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvWifiName = itemView.findViewById(R.id.tv_wifi_name);
                tvWifiStatus = itemView.findViewById(R.id.tv_wifi_state);
                tvWifiLevel = itemView.findViewById(R.id.tv_signal);
            }
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }

    /**
     * Wifi扫描广播接收器
     */
    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            //处理扫描结果
            wifiList.clear();
            List<ScanResult> wifiListData = WifiHelper.getInstance().getWifiList();
            List<ScanResult> scanResults = WifiToolUtils.removeDuplicate(wifiListData);
            Log.i("Network-Receiver" , "wifi" + success + " , "
                    + wifiListData.size() + " , " + scanResults.size());
            wifiList.addAll(wifiListData);
            //根据Level排序
            Collections.sort(wifiList, (lhs, rhs) -> rhs.level - lhs.level);
            if (wifiAdapter != null) {
                wifiAdapter.notifyDataSetChanged();
            }
        }
    };


    private void clickConnectWifi(String ssid, String pwd) {
        if (WifiHelper.getInstance().isWifiEnable()) {
            ToastUtils.showRoundRectToast("开始连接Wi-Fi");
            connect(ssid, pwd);
        } else {
            ToastUtils.showRoundRectToast("开始打开Wi-Fi");
            WifiHelper.getInstance().openWifi();
        }
    }


    private void connect(String ssid, String pwd) {
        boolean isSuccess;
        if (TextUtils.isEmpty(pwd)) {
            isSuccess = WifiHelper.getInstance().connectOpenNetwork(ssid);
        } else {
            isSuccess = WifiHelper.getInstance().connectWPA2Network(ssid, pwd);
        }
        Log.i("Network-Connect" , "wifi : " + ssid + " , " + isSuccess);
        // 如果连接失败，再试一次
        if (!isSuccess) {
            if (TextUtils.isEmpty(pwd)) {
                isSuccess = WifiHelper.getInstance().connectOpenNetwork(ssid);
            } else {
                isSuccess = WifiHelper.getInstance().connectWPA2Network(ssid, pwd);
            }
        }
        Log.i("Network-Connect" , "again wifi : " + ssid + " , " + isSuccess);
        // 无论是否成功，都发请求确认一下
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NetTestHelper.getInstance().testNetwork(new OnNetTestListener() {
                    @Override
                    public void onTest(boolean success, String msg) {
                        Log.i("Network-Connect" , "testNetwork : " + success + " , " + msg);
                    }
                });
            }
        }).start();
    }


    private void test() {
        //打开或关闭移动数据
        AppNetworkUtils.setMobileDataEnabled(true);
        WifiHelper wifiHelper = WifiHelper.getInstance();
        wifiHelper.closeWifi();
        wifiHelper.openWifi();
        wifiHelper.closeAp(this);
        wifiHelper.openAp(this, "", "");
    }

}
