

package com.yc.todoapplive.tasks;

import android.widget.ListView;

import com.yc.todoapplive.data.Task;
import androidx.databinding.BindingAdapter;
import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Task} list.
 */
public class TasksListBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Task> items) {
        TasksAdapter adapter = (TasksAdapter) listView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }
}
