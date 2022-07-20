package com.yc.alive.manager;

import android.util.SparseArray;

import androidx.annotation.RestrictTo;

import com.yc.alive.constant.AliveSettingType.TYPE;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.service.BaseAccessibility;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 操作管理
 */
@RestrictTo(LIBRARY)
public class AliveOptionManager {

    private SparseArray<AliveOptionModel> mOptionArray;

    private AliveOptionModel mCurrentOption;

    private BaseAccessibility mAccessibility;

    private AliveOptionManager() {
    }

    private static final class InnerHolder {
        private static final AliveOptionManager INSTANCE = new AliveOptionManager();
    }

    public static AliveOptionManager getInstance() {
        return InnerHolder.INSTANCE;
    }

    public void setCurrentOption(AliveOptionModel option) {
        this.mCurrentOption = option;
    }

    public AliveOptionModel getCurrentOption() {
        return mCurrentOption;
    }

    public void clearCurrentOption() {
        if (mCurrentOption != null) {
            mCurrentOption.clear();
        }
    }

    public void initOptions(SparseArray<AliveOptionModel> array) {
        this.mOptionArray = array;
    }

    public void setAccessibility(BaseAccessibility accessibility) {
        this.mAccessibility = accessibility;
    }

    public BaseAccessibility getAccessibility() {
        return mAccessibility;
    }

    public AliveOptionModel getOption(@TYPE int type) {
        return mOptionArray.get(type).clone();
    }
}
