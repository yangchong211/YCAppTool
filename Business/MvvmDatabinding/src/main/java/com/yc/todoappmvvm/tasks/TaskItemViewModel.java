

package com.yc.todoappmvvm.tasks;

import android.content.Context;

import androidx.annotation.Nullable;

import com.yc.todoappmvvm.TaskViewModel;
import com.yc.todoappmvvm.data.source.TasksRepository;

import java.lang.ref.WeakReference;


/**
 * Listens to user actions from the list item in ({@link TasksFragment}) and redirects them to the
 * Fragment's actions listener.
 */
public class TaskItemViewModel extends TaskViewModel {

    // This navigator is s wrapped in a WeakReference to avoid leaks because it has references to an
    // activity. There's no straightforward way to clear it for each item in a list adapter.
    @Nullable
    private WeakReference<TaskItemNavigator> mNavigator;

    public TaskItemViewModel(Context context, TasksRepository tasksRepository) {
        super(context, tasksRepository);
    }

    public void setNavigator(TaskItemNavigator navigator) {
        mNavigator = new WeakReference<>(navigator);
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void taskClicked() {
        String taskId = getTaskId();
        if (taskId == null) {
            // Click happened before task was loaded, no-op.
            return;
        }
        if (mNavigator != null && mNavigator.get() != null) {
            mNavigator.get().openTaskDetails(taskId);
        }
    }
}
