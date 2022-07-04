package com.yc.baseclasslib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 父类BroadcastReceiver
 *     revise:
 * </pre>
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    /**
     * 不建议做耗时操作。
     * 如果要在广播中执行耗时操作，通过创建子线程的方式是不可靠的，因为它生命周期很短，
     * 一旦结束，其所在进程属于空进程（没有任何活动组件的进程），极易在系统内存不足时优先被杀死，如此，正在工作的子线程也会被杀死。
     *
     * 在广播中执行耗时操作，可开启 Service 将耗时操作交给它，这样可以提高宿主进程优先级，保证耗时操作执行完成。
     */


    /**
     * 广播的回调
     * @param context       上下文
     * @param intent        intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

    }



}
