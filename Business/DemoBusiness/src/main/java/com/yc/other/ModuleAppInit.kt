package com.yc.other

import com.sankuai.erp.component.appinit.api.SimpleAppInit
import com.sankuai.erp.component.appinit.common.AppInit
import com.sankuai.erp.component.appinit.common.AppInitLogger


@AppInit(priority = 3, description = "模块demo的描述")
class ModuleAppInit : SimpleAppInit() {

    override fun onCreate() {
        AppInitLogger.demo("onCreate ModuleAppInit 模块demo的描述")
    }
}