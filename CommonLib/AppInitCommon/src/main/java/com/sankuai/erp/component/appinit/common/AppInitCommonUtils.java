package com.sankuai.erp.component.appinit.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者:王浩
 * 创建时间:2018/10/26
 * 描述:
 */
public final class AppInitCommonUtils {

    private AppInitCommonUtils() {
    }

    public static List<AppInitItem> sortAppInitItem(boolean abortOnNotExist, List<ChildInitTable> childInitTableList, Map<String, String> coordinateAheadOfMap,
            StringBuilder logSb) {
        logSb.append("处理 aheadOf 前的顺序为：\n");
        StringBuilder notHandleAheadOfLogSb = new StringBuilder();

        Map<String, AppInitItem> coordinateMap = new HashMap<>();

        AppInitItem head = null;
        AppInitItem tail = null;
        AppInitItem pre;
        AppInitItem target;

        for (ChildInitTable childInitTable : childInitTableList) {
            logSb.append(childInitTable.getModuleInfo()).append('\n');

            Collections.sort(childInitTable);
            for (AppInitItem current : childInitTable) {
                current.moduleInfo = childInitTable.getModuleInfo();
                logSb.append(current.toString()).append("\n");
                // 检测 coordinate 是否重复
                checkAppInitDuplicate(current, coordinateMap);

                if (coordinateAheadOfMap != null && coordinateAheadOfMap.containsKey(current.coordinate)) {
                    current.aheadOf = coordinateAheadOfMap.get(current.coordinate);
                }

                if (head == null) {
                    head = current;
                    tail = current;
                } else {
                    tail.next = current;
                    current.pre = tail;
                    tail = current;

                    if (isEmpty(tail.aheadOf)) {
                        continue;
                    }

                    target = coordinateMap.get(tail.aheadOf);
                    if (target == null) {
                        notHandleAheadOfLogSb.append(String.format("%s aheadOf 的「%s」不存在，或已经在其之后了\n", tail.toString(), tail.aheadOf));
                        continue;
                    }
                    // 当前结点的前一个结点设置为新的 tail
                    tail = current.pre;
                    tail.next = null;

                    pre = target.pre;
                    if (pre == null) {
                        // pre 为空，说明 target 就是老的 head，将 current 设置为新的 head
                        current.pre = null;
                        head = current;
                    } else {
                        current.pre = pre;
                        pre.next = current;
                    }

                    target.pre = current;
                    current.next = target;
                }
            }
        }

        if (coordinateAheadOfMap != null && !coordinateAheadOfMap.isEmpty()) {
            for (String coordinate : coordinateAheadOfMap.keySet()) {
                if (!coordinateMap.containsKey(coordinate)) {
                    notHandleAheadOfLogSb.append(String.format("getCoordinateAheadOfMap() 方法中返回的「%s」不存在\n", coordinate));
                }
            }
        }

        String notHandleAheadOfLog = notHandleAheadOfLogSb.toString();
        if (!isEmpty(notHandleAheadOfLog)) {
            notHandleAheadOfLog = "\n    !!!!!!未能处理的 aheadOf 有：\n" + notHandleAheadOfLog;
            if (abortOnNotExist) {
                throw new IllegalArgumentException(notHandleAheadOfLog);
            }
        }

        List<AppInitItem> result = assembleAppInitItemList(head, logSb);
        logSb.append(notHandleAheadOfLog).append("\n");
        return result;
    }

    private static List<AppInitItem> assembleAppInitItemList(AppInitItem appInitItem, StringBuilder logSb) {
        logSb.append("\n最终的初始化顺序为：\n");
        List<AppInitItem> appInitItemList = new ArrayList<>();
        while (appInitItem != null) {
            if (appInitItem.pre == null || !equals(appInitItem.pre.moduleCoordinate, appInitItem.moduleCoordinate)) {
                logSb.append(appInitItem.moduleInfo).append("\n");
            }
            logSb.append(appInitItem.toString()).append("\n");
            appInitItemList.add(appInitItem);
            appInitItem = appInitItem.next;
        }
        return appInitItemList;
    }

    private static void checkAppInitDuplicate(AppInitItem appInitItem, Map<String, AppInitItem> coordinateMap) {
        String coordinate = appInitItem.coordinate;
        if (isEmpty(coordinate)) {
            return;
        }

        if (coordinateMap.containsKey(coordinate)) {
            String msg = "不允许出现两个 AppInit 的 coordinate 相同：\n" + appInitItem.toString() + "\n" + coordinateMap.get(coordinate).toString() + "\n";
            throw new IllegalArgumentException(msg);
        } else {
            coordinateMap.put(coordinate, appInitItem);
        }
    }

    // android.text.TextUtils.isEmpty
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    // android.text.TextUtils.equals
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 统计执行时间
     */
    public static long time(Runnable runnable) {
        if (runnable == null) {
            return 0;
        }
        long startTime = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - startTime;
    }

    /**
     * 统计执行时间
     */
    public static long time(String desc, Runnable runnable) {
        long time = time(runnable);
        AppInitLogger.d(String.format("%s耗时:%sms\n\n", desc, time));
        return time;
    }

    /**
     * 统计执行时间
     */
    public static String timeStr(String desc, Runnable runnable) {
        String msg = String.format("%s耗时:%sms\n\n", desc, time(runnable));
        AppInitLogger.d(msg);
        return msg;
    }
}
