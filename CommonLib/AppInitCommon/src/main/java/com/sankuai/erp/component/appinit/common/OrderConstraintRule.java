package com.sankuai.erp.component.appinit.common;

/**
 * 作者:王浩
 * 创建时间:2018/1/18
 * 描述:排序类型
 */
public enum OrderConstraintRule {
    // 按 priority 排序
    PRIORITY,
    // 始终最先初始化
    HEAD,
    // 始终最后初始化
    TAIL
}