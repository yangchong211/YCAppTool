package com.yc.monitorpinglib;


import com.yc.toolutils.AppLogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAndroidTool
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 直接通过ping命令监测网络
 *     revise: 之前投资界公共库
 * </pre>
 */
public class NetPing {

    /**
     * 回传ping的结果
     */
    LDNetPingListener listener;
    /**
     * 每次ping发送数据包的个数
     */
    private final int _sendCount;

    public NetPing(LDNetPingListener listener, int theSendCount) {
        super();
        this.listener = listener;
        this._sendCount = theSendCount;
    }

    /**
     * 监控NetPing的日志输出到Service
     *
     * @author panghui
     */
    public interface LDNetPingListener {
        void OnNetPingFinished(String log);
    }

    private static final String MATCH_PING_IP = "(?<=from ).*(?=: icmp_seq=1 ttl=)";

    /**
     * 执行ping命令，返回ping命令的全部控制台输出
     *
     * @param ping                              ping
     * @return
     */
    private String execPing(PingTask ping, boolean isNeedL) {
        String cmd = "ping -c ";
        if (isNeedL) {
            cmd = "ping -s 8185 -c  ";
        }
        Process process = null;
        StringBuilder str = new StringBuilder();
        BufferedReader reader = null;
        try {
            String pingCmd = cmd + this._sendCount + " " + ping.getHost();
            AppLogUtils.i("NetPing--------pingCmd---"+pingCmd);
            //比如 ping -c 4 10.3.140.254
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(pingCmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str.toString();
    }

    /**
     * 执行指定host的traceroute
     *
     * @param host                              host
     * @return
     */
    public void exec(String host, boolean isNeedL) {
        PingTask pingTask = new PingTask(host);
        StringBuilder log = new StringBuilder(256);
        String status = execPing(pingTask, isNeedL);
        if (Pattern.compile(MATCH_PING_IP).matcher(status).find()) {
            AppLogUtils.i("NetPing--------status---"+status);
            log.append("\t").append(status);
        } else {
            if (status.length() == 0) {
                log.append("unknown host or network error");
            } else {
                log.append("timeout");
            }
        }
        AppLogUtils.i("NetPing--------exec---"+host);
        String logStr = PingParseTool.getFormattingStr(host, log.toString());
        AppLogUtils.i("NetPing--------logStr---"+logStr);
        this.listener.OnNetPingFinished(logStr);
    }

    /**
     * Ping任务
     */
    private static class PingTask {

        private String host;
        private static final String MATCH_PING_HOST_IP = "(?<=\\().*?(?=\\))";

        public String getHost() {
            return host;
        }

        public PingTask(String host) {
            super();
            this.host = host;
            Pattern p = Pattern.compile(MATCH_PING_HOST_IP);
            Matcher m = p.matcher(host);
            if (m.find()) {
                this.host = m.group();
            }
        }
    }

}
