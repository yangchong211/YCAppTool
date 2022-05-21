package com.sankuai.erp.component.appinit.common;

/**
 * 作者:王浩
 * 创建时间:2018/1/18
 * 描述:
 */
public interface ModuleConsts {
    // 该库对应的 apt 模块坐标参数 key
    String APT_MODULE_COORDINATE_KEY = "AppInitAptModuleCoordinate";
    // 该库对应的 apt 模块依赖参数 key
    String APT_DEPENDENCIES_KEY = "APP_INIT_DEPENDENCIES";

    String DOT = ".";
    String PACKAGE_NAME_GENERATED = "com.sankuai.erp.component.appinit.generated";
    String PACKAGE_NAME_GENERATED_SLASH = PACKAGE_NAME_GENERATED.replace('.', '/');
    String PACKAGE_NAME_COMMON = "com.sankuai.erp.component.appinit.common";
    String PACKAGE_NAME_COMMON_SLASH = PACKAGE_NAME_COMMON.replace('.', '/');
    String PACKAGE_NAME_API = "com.sankuai.erp.component.appinit.api";
    String PACKAGE_NAME_API_SLASH = PACKAGE_NAME_API.replace('.', '/');

    String APP_INIT_MANAGER = "AppInitManager";
    String APP_INIT_MANAGER_CANONICAL_NAME = PACKAGE_NAME_API + DOT + APP_INIT_MANAGER;
    String APP_INIT_ITEM = "AppInitItem";
    String APP_INIT_ITEM_CANONICAL_NAME = PACKAGE_NAME_COMMON + DOT + APP_INIT_ITEM;
    String APP_INIT_INTERFACE = PACKAGE_NAME_COMMON + DOT + "IAppInit";
    String CHILD_INIT_TABLE_SUFFIX = "ChildInitTable";
    String CHILD_INIT_TABLE_CANONICAL_NAME = PACKAGE_NAME_COMMON + DOT + CHILD_INIT_TABLE_SUFFIX;

    String FIELD_INJECT_ABORT_ON_NOT_EXIST = "mAbortOnNotExist";
    String METHOD_INJECT_CHILD_INIT_TABLE_LIST = "injectChildInitTableList";
    String METHOD_INJECT_APP_INIT_ITEM_LIST = "injectAppInitItemList";
}
