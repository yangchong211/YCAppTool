package com.sankuai.erp.component.appinit.common;

/**
 * 作者:王浩
 * 创建时间:2018/1/18
 * 描述:
 */
public class AppInitItem implements Comparable<AppInitItem> {
    public String appInitClassName;
    public IAppInit appInit;
    public Process process; // 指定在哪个进程初始化
    public int priority; // 开发者配置的初始化顺序优先级，越大越先初始化，用于模块返回内重新排序
    public String coordinate; // 唯一标识
    public String aheadOf; // 在指定项之前初始化，用于整个项目范围内重新排序
    public String description; // 描述
    public boolean onlyForDebug; // 只有在 debug 时才初始化
    public AppInitItem pre;
    public AppInitItem next;
    public String moduleInfo;
    public String moduleCoordinate;
    public long time;

    // 给 AppInitProcessor 生成子表时调用
    @SuppressWarnings("checkstyle:ParameterNumber")
    public AppInitItem(
            String appInitClassName,
            int processOrdinal,
            int priority,
            String coordinate,
            String aheadOf,
            String description,
            String onlyForDebug,
            String moduleCoordinate) {
        this.appInitClassName = appInitClassName;
        this.process = Process.values()[processOrdinal];
        this.priority = priority;
        this.coordinate = coordinate;
        this.aheadOf = aheadOf;
        this.description = description;
        this.onlyForDebug = Boolean.valueOf(onlyForDebug);
        this.moduleCoordinate = moduleCoordinate;
    }

    // 给 AppInitTransform 生成主表时调用
    @SuppressWarnings("checkstyle:ParameterNumber")
    public AppInitItem(
            IAppInit appInit,
            int processOrdinal,
            int priority,
            String coordinate,
            String aheadOf,
            String description,
            String onlyForDebug,
            String moduleCoordinate) {
        this(appInit.getClass().getCanonicalName(), processOrdinal, priority, coordinate, aheadOf, description, onlyForDebug, moduleCoordinate);
        this.appInit = appInit;
    }

    public boolean isForMainProcess() {
        return Process.MAIN == process || isForAllProcess();
    }

    private boolean isForAllProcess() {
        return Process.ALL == process;
    }

    public boolean isNotForMainProcess() {
        return Process.OTHER == process || isForAllProcess();
    }

    @Override
    public int compareTo(AppInitItem other) {
        return Integer.compare(this.priority, other.priority);
    }

    @Override
    public String toString() {
        if (AppInitCommonUtils.isEmpty(aheadOf)) {
            return String.format("        * [%s][%s][进程=%s][description=%s]", coordinate, priority, process.name(), description);
        } else {
            return String.format("        * [%s][%s][进程=%s][aheadOf=%s][description=%s]", coordinate, priority, process.name(), aheadOf, description);
        }
    }

    public String timeInfo() {
        if (AppInitCommonUtils.isEmpty(aheadOf)) {
            return String.format("        * [%s][%s][进程=%s][description=%s][耗时=%sms]", coordinate, priority, process.name(), description, time);
        } else {
            return String.format("        * [%s][%s][进程=%s][aheadOf=%s][description=%s][耗时=%sms]",
                    coordinate, priority, process.name(), aheadOf, description, time);
        }
    }
}
