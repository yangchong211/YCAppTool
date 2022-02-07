package com.yc.toollib.crash.compat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.servertransaction.ClientTransaction;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.Method;



public class ActivityKillerV28 implements IActivityKiller {

    @Override
    public void finishLaunchActivity(Message message) {
        try {
            tryFinish1(message);
            return;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        try {
            tryFinish2(message);
            return;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        try {
            tryFinish3(message);
            return;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void tryFinish1(Message message) throws Throwable {
        ClientTransaction clientTransaction = (ClientTransaction) message.obj;
        IBinder binder = clientTransaction.getActivityToken();
        finish(binder);
    }

    private void tryFinish3(Message message) throws Throwable {
        Object clientTransaction = message.obj;
        Field mActivityTokenField = clientTransaction.getClass().getDeclaredField("mActivityToken");
        IBinder binder = (IBinder) mActivityTokenField.get(clientTransaction);
        finish(binder);
    }

    private void tryFinish2(Message message) throws Throwable {
        Object clientTransaction = message.obj;
        Method getActivityTokenMethod = clientTransaction.getClass().getDeclaredMethod("getActivityToken");
        IBinder binder = (IBinder) getActivityTokenMethod.invoke(clientTransaction);
        finish(binder);
    }


    @Override
    public void finishResumeActivity(Message message) {

    }


    @Override
    public void finishPauseActivity(Message message) {

    }

    @Override
    public void finishStopActivity(Message message) {

    }

    private void finish(IBinder binder) throws Exception {
        Class<?> clazz = ActivityManager.class;
        //获取方法
        Method getServiceMethod = clazz.getDeclaredMethod("getService");
        getServiceMethod.setAccessible(true);
        //通过invoke方法调用
        Object activityManager = getServiceMethod.invoke(null);
        Class<?> managerClass = activityManager.getClass();
        //获取ActivityManagerService类，简称AMS，拿到finishActivity方法
        //第四个参数是：是否完成与此活动关联的任务。
        Method finishActivityMethod = managerClass.getDeclaredMethod(
                "finishActivity", IBinder.class, int.class, Intent.class, int.class);
        finishActivityMethod.setAccessible(true);
        //当活动完成时，任务还没有完成
        int DONT_FINISH_TASK_WITH_ACTIVITY = 0;
        //FINISH_TASK_WITH_ROOT_ACTIVITY = 1
        //表示如果完成活动是任务的根，则任务已完成。为了保存过去的行为，该任务也从最近项中删除。
        //FINISH_TASK_WITH_ACTIVITY = 2
        //任务与完成活动一起完成，但不会从近期中删除。
        finishActivityMethod.invoke(activityManager, binder,
                Activity.RESULT_CANCELED, null, DONT_FINISH_TASK_WITH_ACTIVITY);
    }

}
