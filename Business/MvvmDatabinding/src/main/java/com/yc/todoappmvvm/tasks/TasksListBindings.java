

package com.yc.todoappmvvm.tasks;

import android.widget.ListView;

import androidx.databinding.BindingAdapter;

import com.yc.todoappmvvm.data.Task;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Task} list.
 */
public class TasksListBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Task> items) {
        TasksFragment.TasksAdapter adapter = (TasksFragment.TasksAdapter) listView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }
}
