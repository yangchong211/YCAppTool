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
import com.yc.networklib.AppNetworkUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toolutils.AppDeviceUtils;
import com.yc.toolutils.AppInfoUtils;


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
                //URL Scheme协议格式：
                //String urlStr = "http://www.orangecpp.com:80/tucao?id=hello&name=lily";
                //url =           protocol + authority(host + port) + path + query
                //协议protocol=    http
                //域名authority=   www.orangecpp.com:80
                //页面path=        /tucao
                //参数query=       id=hello&name=lily
                //authority =      host + port
                //主机host=        www.orangecpp.com
                //端口port=        80

                Uri uri = Uri.parse(url);
                // 完整的url信息
                String urlStr = uri.toString();
                String host = uri.getHost();
                int port = uri.getPort();
                String scheme = uri.getScheme();
                String path = uri.getPath();
                String query = uri.getQuery();
                StringBuilder sb = new StringBuilder();
                //获取此URI的解码权限部分。对于服务器地址，权限的结构如下：Examples: "google.com", "bob@google.com:80"
                String authority = uri.getAuthority();
                //从权限获取已解码的用户信息。例如，如果权限为“任何人@google.com”，此方法将返回“任何人”。
                String userInfo = uri.getUserInfo();
                sb.append("完整的url信息:").append(urlStr);
                sb.append("\n域名ip地址:").append(AppNetworkUtils.getHostIP(host));
                sb.append("\n域名host名称:").append(AppNetworkUtils.getHostName(host));
                sb.append("\n域名port端口号:").append(port);
                sb.append("\n域名端口号:").append(port);
                sb.append("\n域名scheme:").append(scheme);
                sb.append("\n域名path路径:").append(path);
                sb.append("\n域名Query部分:").append(query);
                sb.append("\n域名authority解码:").append(authority);
                sb.append("\n域名用户信息:").append(userInfo);
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
