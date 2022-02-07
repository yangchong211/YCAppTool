
package com.ycbjie.live.whitelist;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 白名单跳转回调
 *     revise:
 * </pre>
 */
public interface IWhiteListCallback {

    /**
     * 初始化
     *
     * @param target  需要加入白名单的目标
     * @param appName 需要处理白名单的应用名
     */
    void init(@NonNull String target, @NonNull String appName);

    /**
     * 显示白名单
     *
     * @param activity
     * @param intentWrapper
     */
    void showWhiteList(@NonNull Activity activity, @NonNull WhiteListIntentWrapper intentWrapper);

    /**
     * 显示白名单
     *
     * @param fragment
     * @param intentWrapper
     */
    void showWhiteList(@NonNull Fragment fragment, @NonNull WhiteListIntentWrapper intentWrapper);

}
