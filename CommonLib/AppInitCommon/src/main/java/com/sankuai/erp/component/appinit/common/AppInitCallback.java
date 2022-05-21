package com.sankuai.erp.component.appinit.common;

import java.util.List;
import java.util.Map;

/**
 * 作者:王浩
 * 创建时间:2018/10/30
 * 描述:初始化配置回调接口
 */
public interface AppInitCallback {

    /**
     * 开始初始化
     *
     * @param isMainProcess 是否为主进程
     * @param processName   进程名称
     */
    void onInitStart(boolean isMainProcess, String processName);

    /**
     * 是否为 debug 模式
     */
    boolean isDebug();

    /**
     * 通过 coordinate 自定义依赖关系映射，键值都是 coordinate
     *
     * @return 如果返回的 map 不为空，则会在启动时检测依赖并重新排序
     */
    Map<String, String> getCoordinateAheadOfMap();

    /**
     * 同步初始化完成
     *
     * @param isMainProcess      是否为主进程
     * @param processName        进程名称
     * @param childInitTableList 初始化模块列表
     * @param appInitItemList    初始化列表
     */
    void onInitFinished(boolean isMainProcess, String processName, List<ChildInitTable> childInitTableList, List<AppInitItem> appInitItemList);
}
