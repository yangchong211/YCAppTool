package com.yc.networklib;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2016/09/23
 *     desc  : 网络相关工具类
 *     revise:
 * </pre>
 */
public final class IpToolUtils {

    /**
     * 获取内网IP地址的方法
     *
     * @return
     */
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface.getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress();
                        return ip;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }


}
