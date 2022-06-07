package com.yc.common

import com.sankuai.erp.component.appinit.api.SimpleAppInit
import com.sankuai.erp.component.appinit.common.AppInit
import com.sankuai.erp.component.appinit.common.AppInitLogger


@AppInit(priority = 2, description = "模块公共组件的描述")
class ModuleAppInit : SimpleAppInit() {

    override fun onCreate() {
        AppInitLogger.demo("onCreate ModuleAppInit 模块公共组件的描述")
    }
}