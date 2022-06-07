package com.yc.time.loader.gradle.task;

import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskState;

/**
 * Gradle 提供了很多构建生命周期钩子函数，我们可以用 TaskExecutionListener 来监听整个构建过程中 task 的执行
 */
public class AppTaskExecutionListener implements TaskExecutionListener {
    @Override
    public void beforeExecute(Task task) {

    }

    @Override
    public void afterExecute(Task task, TaskState taskState) {

    }
}
