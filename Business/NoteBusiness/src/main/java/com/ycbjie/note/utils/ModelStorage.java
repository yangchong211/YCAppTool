package com.ycbjie.note.utils;


import com.ns.yc.yccustomtextlib.edit.model.HyperEditData;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/18
 *     desc  : 数据缓冲区，替代intent传递大数据方案
 *     revise:
 * </pre>
 */
public class ModelStorage {

    private List<HyperEditData> hyperEditData = new ArrayList<>();

    public static ModelStorage getInstance(){
        return SingletonHolder.instance;
    }

    private static class SingletonHolder{
        private static final ModelStorage instance = new ModelStorage();
    }

    public List<HyperEditData> getHyperEditData() {
        return hyperEditData;
    }

    public void setHyperEditData(List<HyperEditData> hyperEditData) {
        this.hyperEditData.clear();
        this.hyperEditData.addAll(hyperEditData);
    }
}
