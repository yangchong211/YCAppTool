package com.yc.appmvx;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.appwifilib.WifiHelper;
import com.yc.appwifilib.WifiStateListener;
import com.yc.netreceiver.OnNetStatusListener;
import com.yc.netreceiver.broadcast.NetWorkManager;
import com.yc.netreceiver.callback.NetworkHelper;
import com.yc.networklib.AppNetworkUtils;
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
        WifiHelper.getInstance().registerWifiBroadcast(new WifiStateListener() {
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
        NetWorkManager.getInstance().registerNetStatusListener(new OnNetStatusListener() {
            @Override
            public void onChange(boolean connect, int netType) {
                if (netType == 1){
                    AppLogUtils.d("NetWork-NetReceiver: " + "有网线" + connect);
                } else if (netType == 2){
                    AppLogUtils.d("NetWork-NetReceiver: " + "Wi-Fi " + connect);
                } else if (netType == 3){
                    AppLogUtils.d("NetWork-NetReceiver: " + "手机流量 " + connect);
                } else {
                    AppLogUtils.d("NetWork-NetReceiver: " + "无网络 " + connect);
                }
                showNetInfo();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            NetworkHelper.getInstance().registerNetStatusListener(new OnNetStatusListener() {
//                @Override
//                public void onChange(boolean connect, int netType) {
//                    if (netType == 1){
//                        AppLogUtils.d("NetWork-Callback: " + "有网线" + connect);
//                    } else if (netType == 2){
//                        AppLogUtils.d("NetWork-Callback: " + "Wi-Fi " + connect);
//                    } else if (netType == 3){
//                        AppLogUtils.d("NetWork-Callback: " + "手机流量 " + connect);
//                    } else {
//                        AppLogUtils.d("NetWork-Callback: " + "无网络 " + connect);
//                    }
//                }
//            });
        }
        showNetInfo();
    }

    private void initView() {
        tvNet1 = findViewById(R.id.tv_net_1);
        tvNet2 = findViewById(R.id.tv_net_2);
        tvNet3 = findViewById(R.id.tv_net_3);
        tvNet4 = findViewById(R.id.tv_net_4);
        tvNet1.setVisibility(View.VISIBLE);
        tvNet2.setVisibility(View.VISIBLE);
        tvNet3.setVisibility(View.VISIBLE);
        tvNet4.setVisibility(View.VISIBLE);
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
        if (AppNetworkUtils.isMobileDataEnabled()){
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
        NetWorkManager.getInstance().destroy();
        NetworkHelper.getInstance().destroy();
    }

    @Override
    public void onClick(View v) {
        if (v == tvNet1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                NetWorkUtils.requestNetwork(NetWorkUtils.ETHERNET);
                NetWorkUtils.requestNetwork(NetWorkUtils.MOBILE);
//                NetWorkUtils.requestNetwork(NetWorkUtils.WIFI);
            }
            ToastUtils.showRoundRectToast("查看网络信息");
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
        } else if (v == tvNet4){
            clickConnectWifi();
        }
    }

    private void clickConnectWifi() {
        if (WifiHelper.getInstance().isConnectedTargetSsid("iPhone")){
            ToastUtils.showRoundRectToast("取消热点连接");
        } else {
            ToastUtils.showRoundRectToast("开始热点连接");
            String ssid = "iPhone";
            String pwd = "yc123456";
//            if (!WifiHelper.getInstance().isHaveWifi(ssid)){
//                ToastUtils.showRoundRectToast("没有该热点");
//                return;
//            }
            if (WifiHelper.getInstance().isWifiEnable()){
//                    WifiHelper.getInstance().connectWifi(this,ssid,pwd);
                ToastUtils.showRoundRectToast("开始连接Wi-Fi");
                connect(ssid,pwd);
            } else {
                ToastUtils.showRoundRectToast("开始打开Wi-Fi");
                WifiHelper.getInstance().openWifi();
                tvNet4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (WifiHelper.getInstance().isWifiEnable()){
//                                WifiHelper.getInstance().connectWifi(NetWorkActivity.this,ssid,pwd);
                            connect(ssid,pwd);
                        } else {
                            ToastUtils.showRoundRectToast("wifi打开失败");
                        }
                    }
                },3000);
            }
        }
    }


    private void connect(String ssid , String pwd){
        boolean isSuccess;
        if (TextUtils.isEmpty(pwd)){
            isSuccess = WifiHelper.getInstance().connectOpenNetwork(ssid);
        } else {
            isSuccess = WifiHelper.getInstance().connectWPA2Network(ssid, pwd);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ping = NetWorkUtils.ping();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showRoundRectToast("连接状态"+isSuccess + " -- " );
                    }
                });
            }
        }).start();
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
            sb.append("\n判断是否是以太网：").append(NetWorkUtils.isActivityEthernet());
            sb.append("\n判断是否是Wi-Fi：").append(NetWorkUtils.isActivityWifi());
            sb.append("\n判断是否是移动流量：").append(NetWorkUtils.isActivityCellular());
        }

        WifiHelper wifiHelper = WifiHelper.getInstance();
        sb.append("\n\n下面是Wi-Fi相关工具：");
        sb.append("\n判断 wifi 是否打开：").append(wifiHelper.isWifiEnable());
        sb.append("\n获取wifi的名称：").append(wifiHelper.getSsid());
        sb.append("\n获取被连接网络的mac地址：").append(wifiHelper.getBssid());
        sb.append("\n获取wifi的ip：").append(wifiHelper.getWifiIpStr());
        sb.append("\n获取wifi的强弱：").append(wifiHelper.getWifiState());
        sb.append("\n便携热点是否开启：").append(wifiHelper.isApEnable());
        sb.append("\n是否连接着指定WiFi：").append(wifiHelper.isConnectedTargetSsid("iPhone"));
        sb.append("\n获取开启便携热点后自身热点IP地址：").append(wifiHelper.getLocalIp());

        sb.append("\n\n下面是以太网工具：");
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
