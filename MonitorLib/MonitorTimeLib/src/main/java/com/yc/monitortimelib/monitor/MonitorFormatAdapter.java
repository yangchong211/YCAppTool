package com.yc.monitortimelib.monitor;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 自定义打印接口
 *     revise:
 * </pre>
 */
public interface MonitorFormatAdapter {

    String onFormat(String processName, long startTime);

    final class Factory {
        private Factory() {

        }

        public static MonitorFormatAdapter newDefaultLogAdapter() {
            return new MonitorFormatAdapter() {
                @Override
                public String onFormat(String processName, long monitorTime) {
                    StringBuilder stringBuffer = new StringBuilder();
                    stringBuffer.append("耗时：").append(monitorTime).append("ms");
                    return stringBuffer.toString();
                }
            };
        }
    }
}
