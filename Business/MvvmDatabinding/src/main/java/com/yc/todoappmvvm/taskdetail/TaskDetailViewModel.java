

package com.yc.todoappmvvm.taskdetail;

import android.content.Context;

import androidx.annotation.Nullable;

import com.yc.todoappmvvm.TaskViewModel;
import com.yc.todoappmvvm.data.source.TasksRepository;
import com.yc.todoappmvvm.tasks.TasksFragment;


/**
 * Listens to user actions from the list item in ({@link TasksFragment}) and redirects them to the
 * Fragment's actions listener.
 */
public class TaskDetailViewModel extends TaskViewModel {

    @Nullable
    private TaskDetailNavigator mTaskDetailNavigator;

    public TaskDetailViewModel(Context context, TasksRepository tasksRepository) {
        super(context, tasksRepository);
    }

    public void setNavigator(TaskDetailNavigator taskDetailNavigator) {
        mTaskDetailNavigator = taskDetailNavigator;
    }

    public void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mTaskDetailNavigator = null;
    }

    /**
     * Can be called by the Data Binding Library or the delete menu item.
     */
    public void deleteTask() {
        super.deleteTask();
        if (mTaskDetailNavigator != null) {
            mTaskDetailNavigator.onTaskDeleted();
        }
    }

    public void startEditTask() {
        if (mTaskDetailNavigator != null) {
            mTaskDetailNavigator.onStartEditTask();
        }
    }
}
