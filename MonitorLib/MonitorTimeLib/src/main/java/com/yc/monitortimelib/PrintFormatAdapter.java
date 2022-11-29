package com.yc.monitortimelib;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 自定义打印接口
 *     revise:
 * </pre>
 */
public interface PrintFormatAdapter {

    String onFormat(String processName, long startTime);

    final class Factory {
        private Factory() {

        }

        public static PrintFormatAdapter newDefaultLogAdapter() {
            return new PrintFormatAdapter() {
                @Override
                public String onFormat(String processName, long monitorTime) {
                    String rst = processName + " " + monitorTime;
                    return rst;
                }
            };
        }
    }
}
