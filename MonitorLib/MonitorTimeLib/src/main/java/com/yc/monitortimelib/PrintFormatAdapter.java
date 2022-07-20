package com.yc.monitortimelib;

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
