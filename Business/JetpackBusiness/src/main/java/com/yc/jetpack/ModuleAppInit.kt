package com.yc.jetpack

import com.sankuai.erp.component.appinit.api.SimpleAppInit
import com.sankuai.erp.component.appinit.common.AppInit
import com.sankuai.erp.component.appinit.common.AppInitLogger


@AppInit(priority = 5, description = "模块jetpakc组件的描述")
class ModuleAppInit : SimpleAppInit() {

    override fun onCreate() {
        AppInitLogger.demo("onCreate ModuleAppInit 模块jetpakc组件的描述")
    }
}