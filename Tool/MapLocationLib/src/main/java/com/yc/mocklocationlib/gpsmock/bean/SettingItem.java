package com.yc.mocklocationlib.gpsmock.bean;



import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.List;

public class SettingItem {
    @StringRes
    public final int desc;
    public String descStr;
    public String subDescStr;
    public String rightDesc;
    @DrawableRes
    public int icon;
    public boolean isChecked;
    public boolean canCheck;
    public List<String> enumList = null;

    public SettingItem(@StringRes int desc) {
        this.desc = desc;
    }

    public SettingItem(@StringRes int desc, boolean isChecked) {
        this.desc = desc;
        this.isChecked = isChecked;
        this.canCheck = true;
    }

    public SettingItem(@StringRes int desc, @DrawableRes int icon) {
        this.desc = desc;
        this.icon = icon;
    }

    public SettingItem(String descStr, String subDescStr, boolean isChecked, List<String> enumList) {
        this.desc = 0;
        this.descStr = descStr;
        this.subDescStr = subDescStr;
        this.canCheck = true;
        this.isChecked = isChecked;
        this.enumList = enumList;
    }
}

