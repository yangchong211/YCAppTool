

package com.yc.todoapplive.taskdetail;

import static com.yc.todoapplive.addedittask.AddEditTaskActivity.ADD_EDIT_RESULT_OK;
import static com.yc.todoapplive.taskdetail.TaskDetailFragment.REQUEST_EDIT_TASK;

import android.content.Intent;
import android.os.Bundle;

import com.yc.todoapplive.Event;
import com.yc.todoapplive.R;
import com.yc.todoapplive.ViewModelFactory;
import com.yc.todoapplive.addedittask.AddEditTaskActivity;
import com.yc.todoapplive.addedittask.AddEditTaskFragment;
import com.yc.todoapplive.util.ActivityUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Displays task details screen.
 */
public class TaskDetailActivity extends AppCompatActivity implements TaskDetailNavigator {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    public static final int DELETE_RESULT_OK = RESULT_FIRST_USER + 2;

    public static final int EDIT_RESULT_OK = RESULT_FIRST_USER + 3;

    private TaskDetailViewModel mTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taskdetail_act);

        setupToolbar();

        TaskDetailFragment taskDetailFragment = findOrCreateViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                taskDetailFragment, R.id.contentFrame);

        mTaskViewModel = obtainViewModel(this);

        subscribeToNavigationChanges(mTaskViewModel);
    }

    @NonNull
    private TaskDetailFragment findOrCreateViewFragment() {
        // Get the requested task id
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);
        }
        return taskDetailFragment;
    }

    @NonNull
    public static TaskDetailViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(TaskDetailViewModel.class);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    private void subscribeToNavigationChanges(TaskDetailViewModel viewModel) {
        // The activity observes the navigation commands in the ViewModel
        viewModel.getEditTaskCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskEvent) {
                if (taskEvent.getContentIfNotHandled() != null) {
                    TaskDetailActivity.this.onStartEditTask();
                }
            }
        });
        viewModel.getDeleteTaskCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskEvent) {
                if (taskEvent.getContentIfNotHandled() != null) {
                    TaskDetailActivity.this.onTaskDeleted();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == ADD_EDIT_RESULT_OK) {
                // If the result comes from the add/edit screen, it's an edit.
                setResult(EDIT_RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTaskDeleted() {
        setResult(DELETE_RESULT_OK);
        // If the task was deleted successfully, go back to the list.
        finish();
    }

    @Override
    public void onStartEditTask() {
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

}
