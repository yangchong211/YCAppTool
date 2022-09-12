package com.yc.apprestartlib;

import android.content.Context;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 重启APP接口
 *     revise:
 * </pre>
 */
public interface IRestartProduct {

    /**
     * 重启App抽象方法
     *
     * @param context 上下文
     */
    void restartApp(Context context);

}
