package com.yc.apprestartlib.reboot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 这个Activity确实是在:reboot 进程启动的，且是Translucent透明的。
 * 可以无感知重启App
 */
public final class ProcessReboot extends Activity {
    private static final String KEY_RESTART_INTENTS = "reboot_restart_intents";
    private static final String KEY_MAIN_PROCESS_PID = "reboot_main_process_pid";

    /**
     * 重启app后进入首页
     * 核心原理
     * 当调用triggerRebirth方法的时候，会启动一个透明的Activity，这个Activity运行在: reboot 进程
     * Activity启动后，杀掉主进程，然后用:reboot 进程拉起主进程的Activity
     * 关闭当前Activity，杀掉:reboot 进程
     *
     * @param context 上下文
     */
    public static void triggerRebirth(Context context) {
        triggerRebirth(context, getRestartIntent(context));
    }

    /**
     * 重启app后进入特定的页面，则需要构造具体页面的intent，当做参数传入
     *
     * @param context     上下文
     * @param nextIntents intent
     */
    public static void triggerRebirth(Context context, Intent... nextIntents) {
        if (nextIntents.length < 1) {
            throw new IllegalArgumentException("intents cannot be empty");
        }
        // 第一个activity添加new_task标记，重新开启一个新的stack
        nextIntents[0].addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        Intent intent = new Intent(context, ProcessReboot.class);
        // 这里是为了防止传入的context非Activity
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        // 将待启动的intent作为参数，intent是parcelable的
        intent.putParcelableArrayListExtra(KEY_RESTART_INTENTS, new ArrayList<>(Arrays.asList(nextIntents)));
        // 将主进程的pid作为参数
        intent.putExtra(KEY_MAIN_PROCESS_PID, Process.myPid());
        // 启动 ProcessReboot Activity
        context.startActivity(intent);
    }

    /**
     * 获取默认的intent
     *
     * @param context 上下文
     * @return 默认的intent
     */
    private static Intent getRestartIntent(Context context) {
        //获取包名
        String packageName = context.getPackageName();
        //获取启动默认的intent
        Intent defaultIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (defaultIntent != null) {
            return defaultIntent;
        }
        throw new IllegalStateException("Unable to determine default activity for "
                + packageName
                + ". Does an activity specify the DEFAULT category in its intent filter?");
    }

    /**
     * reboot 进程主要做了以下事情：
     * 杀死主进程
     * 用传入的Intent启动主进程的Activity（也可以是Service）
     * 关闭 ProcessReboot Activity，杀掉 reboot 进程
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //杀死原主进程
        Process.killProcess(getIntent().getIntExtra(KEY_MAIN_PROCESS_PID, -1));

        ArrayList<Intent> intents = getIntent().getParcelableArrayListExtra(KEY_RESTART_INTENTS);
        startActivities(intents.toArray(new Intent[intents.size()]));
        finish();
        //杀死该进程
        Runtime.getRuntime().exit(0);
    }
}
