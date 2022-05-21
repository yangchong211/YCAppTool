package com.sankuai.erp.component.appinit.api;

import com.sankuai.erp.component.appinit.common.AppInitCallback;
import com.sankuai.erp.component.appinit.common.AppInitItem;
import com.sankuai.erp.component.appinit.common.ChildInitTable;

import java.util.List;
import java.util.Map;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : AppInitCallback抽象子类
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCAppTool
 * </pre>
 */
public abstract class SimpleAppInitCallback implements AppInitCallback {
    @Override
    public void onInitStart(boolean isMainProcess, String processName) {
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public Map<String, String> getCoordinateAheadOfMap() {
        return null;
    }

    @Override
    public void onInitFinished(boolean isMainProcess, String processName, List<ChildInitTable> childInitTableList, List<AppInitItem> appInitItemList) {
    }
}
