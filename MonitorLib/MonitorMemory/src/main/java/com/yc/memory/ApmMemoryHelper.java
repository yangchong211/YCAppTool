package com.yc.memory;

import android.content.Context;

import com.yc.toolmemorylib.AppMemoryUtils;
import com.yc.toolmemorylib.DalvikHeapMem;
import com.yc.toolmemorylib.PssInfo;
import com.yc.toolmemorylib.RamMemoryInfo;

public class ApmMemoryHelper {

    private static volatile ApmMemoryHelper singleton = null;

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static ApmMemoryHelper getInstance() {
        if (singleton == null) {
            synchronized (ApmMemoryHelper.class) {
                if (singleton == null) {
                    singleton = new ApmMemoryHelper();
                }
            }
        }
        return singleton;
    }

    private ApmMemoryHelper() {

    }

    public void getAppMemory(Context context , AppMemoryUtils.OnGetMemoryInfoCallback callback) {
        AppMemoryUtils.getMemoryInfo(context, new AppMemoryUtils.OnGetMemoryInfoCallback() {
            @Override
            public void onGetMemoryInfo(String pkgName, int pid, RamMemoryInfo ramMemoryInfo,
                                        PssInfo pssInfo, DalvikHeapMem dalvikHeapMem) {
                callback.onGetMemoryInfo(pkgName, pid, ramMemoryInfo, pssInfo, dalvikHeapMem);
            }
        });
    }


}
