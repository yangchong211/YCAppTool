
package com.ycbjie.live.whitelist;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;


import androidx.fragment.app.Fragment;

import com.ycbjie.live.alive.YcKeepAlive;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 加入系统白名单的意图信息
 *     revise:
 * </pre>
 */
public final class WhiteListIntentWrapper {

    /**
     * 跳转意图
     */
    private Intent mIntent;
    /**
     * 类型
     */
    private int mType;

    public WhiteListIntentWrapper(Intent intent, int type) {
        mIntent = intent;
        mType = type;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public WhiteListIntentWrapper setIntent(Intent intent) {
        mIntent = intent;
        return this;
    }

    public int getType() {
        return mType;
    }

    public WhiteListIntentWrapper setType(int type) {
        mType = type;
        return this;
    }

    /**
     * 判断本机上是否有能处理当前Intent的Activity
     */
    public boolean doesActivityExists() {
        PackageManager pm = YcKeepAlive.getApplication().getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    /**
     * 安全地启动一个Activity
     */
    public void startActivitySafely(Activity activity) {
        try {
            activity.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地启动一个Activity
     */
    public void startActivitySafely(Fragment fragment) {
        try {
            fragment.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 优先使用fragment进行跳转
     *
     * @param activity
     * @param fragment
     */
    public void startActivitySafely(Activity activity, Fragment fragment) {
        if (fragment != null) {
            startActivitySafely(fragment);
        } else {
            startActivitySafely(activity);
        }
    }

    @Override
    public String toString() {
        return "WhiteListIntentWrapper{" +
                "mIntent=" + mIntent +
                ", mType=" + mType +
                '}';
    }
}
