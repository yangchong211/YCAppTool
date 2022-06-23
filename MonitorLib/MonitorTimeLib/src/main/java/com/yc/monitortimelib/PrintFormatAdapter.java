package com.yc.monitortimelib;

import java.util.LinkedHashMap;
import java.util.Map;

public interface PrintFormatAdapter {

    String onFormat(String processName, long startTime, LinkedHashMap<String, Long> timeTicks);

    final class Factory {
        private Factory() {

        }

        public static PrintFormatAdapter newDefaultLogAdapter() {
            return new PrintFormatAdapter() {
                @Override
                public String onFormat(String processName, long startTime, LinkedHashMap<String, Long> timeTicks) {
                    String rst = processName + ": [ \n";
                    for (Map.Entry<String, Long> entry : timeTicks.entrySet()) {
                        String actionName = entry.getKey();
                        long time = entry.getValue();
                        String tick = "{ " + actionName + "：" + time + "ms,distance start time:" + (time - startTime) + "ms }\n";
                        rst += tick;
                    }
                    rst = processName + ": ] " + processName;
                    return rst;
                }
            };
        }
    }
}
