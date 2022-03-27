

package com.yc.todoapplive.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.yc.todoapplive.data.Task;
import com.yc.todoapplive.databinding.TaskItemBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;


public class TasksAdapter extends BaseAdapter {

    private final TasksViewModel mTasksViewModel;

    private List<Task> mTasks;

    private LifecycleOwner mLifecycleOwner;

    public TasksAdapter(List<Task> tasks,
            TasksViewModel tasksViewModel, LifecycleOwner activity) {
        mTasksViewModel = tasksViewModel;
        setList(tasks);
        mLifecycleOwner = activity;

    }

    public void replaceData(List<Task> tasks) {
        setList(tasks);
    }

    @Override
    public int getCount() {
        return mTasks != null ? mTasks.size() : 0;
    }

    @Override
    public Task getItem(int position) {
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View view, final ViewGroup viewGroup) {
        TaskItemBinding binding;
        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = TaskItemBinding.inflate(inflater, viewGroup, false);
        } else {
            // Recycling view
            binding = DataBindingUtil.getBinding(view);
        }

        TaskItemUserActionsListener userActionsListener = new TaskItemUserActionsListener() {
            @Override
            public void onCompleteChanged(Task task, View v) {
                boolean checked = ((CheckBox)v).isChecked();
                mTasksViewModel.completeTask(task, checked);
            }

            @Override
            public void onTaskClicked(Task task) {
                mTasksViewModel.openTask(task.getId());
            }
        };

        binding.setTask(mTasks.get(position));
        binding.setLifecycleOwner(mLifecycleOwner);

        binding.setListener(userActionsListener);

        binding.executePendingBindings();
        return binding.getRoot();
    }

    private void setList(List<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }
}
