package com.yc.monitorpinglib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toolutils.AppDeviceUtils;
import com.yc.toolutils.AppInfoUtils;
import com.yc.toolutils.net.AppNetworkUtils;


/**
 * <pre>
 *     @author yangchong
 *     email  :   yangchong211@163.com
 *     time  :   2018/5/6
 *     desc  :   ping
 *     revise:
 * </pre>
 */
public class MonitorPingFragment extends Fragment {

    private PingView tvNetInfo;
    private TextView tvWebInfo;
    private String url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ping_all_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFindViewById(view);

        initData();
    }

    private void initFindViewById(View view) {
        tvWebInfo = view.findViewById(R.id.tv_web_info);
        tvNetInfo = view.findViewById(R.id.tv_net_info);
    }

    private void initData() {
        //网络诊断，也就是ping一下
        Context context = getActivity();
        if (context == null || url == null) {
            return;
        }
        //服务端信息
        //也就是host，域名ip，域名mac，域名是否正常，是ipv4还是ipv6
        DelegateTaskExecutor.getInstance().executeOnCore(new Runnable() {
            @Override
            public void run() {
                String host = Uri.parse(url).getHost();
                StringBuilder sb = new StringBuilder();
                sb.append("域名ip地址:").append(AppNetworkUtils.getHostIP(host));
                sb.append("\n域名host名称:").append(AppNetworkUtils.getHostName(host));
                sb.append("\n待完善:").append("获取服务端ip，mac，是ipv4还是ipv6等信息");
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        String string = sb.toString();
                        tvWebInfo.setText(string);
                    }
                });
            }
        });

        tvNetInfo.setDeviceId(AppDeviceUtils.getAndroidID(context));
        tvNetInfo.setUserId(context.getPackageName());
        tvNetInfo.setVersionName(AppInfoUtils.getAppVersionName());
        String host = Uri.parse(url).getHost();
        tvNetInfo.pingHost(host);
    }


}
