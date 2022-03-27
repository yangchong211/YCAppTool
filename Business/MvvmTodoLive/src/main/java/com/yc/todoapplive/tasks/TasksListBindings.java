

package com.yc.todoapplive.tasks;

import android.widget.ListView;

import com.yc.todoapplive.data.Task;
import androidx.databinding.BindingAdapter;
import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Task} list.
 */
public class TasksListBindings {

    // 警告提示说，我们的命名空间"app"将会被忽略。
    // 也就说实际运行的时候，是不管这个app命名空间的。注解关注的只是app后面这个"items"。
    // 命名空间是可要可不要的。因此解决办法就是，把这个命名空间的声明删掉。
    @SuppressWarnings("unchecked")
    //@BindingAdapter("app:items")
    @BindingAdapter("items")
    public static void setItems(ListView listView, List<Task> items) {
        TasksAdapter adapter = (TasksAdapter) listView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }
}
