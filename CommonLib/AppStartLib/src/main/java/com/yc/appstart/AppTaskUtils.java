package com.yc.appstart;


import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

public final class AppTaskUtils {

    private static final String TAG = "AppStartTask ";

    public static void showLog(boolean isShowLog, String log) {
        if (isShowLog) {
            Log.e(TAG, log);
        }
    }

    /**
     * 拓扑排序
     * taskIntegerHashMap每个Task的入度（key= Class < ? extends AppStartTask>）
     * taskHashMap每个Task            （key= Class < ? extends AppStartTask>）
     * taskChildHashMap每个Task的孩子  （key= Class < ? extends AppStartTask>）
     * deque 入度为0的Task
     */
    public static List<AppStartTask> getSortResult(
            List<AppStartTask> startTaskList,
            HashMap<Class<? extends AppStartTask>, AppStartTask> taskHashMap,
            HashMap<Class<? extends AppStartTask>, List<Class<? extends AppStartTask>>> taskChildHashMap) {
        List<AppStartTask> sortTaskList = new ArrayList<>();
        HashMap<Class<? extends AppStartTask>, TaskSortModel> taskIntegerHashMap = new HashMap<>();
        Deque<Class<? extends AppStartTask>> deque = new ArrayDeque<>();
        for (AppStartTask task : startTaskList) {
            if (!taskIntegerHashMap.containsKey(task.getClass())) {
                taskHashMap.put(task.getClass(), task);
                taskIntegerHashMap.put(task.getClass(), new TaskSortModel(task.getDependsTaskList() == null ? 0 : task.getDependsTaskList().size()));
                taskChildHashMap.put(task.getClass(), new ArrayList<Class<? extends AppStartTask>>());
                //入度为0的队列
                TaskSortModel taskSortModel = taskIntegerHashMap.get(task.getClass());
                if (taskSortModel != null && taskSortModel.getIn() == 0) {
                    deque.offer(task.getClass());
                }
            } else {
                throw new RuntimeException("任务重复了: " + task.getClass());
            }
        }
        //把孩子都加进去
        for (AppStartTask task : startTaskList) {
            if (task.getDependsTaskList() != null) {
                for (Class<? extends AppStartTask> aclass : task.getDependsTaskList()) {
                    List<Class<? extends AppStartTask>> classes = taskChildHashMap.get(aclass);
                    if (classes != null) {
                        classes.add(task.getClass());
                    }
                }
            }
        }
        //循环去除入度0的，再把孩子入度变成0的加进去
        while (!deque.isEmpty()) {
            Class<? extends AppStartTask> aclass = deque.poll();
            sortTaskList.add(taskHashMap.get(aclass));
            List<Class<? extends AppStartTask>> classes = taskChildHashMap.get(aclass);
            if (classes == null || classes.size() == 0) {
                continue;
            }
            for (Class<? extends AppStartTask> classChild : classes) {
                TaskSortModel taskSortModel = taskIntegerHashMap.get(classChild);
                if (taskSortModel != null) {
                    taskSortModel.setIn(taskSortModel.getIn() - 1);
                    if (taskSortModel.getIn() == 0) {
                        deque.offer(classChild);
                    }
                }
            }
        }
        if (sortTaskList.size() != startTaskList.size()) {
            throw new RuntimeException("出现环了");
        }
        return sortTaskList;
    }
}
