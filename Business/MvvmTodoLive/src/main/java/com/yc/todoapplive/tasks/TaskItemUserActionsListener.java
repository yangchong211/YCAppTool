

package com.yc.todoapplive.tasks;


import android.view.View;

import com.yc.todoapplive.data.Task;

/**
 * Listener used with data binding to process user actions.
 */
public interface TaskItemUserActionsListener {
    void onCompleteChanged(Task task, View v);

    void onTaskClicked(Task task);
}
