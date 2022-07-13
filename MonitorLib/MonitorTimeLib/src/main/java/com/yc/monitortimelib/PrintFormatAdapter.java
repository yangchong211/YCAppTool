package com.yc.monitortimelib;

import java.util.LinkedHashMap;
import java.util.Map;

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
