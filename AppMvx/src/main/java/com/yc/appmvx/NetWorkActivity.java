package com.yc.appmvx;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.appwifilib.WifiHelper;
import com.yc.appwifilib.WifiReceiver;
import com.yc.networklib.AppNetworkUtils;
import com.yc.networklib.NetWorkManager;
import com.yc.networklib.NetWorkUtils;
import com.yc.networklib.NetworkType;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppLogUtils;

public class NetWorkActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNet1;
    private TextView tvNet2;
    private TextView tvNet3;
    private TextView tvNet4;
    private TextView tvContent;
    private boolean isOpenWifi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work);
        initView();
        setWifiText();
        setMobileText();
        WifiHelper.getInstance().registerWifiBroadcast(new WifiReceiver.WifiStateListener() {
            @Override
            public void onWifiOpen() {
                AppLogUtils.d("NetWork-WifiHelper: " + "onWifiOpen");
            }

            @Override
            public void onWifiClose() {
                AppLogUtils.d("NetWork-WifiHelper: " + "onWifiClose");
            }

            @Override
            public void onHotpotOpen() {
                AppLogUtils.d("NetWork-WifiHelper: " + "onHotpotOpen");
            }

            @Override
            public void onHotpotOpenError() {
                AppLogUtils.d("NetWork-WifiHelper: " + "onHotpotOpenError");
            }

            @Override
            public void onHotpotClose() {
                AppLogUtils.d("NetWork-WifiHelper: " + "onHotpotClose");
            }
        });
        NetWorkManager.getInstance().registerNetStatusListener(new NetWorkManager.NetStatusListener() {
            @Override
            public void onChange(boolean connect, int netType) {
                if (netType == 1){
                    AppLogUtils.d("NetWork-NetReceiver: " + "有网线 " + connect);
                } else if (netType == 2){
                    AppLogUtils.d("NetWork-NetReceiver: " + "Wi-Fi " + connect);
                } else if (netType == 3){
                    AppLogUtils.d("NetWork-NetReceiver: " + "手机流量 " + connect);
                }
            }
        });
        showNetInfo();
    }

    private void initView() {
        tvNet1 = findViewById(R.id.tv_net_1);
        tvNet2 = findViewById(R.id.tv_net_2);
        tvNet3 = findViewById(R.id.tv_net_3);
        tvNet4 = findViewById(R.id.tv_net_4);
        tvNet1.setVisibility(View.GONE);
        tvNet2.setVisibility(View.GONE);
        tvNet3.setVisibility(View.GONE);
        tvNet4.setVisibility(View.GONE);
        tvContent = findViewById(R.id.tv_content);
        tvNet1.setOnClickListener(this);
        tvNet2.setOnClickListener(this);
        tvNet3.setOnClickListener(this);
        tvNet4.setOnClickListener(this);
    }

    private void setWifiText(){
        if (WifiHelper.getInstance().isWifiEnable()){
            tvNet2.setText("2.打开Wi-Fi");
        } else {
            tvNet2.setText("2.关闭Wi-Fi");
        }
    }

    private void setMobileText(){
        if (AppNetworkUtils.getMobileDataEnabled()){
            tvNet3.setText("3.打开移动数据");
        } else {
            tvNet3.setText("3.关闭移动数据");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WifiHelper.getInstance().unregisterWifiBroadcast();
        WifiHelper.getInstance().release();
    }

    @Override
    public void onClick(View v) {
        if (v == tvNet1){
            showNetInfo();
        } else if (v == tvNet2){
            if (tvNet2.getText().toString().equals("2.关闭Wi-Fi")){
                WifiHelper.getInstance().openWifi();
                ToastUtils.showRoundRectToast("打开Wi-Fi");
            } else {
                WifiHelper.getInstance().closeWifi();
                ToastUtils.showRoundRectToast("关闭Wi-Fi");
            }
            setWifiText();
        } else if (v == tvNet3){
            if (tvNet3.getText().toString().equals("3.关闭移动数据")){
                AppNetworkUtils.setMobileDataEnabled(true);
                ToastUtils.showRoundRectToast("打开移动数据");
            } else {
                AppNetworkUtils.setMobileDataEnabled(false);
                ToastUtils.showRoundRectToast("关闭移动数据");
            }
            setMobileText();
        }
    }

    private void showNetInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("判断网络是否连接："+AppNetworkUtils.isConnected());
        sb.append("\n判断网络连接后是否可用："+AppNetworkUtils.isAvailable());
        sb.append("\n判断移动数据是否打开："+AppNetworkUtils.getMobileDataEnabled());
        //sb.append("\n判断网络是否是移动数据："+AppNetworkUtils.isMobileData());
        //sb.append("\n判断网络是否是 4G："+AppNetworkUtils.is4G());
        sb.append("\n判断 wifi 是否连接状态："+AppNetworkUtils.isWifiConnected());
        //sb.append("\n获取网络运营商名称："+AppNetworkUtils.getNetworkOperatorName());
        sb.append("\n判断是否是以太网网络："+(AppNetworkUtils.getNetworkType() == NetworkType.NETWORK_ETHERNET));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sb.append("\n判断是否是移动流量："+ NetWorkUtils.isActivityCellular());
            sb.append("\n判断是否是Wi-Fi："+ NetWorkUtils.isActivityWifi());
            sb.append("\n判断是否是以太网："+ NetWorkUtils.isActivityEthernet());
        }

        WifiHelper wifiHelper = WifiHelper.getInstance();
        sb.append("\n\n下面是Wi-Fi相关工具：");
        sb.append("\n判断 wifi 是否打开："+wifiHelper.isWifiEnable());
        sb.append("\n获取wifi的名称："+ wifiHelper.getSsid());
        sb.append("\n获取被连接网络的mac地址："+ wifiHelper.getBssid());
        sb.append("\n获取wifi的ip："+ wifiHelper.getWifiIp());
        sb.append("\n获取wifi的强弱："+ wifiHelper.getWifiState());
        sb.append("\n便携热点是否开启："+ wifiHelper.isApEnable());
        sb.append("\n是否连接着指定WiFi："+ wifiHelper.isConnectedTargetSsid("Tencent-WiFi"));
        sb.append("\n获取开启便携热点后自身热点IP地址："+ wifiHelper.getLocalIp());
        tvContent.setText(sb.toString());
    }

    private void test(){
        //打开或关闭移动数据
        AppNetworkUtils.setMobileDataEnabled(true);
        WifiHelper wifiHelper = WifiHelper.getInstance();
        wifiHelper.closeWifi();
        wifiHelper.openWifi();
        wifiHelper.closeAp(this);
        wifiHelper.openAp(this,"","");
    }

}